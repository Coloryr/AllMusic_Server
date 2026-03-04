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
package net.kyori.adventure.platform.fabric.impl.mixin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import net.kyori.adventure.platform.fabric.impl.WrappedComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(net.minecraft.network.chat.Component.Serializer.class)
public abstract class ComponentSerializerMixin {
  // @formatter:off
  @Shadow public abstract JsonElement shadow$serialize(final Component text, final Type type, final JsonSerializationContext jsonSerializationContext);
  // @formatter:on

  @Inject(method = "serialize", at = @At("HEAD"), cancellable = true)
  public void adventure$writeComponentText(final Component text, final Type type, final JsonSerializationContext ctx, final CallbackInfoReturnable<JsonElement> cir) {
    if (text instanceof WrappedComponent) {
      final @Nullable Component converted = ((WrappedComponent) text).deepConvertedIfPresent();
      if (converted != null) {
        cir.setReturnValue(this.shadow$serialize(text, type, ctx));
      } else {
        cir.setReturnValue(ctx.serialize(((WrappedComponent) text).wrapped(), net.kyori.adventure.text.Component.class));
      }
    }
  }

  // inject into the anonymous function to build a gson instance
  @Inject(method = "*()Lcom/google/gson/Gson;", at = @At(value = "INVOKE_ASSIGN", target = "com/google/gson/GsonBuilder.disableHtmlEscaping()Lcom/google/gson/GsonBuilder;", remap = false),
    locals = LocalCapture.CAPTURE_FAILEXCEPTION, remap = false)
  private static void adventure$injectGson(final CallbackInfoReturnable<Gson> cir, final GsonBuilder gson) {
    GsonComponentSerializer.gson().populator().apply(gson);
  }
}
