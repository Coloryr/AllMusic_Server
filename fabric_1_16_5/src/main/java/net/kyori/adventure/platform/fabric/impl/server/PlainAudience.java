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
package net.kyori.adventure.platform.fabric.impl.server;

import java.util.function.Consumer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.platform.fabric.FabricAudiences;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

public final class PlainAudience implements Audience {
  private final FabricAudiences controller;
  private final Pointered source;
  private final Consumer<String> plainOutput;

  public PlainAudience(final FabricAudiences controller, final Pointered source, final Consumer<String> plainOutput) {
    this.controller = controller;
    this.source = source;
    this.plainOutput = plainOutput;
  }

  @Override
  public void sendMessage(final @NotNull Identity source, final @NotNull Component message, final @NotNull MessageType type) {
    this.plainOutput.accept(PlainTextComponentSerializer.plainText().serialize(this.controller.renderer().render(message, this.source)));
  }

  @Override
  public void sendActionBar(final @NotNull Component message) {
    this.sendMessage(Identity.nil(), message);
  }

  @Override
  public @NotNull Pointers pointers() {
    return this.source.pointers();
  }
}
