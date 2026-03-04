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
package net.kyori.adventure.platform.fabric.impl.client;

import java.util.function.Function;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.fabric.FabricAudiences;
import net.kyori.adventure.platform.fabric.FabricClientAudiences;
import net.kyori.adventure.platform.fabric.impl.AdventureCommon;
import net.kyori.adventure.platform.fabric.impl.WrappedComponent;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

public class FabricClientAudiencesImpl implements FabricClientAudiences {
  public static final FabricClientAudiences INSTANCE = new Builder().build();
  private final Function<Pointered, ?> partition;
  private final ComponentRenderer<Pointered> renderer;
  private final ClientAudience audience;

  public FabricClientAudiencesImpl(final Function<Pointered, ?> partition, final ComponentRenderer<Pointered> renderer) {
    this.partition = partition;
    this.renderer = renderer;
    this.audience = new ClientAudience(Minecraft.getInstance(), this);
  }

  @Override
  public @NotNull Audience audience() {
    return this.audience;
  }

  @Override
  public @NotNull ComponentFlattener flattener() {
    return AdventureCommon.FLATTENER;
  }

  @Override
  @Deprecated
  public @NotNull PlainComponentSerializer plainSerializer() {
    return PlainComponentSerializer.plain();
  }

  @Override
  public @NotNull ComponentRenderer<Pointered> renderer() {
    return this.renderer;
  }

  @Override
  public @NotNull Component toNative(final net.kyori.adventure.text.@NotNull Component adventure) {
    return new WrappedComponent(requireNonNull(adventure, "adventure"), this.partition, this.renderer);
  }

  @Override
  public net.kyori.adventure.text.@NotNull Component toAdventure(final @NotNull Component vanilla) {
    if (vanilla instanceof WrappedComponent) {
      return ((WrappedComponent) vanilla).wrapped();
    } else {
      return FabricAudiences.nonWrappingSerializer().deserialize(vanilla);
    }
  }

  public Function<Pointered, ?> partition() {
    return this.partition;
  }

  public static final class Builder implements FabricClientAudiences.Builder {
    private Function<Pointered, ?> partition;
    private ComponentRenderer<Pointered> renderer;

    public Builder() {
      this.componentRenderer(AdventureCommon.localePartition(), GlobalTranslator.renderer());
    }

    @Override
    public FabricClientAudiences.@NotNull Builder componentRenderer(final @NotNull ComponentRenderer<Pointered> componentRenderer) {
      this.renderer = requireNonNull(componentRenderer, "componentRenderer");
      return this;
    }

    @Override
    public FabricClientAudiences.@NotNull Builder partition(final @NotNull Function<Pointered, ?> partitionFunction) {
      this.partition = requireNonNull(partitionFunction, "partitionFunction");
      return this;
    }

    @Override
    public @NotNull FabricClientAudiences build() {
      return new FabricClientAudiencesImpl(this.partition, this.renderer);
    }
  }
}
