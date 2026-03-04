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
package net.kyori.adventure.platform.fabric.impl.mixin.client;

import java.util.Locale;
import java.util.Objects;
import net.kyori.adventure.platform.fabric.impl.LocaleHolderBridge;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Options.class)
public class OptionsMixin implements LocaleHolderBridge {
  // @formatter:off
  @Shadow public String languageCode;
  // @formatter:on

  private String adventure$cachedLanguage;
  private Locale adventure$cachedLocale;

  @Override
  public Locale adventure$locale() {
    final String language = this.languageCode;
    if (Objects.equals(this.adventure$cachedLanguage, language)) {
      return this.adventure$cachedLocale;
    } else {
      this.adventure$cachedLanguage = language;
      return this.adventure$cachedLocale = LocaleHolderBridge.toLocale(language);
    }
  }
}
