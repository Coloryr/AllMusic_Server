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

import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LocaleHolderBridge {
  /**
   * Take a locale string provided from a minecraft client and attempt to parse it as a locale.
   * These are not strictly compliant with the iso standard, so we try to make things a bit more normalized.
   *
   * @param mcLocale The locale string, in the format provided by the Minecraft client
   * @return A Locale object matching the provided locale string
   */
  static @Nullable Locale toLocale(final @Nullable String mcLocale) {
    if (mcLocale == null) return null;

    final String[] parts = mcLocale.split("_", 3);
    switch (parts.length) {
      case 1:
        return parts[0].isEmpty() ? null : new Locale(parts[0]);
      case 2:
        return new Locale(parts[0], parts[1]);
      case 3:
        return new Locale(parts[0], parts[1], parts[2]);
      case 0:
      default:
        return null;
    }
  }

  @NotNull Locale adventure$locale();
}
