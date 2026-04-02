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
package com.coloryr.allmusic.server.adventure;

import java.util.Locale;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import com.coloryr.allmusic.server.adventure.impl.LocaleHolderBridge;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * API for working with player locales.
 *
 * @since 4.0.0
 */
public interface PlayerLocales {
  /**
   * Registration for {@link Changed}.
   *
   * @since 4.0.0
   */
  @NotNull Event<Changed> CHANGED_EVENT = EventFactory.createArrayBacked(Changed.class, listeners -> (player, locale) -> {
    for (final Changed event : listeners) {
      event.onLocaleChanged(player, locale);
    }
  });

  /**
   * An event called when a player locale update is received.
   *
   * <p>This event is only called if the player locale has actually changed from its previous value.</p>
   *
   * @since 4.0.0
   */
  interface Changed {

    /**
     * Handle the locale change.
     *
     * @param player the player whose locale changed
     * @param newLocale the new locale
     * @since 4.0.0
     */
    void onLocaleChanged(final @NotNull ServerPlayer player, final @Nullable Locale newLocale);
  }

  /**
   * Get the active locale for a player, either on the server or client sides.
   *
   * <p>Will return the system-wide default value if the player has no locale set.</p>
   *
   * @param player the source of the locale
   * @return player locale
   * @since 4.0.0
   */
  static @NotNull Locale locale(final @NotNull Player player) {
    return player instanceof LocaleHolderBridge ? ((LocaleHolderBridge) player).adventure$locale() : Locale.getDefault();
  }
}
