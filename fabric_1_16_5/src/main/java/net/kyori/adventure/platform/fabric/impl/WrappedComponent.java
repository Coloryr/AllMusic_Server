/*
 * This file is part of adventure-platform-fabric, licensed under the MIT License.
 *
 * Copyright (c) 2021 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.adventure.platform.fabric.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyori.adventure.platform.fabric.FabricAudiences;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Nullable;

public final class WrappedComponent implements Component {
  private Component converted;
  private @Nullable Object deepConvertedLocalized = null;
  private final net.kyori.adventure.text.Component wrapped;
  private final @Nullable Function<Pointered, ?> partition;
  private final @Nullable ComponentRenderer<Pointered> renderer;
  private @Nullable Object lastData;
  private @Nullable WrappedComponent lastRendered;

  public WrappedComponent(final net.kyori.adventure.text.Component wrapped, final @Nullable Function<Pointered, ?> partition, final @Nullable ComponentRenderer<Pointered> renderer) {
    this.wrapped = wrapped;
    this.partition = partition;
    this.renderer = renderer;
  }

  /**
   * Renderer to use to translate messages.
   *
   * @return the renderer, if any
   */
  public @Nullable ComponentRenderer<Pointered> renderer() {
    return this.renderer;
  }

  /**
   * Partition to use to generate cache keys.
   *
   * @return the partition, if any
   */
  public @Nullable Function<Pointered, ?> partition() {
    return this.partition;
  }

  public net.kyori.adventure.text.Component wrapped() {
    return this.wrapped;
  }

  public synchronized WrappedComponent rendered(final Pointered ptr) {
    final Object data = this.partition == null ? null : this.partition.apply(ptr);
    if (this.lastData != null && Objects.equals(data, this.lastData)) {
      return this.lastRendered;
    }
    this.lastData = data;
    return this.lastRendered = this.renderer == null ? this : new WrappedComponent(this.renderer.render(this.wrapped, ptr), null, null);
  }

  Component deepConverted() {
    Component converted = this.converted;
    if (converted == null || this.deepConvertedLocalized != null) {
      converted = this.converted = FabricAudiences.nonWrappingSerializer().serialize(this.wrapped);
      this.deepConvertedLocalized = null;
    }
    return converted;
  }

  @Environment(EnvType.CLIENT)
  Component deepConvertedLocalized() {
    Component converted = this.converted;
    final Pointered target = (Pointered) Minecraft.getInstance().player;
    final Object data = this.partition == null ? null : this.partition.apply(target);
    if (converted == null || this.deepConvertedLocalized != data) {
      converted = this.converted = this.rendered(target).deepConverted();
      this.deepConvertedLocalized = data;
    }
    return converted;
  }

  public @Nullable Component deepConvertedIfPresent() {
    return this.converted;
  }

  @Override
  public Style getStyle() {
    return this.deepConverted().getStyle();
  }

  @Override
  public String getString() {
    return PlainTextComponentSerializer.plainText().serialize(this.rendered(AdventureCommon.pointered(Pointers::empty)).wrapped);
  }

  @Override
  public String getString(final int length) {
    return this.deepConverted().getString(length);
  }

  @Override
  public String getContents() {
    if (this.wrapped instanceof TextComponent) {
      return ((TextComponent) this.wrapped).content();
    } else {
      return this.deepConverted().getContents();
    }
  }

  @Override
  public List<Component> getSiblings() {
    return this.deepConverted().getSiblings();
  }

  @Override
  public MutableComponent plainCopy() {
    return this.deepConverted().plainCopy();
  }

  @Override
  public MutableComponent copy() {
    return this.deepConverted().copy();
  }

  @Override
  @Environment(EnvType.CLIENT)
  public FormattedCharSequence getVisualOrderText() {
    return this.deepConvertedLocalized().getVisualOrderText();
  }

  @Override
  @Environment(EnvType.CLIENT)
  public <T> Optional<T> visit(final StyledContentConsumer<T> visitor, final Style style) {
    return this.deepConvertedLocalized().visit(visitor, style);
  }

  @Override
  public <T> Optional<T> visit(final ContentConsumer<T> visitor) {
    return this.deepConverted().visit(visitor);
  }
}
