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

import java.time.Duration;
import java.util.Objects;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.fabric.FabricAudiences;
import net.kyori.adventure.platform.fabric.impl.GameEnums;
import net.kyori.adventure.platform.fabric.impl.PointerProviderBridge;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesPacket;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

public final class ServerPlayerAudience implements Audience {
  private final ServerPlayer player;
  private final FabricServerAudiencesImpl controller;

  public ServerPlayerAudience(final ServerPlayer player, final FabricServerAudiencesImpl controller) {
    this.player = player;
    this.controller = controller;
  }

  void sendPacket(final Packet<?> packet) {
    this.player.connection.send(packet);
  }

  @Override
  public void sendMessage(final Identity source, final Component text, final net.kyori.adventure.audience.MessageType type) {
    final ChatType mcType;
    if (type == net.kyori.adventure.audience.MessageType.CHAT) {
      mcType = ChatType.CHAT;
    } else {
      mcType = ChatType.SYSTEM;
    }

    this.sendPacket(new ClientboundChatPacket(this.controller.toNative(text), mcType, source.uuid()));
  }

  @Override
  public void sendActionBar(final @NotNull Component message) {
    this.sendPacket(new ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.ACTIONBAR, this.controller.toNative(message)));
  }

  @Override
  public void showBossBar(final @NotNull BossBar bar) {
    FabricServerAudiencesImpl.forEachInstance(controller -> {
      if (controller != this.controller) {
        controller.bossBars.unsubscribe(this.player, bar);
      }
    });
    this.controller.bossBars.subscribe(this.player, bar);
  }

  @Override
  public void hideBossBar(final @NotNull BossBar bar) {
    FabricServerAudiencesImpl.forEachInstance(controller -> controller.bossBars.unsubscribe(this.player, bar));
  }

  @Override
  public void playSound(final @NotNull Sound sound) {
    this.sendPacket(new ClientboundCustomSoundPacket(FabricAudiences.toNative(sound.name()),
      GameEnums.SOUND_SOURCE.toMinecraft(sound.source()), this.player.position(), sound.volume(), sound.pitch()));
  }

  @Override
  public void playSound(final @NotNull Sound sound, final double x, final double y, final double z) {
    this.sendPacket(new ClientboundCustomSoundPacket(FabricAudiences.toNative(sound.name()),
      GameEnums.SOUND_SOURCE.toMinecraft(sound.source()), new Vec3(x, y, z), sound.volume(), sound.pitch()));
  }

  @Override
  public void playSound(final @NotNull Sound sound, final Sound.@NotNull Emitter emitter) {
    final Entity targetEntity;
    if (emitter == Sound.Emitter.self()) {
      targetEntity = this.player;
    } else if (emitter instanceof Entity) {
      targetEntity = (Entity) emitter;
    } else {
      throw new IllegalArgumentException("Provided emitter '" + emitter + "' was not Sound.Emitter.self() or an Entity");
    }

    if (!this.player.level.equals(targetEntity.level)) {
      // don't send unless entities are in the same dimension
      return;
    }

    final SoundEvent event = Registry.SOUND_EVENT.getOptional(FabricAudiences.toNative(sound.name())).orElse(null);
    if (event == null) {
      return;
    }

    this.sendPacket(new ClientboundSoundEntityPacket(event, GameEnums.SOUND_SOURCE.toMinecraft(sound.source()), targetEntity, sound.volume(), sound.pitch()));
  }

  @Override
  public void stopSound(final @NotNull SoundStop stop) {
    final @Nullable Key sound = stop.sound();
    final Sound.@Nullable Source src = stop.source();
    final @Nullable SoundSource cat = src == null ? null : GameEnums.SOUND_SOURCE.toMinecraft(src);
    this.sendPacket(new ClientboundStopSoundPacket(sound == null ? null : FabricAudiences.toNative(sound), cat));
  }

  static final String BOOK_TITLE = "title";
  static final String BOOK_AUTHOR = "author";
  static final String BOOK_PAGES = "pages";
  static final String BOOK_RESOLVED = "resolved";

  @Override
  public void openBook(final @NotNull Book book) {
    final ItemStack bookStack = new ItemStack(Items.WRITTEN_BOOK, 1);
    final CompoundTag bookTag = bookStack.getOrCreateTag();
    bookTag.putString(BOOK_TITLE, this.adventure$plain(book.title()));
    bookTag.putString(BOOK_AUTHOR, this.adventure$plain(book.author()));
    final ListTag pages = new ListTag();
    for (final Component page : book.pages()) {
      pages.add(StringTag.valueOf(this.adventure$serialize(page)));
    }
    bookTag.put(BOOK_PAGES, pages);
    bookTag.putBoolean(BOOK_RESOLVED, true); // todo: any parseable texts?

    final ItemStack previous = this.player.inventory.getSelected();
    this.sendPacket(new ClientboundContainerSetSlotPacket(-2, this.player.inventory.selected, bookStack));
    this.player.openItemGui(bookStack, InteractionHand.MAIN_HAND);
    this.sendPacket(new ClientboundContainerSetSlotPacket(-2, this.player.inventory.selected, previous));
  }

  private String adventure$plain(final @NotNull Component component) {
    return PlainTextComponentSerializer.plainText().serialize(this.controller.renderer().render(component, this));
  }

  private String adventure$serialize(final @NotNull Component component) {
    return GsonComponentSerializer.gson().serialize(this.controller.renderer().render(component, this));
  }

  @Override
  public void showTitle(final @NotNull Title title) {
    if (title.subtitle() != Component.empty()) {
      this.sendPacket(new ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.SUBTITLE, this.controller.toNative(title.subtitle())));
    }

    final Title.@Nullable Times times = title.times();
    if (times != null) {
      final int fadeIn = ticks(times.fadeIn());
      final int fadeOut = ticks(times.fadeOut());
      final int dwell = ticks(times.stay());
      if (fadeIn != -1 || fadeOut != -1 || dwell != -1) {
        this.sendPacket(new ClientboundSetTitlesPacket(fadeIn, dwell, fadeOut));
      }
    }

    if (title.title() != Component.empty()) {
      this.sendPacket(new ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.TITLE, this.controller.toNative(title.title())));
    }
  }

  @Override
  public <T> void sendTitlePart(final @NotNull TitlePart<T> part, @NotNull final T value) {
    Objects.requireNonNull(value, "value");
    if (part == TitlePart.TITLE) {
      this.sendPacket(new ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.TITLE, this.controller.toNative((Component) value)));
    } else if (part == TitlePart.SUBTITLE) {
      this.sendPacket(new ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.SUBTITLE, this.controller.toNative((Component) value)));
    } else if (part == TitlePart.TIMES) {
      final Title.Times times = (Title.Times) value;
      this.sendPacket(new ClientboundSetTitlesPacket(ticks(times.fadeIn()), ticks(times.stay()), ticks(times.fadeOut())));
    } else {
      throw new IllegalArgumentException("Unknown TitlePart '" + part + "'");
    }
  }

  static int ticks(final @NotNull Duration duration) {
    return duration.getSeconds() == -1 ? -1 : (int) (duration.toMillis() / 50);
  }

  @Override
  public void clearTitle() {
    this.sendPacket(new ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.CLEAR, null));
  }

  @Override
  public void resetTitle() {
    this.sendPacket(new ClientboundSetTitlesPacket(ClientboundSetTitlesPacket.Type.RESET, null));
  }

  @Override
  public void sendPlayerListHeader(final @NotNull Component header) {
    requireNonNull(header, "header");
    ((ServerPlayerBridge) this.player).bridge$updateTabList(this.controller.toNative(header), null);
  }

  @Override
  public void sendPlayerListFooter(final @NotNull Component footer) {
    requireNonNull(footer, "footer");
    ((ServerPlayerBridge) this.player).bridge$updateTabList(null, this.controller.toNative(footer));

  }

  @Override
  public void sendPlayerListHeaderAndFooter(final @NotNull Component header, final @NotNull Component footer) {
    ((ServerPlayerBridge) this.player).bridge$updateTabList(
      this.controller.toNative(requireNonNull(header, "header")),
      this.controller.toNative(requireNonNull(footer, "footer")));
  }

  @Override
  public @NotNull Pointers pointers() {
    return ((PointerProviderBridge) this.player).adventure$pointers();
  }
}
