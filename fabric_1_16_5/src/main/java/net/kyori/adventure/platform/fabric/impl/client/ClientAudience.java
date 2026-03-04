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

import java.time.Duration;
import java.util.Objects;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.fabric.FabricAudiences;
import net.kyori.adventure.platform.fabric.impl.GameEnums;
import net.kyori.adventure.platform.fabric.impl.PointerProviderBridge;
import net.kyori.adventure.platform.fabric.impl.accessor.client.AbstractSoundInstanceAccess;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.ChatVisiblity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClientAudience implements Audience {
  private final Minecraft client;
  private final FabricClientAudiencesImpl controller;

  public ClientAudience(final Minecraft client, final FabricClientAudiencesImpl renderer) {
    this.client = client;
    this.controller = renderer;
  }

  @Override
  public void sendMessage(final Identity source, final @NotNull Component message, final @NotNull MessageType type) {
    if (this.client.isBlocked(source.uuid())) return;

    final ChatVisiblity visibility = this.client.options.chatVisibility;
    if (type == MessageType.CHAT) {
      // Add to chat queue (following delay and such)
      if (visibility == ChatVisiblity.FULL) {
        this.client.gui.getChat().enqueueMessage(this.controller.toNative(message));
      }
    } else {
      // Add immediately as a system message
      if (visibility == ChatVisiblity.FULL || visibility == ChatVisiblity.SYSTEM) {
        this.client.gui.getChat().addMessage(this.controller.toNative(message));
      }
    }
  }

  @Override
  public void sendActionBar(final @NotNull Component message) {
    this.client.gui.setOverlayMessage(this.controller.toNative(message), false);
  }

  @Override
  public void showTitle(final @NotNull Title title) {
    final net.minecraft.network.chat.@Nullable Component titleText = title.title() == Component.empty() ? null : this.controller.toNative(title.title());
    final net.minecraft.network.chat.@Nullable Component subtitleText = title.subtitle() == Component.empty() ? null : this.controller.toNative(title.subtitle());
    final Title.@Nullable Times times = title.times();
    this.client.gui.setTitles(titleText, subtitleText,
      this.adventure$ticks(times == null ? null : times.fadeIn()),
      this.adventure$ticks(times == null ? null : times.stay()),
      this.adventure$ticks(times == null ? null : times.fadeOut()));
  }

  @Override
  public <T> void sendTitlePart(final @NotNull TitlePart<T> part, @NotNull final T value) {
    Objects.requireNonNull(value, "value");
    if (part == TitlePart.TITLE) {
      this.client.gui.setTitles(this.controller.toNative((Component) value), null, -1, -1, -1);
    } else if (part == TitlePart.SUBTITLE) {
      this.client.gui.setTitles(null, this.controller.toNative((Component) value), -1, -1, -1);
    } else if (part == TitlePart.TIMES) {
      final Title.Times times = (Title.Times) value;
      this.client.gui.setTitles(
        null,
        null,
        this.adventure$ticks(times.fadeIn()),
        this.adventure$ticks(times.stay()),
        this.adventure$ticks(times.fadeOut())
      );
    } else {
      throw new IllegalArgumentException("Unknown TitlePart '" + part + "'");
    }
  }

  private int adventure$ticks(final @Nullable Duration duration) {
    return duration == null || duration.getSeconds() == -1 ? -1 : (int) (duration.toMillis() / 50);
  }

  @Override
  public void clearTitle() {
    this.client.gui.setTitles(null, null, -1, -1, -1);
  }

  @Override
  public void resetTitle() {
    this.client.gui.resetTitleTimes();
  }

  @Override
  public void showBossBar(final @NotNull BossBar bar) {
    BossHealthOverlayBridge.listener(this.client.gui.getBossOverlay(), this.controller).add(bar);
  }

  @Override
  public void hideBossBar(final @NotNull BossBar bar) {
    BossHealthOverlayBridge.listener(this.client.gui.getBossOverlay(), this.controller).remove(bar);
  }

  @Override
  public void playSound(final @NotNull Sound sound) {
    final LocalPlayer player = this.client.player;
    if (player != null) {
      this.playSound(sound, player.getX(), player.getY(), player.getZ());
    } else {
      // not in-game
      this.client.getSoundManager().play(new SimpleSoundInstance(FabricAudiences.toNative(sound.name()), GameEnums.SOUND_SOURCE.toMinecraft(sound.source()),
        sound.volume(), sound.pitch(), false, 0, SoundInstance.Attenuation.NONE, 0, 0, 0, true));
    }
  }

  @Override
  public void playSound(final @NotNull Sound sound, final Sound.@NotNull Emitter emitter) {
    final Entity targetEntity;
    if (emitter == Sound.Emitter.self()) {
      targetEntity = this.client.player;
    } else if (emitter instanceof Entity) {
      targetEntity = (Entity) emitter;
    } else {
      throw new IllegalArgumentException("Provided emitter '" + emitter + "' was not Sound.Emitter.self() or an Entity");
    }

    // Initialize with a placeholder event
    final EntityBoundSoundInstance mcSound = new EntityBoundSoundInstance(SoundEvents.ITEM_PICKUP, GameEnums.SOUND_SOURCE.toMinecraft(sound.source()), sound.volume(), sound.pitch(), targetEntity);
    // Then apply the ResourceLocation of our real sound event
    ((AbstractSoundInstanceAccess) mcSound).setLocation(FabricAudiences.toNative(sound.name()));

    this.client.getSoundManager().play(mcSound);
  }

  @Override
  public void playSound(final @NotNull Sound sound, final double x, final double y, final double z) {
    this.client.getSoundManager().play(new SimpleSoundInstance(FabricAudiences.toNative(sound.name()), GameEnums.SOUND_SOURCE.toMinecraft(sound.source()),
      sound.volume(), sound.pitch(), false, 0, SoundInstance.Attenuation.LINEAR, x, y, z, false));
  }

  @Override
  public void stopSound(final @NotNull SoundStop stop) {
    final @Nullable Key sound = stop.sound();
    final @Nullable ResourceLocation soundIdent = sound == null ? null : FabricAudiences.toNative(sound);
    final Sound.@Nullable Source source = stop.source();
    final @Nullable SoundSource category = source == null ? null : GameEnums.SOUND_SOURCE.toMinecraft(source);
    this.client.getSoundManager().stop(soundIdent, category);
  }

  @Override
  public void openBook(final @NotNull Book book) {
    this.client.setScreen(new BookViewScreen(new AdventureBookAccess(book, this.controller.partition(), this.controller.renderer())));
  }

  @Override
  public void sendPlayerListHeader(final @NotNull Component header) {
    this.client.gui.getTabList().setHeader(header == Component.empty() ? null : this.controller.toNative(header));
  }

  @Override
  public void sendPlayerListFooter(final @NotNull Component footer) {
    this.client.gui.getTabList().setHeader(footer == Component.empty() ? null : this.controller.toNative(footer));
  }

  @Override
  public void sendPlayerListHeaderAndFooter(final @NotNull Component header, final @NotNull Component footer) {
    this.sendPlayerListHeader(header);
    this.sendPlayerListFooter(footer);
  }

  @Override
  public @NotNull Pointers pointers() {
    final @Nullable LocalPlayer clientPlayer = this.client.player;
    if (clientPlayer != null) {
      return ((PointerProviderBridge) clientPlayer).adventure$pointers();
    } else {
      return Audience.super.pointers();
    }
  }
}
