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

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.platform.fabric.FabricAudiences;
import net.kyori.adventure.text.Component;
import net.minecraft.world.BossEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractBossBarListener<T extends BossEvent> implements BossBar.Listener {
  private final FabricAudiences controller;
  protected final Map<BossBar, T> bars = new IdentityHashMap<>();

  protected AbstractBossBarListener(final FabricAudiences controller) {
    this.controller = controller;
  }

  @Override
  public void bossBarNameChanged(final @NotNull BossBar bar, final @NotNull Component oldName, final @NotNull Component newName) {
    if (!oldName.equals(newName)) {
      this.minecraft(bar).setName(this.controller.toNative(newName));
    }
  }

  @Override
  public void bossBarProgressChanged(final @NotNull BossBar bar, final float oldPercent, final float newPercent) {
    if (oldPercent != newPercent) {
      this.minecraft(bar).setPercent(newPercent);
    }
  }

  @Override
  public void bossBarColorChanged(final @NotNull BossBar bar, final BossBar.@NotNull Color oldColor, final BossBar.@NotNull Color newColor) {
    if (oldColor != newColor) {
      this.minecraft(bar).setColor(GameEnums.BOSS_BAR_COLOR.toMinecraft(newColor));
    }
  }

  @Override
  public void bossBarOverlayChanged(final @NotNull BossBar bar, final BossBar.@NotNull Overlay oldOverlay, final BossBar.@NotNull Overlay newOverlay) {
    if (oldOverlay != newOverlay) {
      this.minecraft(bar).setOverlay(GameEnums.BOSS_BAR_OVERLAY.toMinecraft(newOverlay));
    }
  }

  @Override
  public void bossBarFlagsChanged(final @NotNull BossBar bar, final @NotNull Set<BossBar.Flag> flagsRemoved, final @NotNull Set<BossBar.Flag> flagsAdded) {
    updateFlags(this.minecraft(bar), bar.flags());
  }

  private static void updateFlags(final @NotNull BossEvent bar, final @NotNull Set<BossBar.Flag> flags) {
    bar.setCreateWorldFog(flags.contains(BossBar.Flag.CREATE_WORLD_FOG));
    bar.setDarkenScreen(flags.contains(BossBar.Flag.DARKEN_SCREEN));
    bar.setPlayBossMusic(flags.contains(BossBar.Flag.PLAY_BOSS_MUSIC));
  }

  private T minecraft(final @NotNull BossBar bar) {
    final @Nullable T mc = this.bars.get(bar);
    if (mc == null) {
      throw new IllegalArgumentException("Unknown boss bar instance " + bar);
    }
    return mc;
  }

  protected abstract T newBar(final net.minecraft.network.chat.@NotNull Component title,
                              final BossEvent.@NotNull BossBarColor color,
                              final BossEvent.@NotNull BossBarOverlay style,
                              final float progress);

  protected T minecraftCreating(final @NotNull BossBar bar) {
    return this.bars.computeIfAbsent(bar, key -> {
      final T ret = this.newBar(this.controller.toNative(key.name()),
        GameEnums.BOSS_BAR_COLOR.toMinecraft(key.color()),
        GameEnums.BOSS_BAR_OVERLAY.toMinecraft(key.overlay()),
        key.progress());

      updateFlags(ret, key.flags());
      key.addListener(this);
      return ret;
    });
  }
}
