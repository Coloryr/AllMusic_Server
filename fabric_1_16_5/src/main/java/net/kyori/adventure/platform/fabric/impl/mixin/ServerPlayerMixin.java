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

import com.google.common.collect.MapMaker;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.kyori.adventure.platform.fabric.PlayerLocales;
import net.kyori.adventure.platform.fabric.impl.LocaleHolderBridge;
import net.kyori.adventure.platform.fabric.impl.accessor.ClientboundTabListPacketAccess;
import net.kyori.adventure.platform.fabric.impl.accessor.ConnectionAccess;
import net.kyori.adventure.platform.fabric.impl.accessor.ServerboundClientInformationPacketAccess;
import net.kyori.adventure.platform.fabric.impl.server.FabricServerAudiencesImpl;
import net.kyori.adventure.platform.fabric.impl.server.FriendlyByteBufBridge;
import net.kyori.adventure.platform.fabric.impl.server.RenderableAudience;
import net.kyori.adventure.platform.fabric.impl.server.ServerPlayerAudience;
import net.kyori.adventure.platform.fabric.impl.server.ServerPlayerBridge;
import net.kyori.adventure.pointer.Pointers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends PlayerMixin implements ForwardingAudience.Single, LocaleHolderBridge, RenderableAudience, ServerPlayerBridge {
  // @formatter:off
  @Shadow @Final public MinecraftServer server;
  @Shadow public ServerGamePacketListenerImpl connection;
  // @formatter:on

  private Audience adventure$backing;
  private Locale adventure$locale;
  private final Map<FabricServerAudiencesImpl, Audience> adventure$renderers = new MapMaker().weakKeys().makeMap();
  private Component adventure$tabListHeader = TextComponent.EMPTY;
  private Component adventure$tabListFooter = TextComponent.EMPTY;

  protected ServerPlayerMixin(final EntityType<? extends LivingEntity> entityType, final Level level) {
    super(entityType, level);
  }

  @Inject(method = "<init>", at = @At("TAIL"))
  private void adventure$init(final CallbackInfo ci) {
    this.adventure$backing = FabricServerAudiences.of(this.server).audience(this);
  }

  @Override
  public @NotNull Audience audience() {
    return this.adventure$backing;
  }

  @Override
  public Audience renderUsing(final FabricServerAudiencesImpl controller) {
    return this.adventure$renderers.computeIfAbsent(controller, ctrl -> new ServerPlayerAudience((ServerPlayer) (Object) this, ctrl));
  }

  @Override
  public @NotNull Locale adventure$locale() {
    return this.adventure$locale;
  }

  @Override
  protected void adventure$populateExtraPointers(final Pointers.Builder builder) {
    builder.withDynamic(Identity.LOCALE, this::adventure$locale);
  }

  // Tab list

  @Override
  public void bridge$updateTabList(final @Nullable Component header, final @Nullable Component footer) {
    if (header != null) {
      this.adventure$tabListHeader = header;
    }
    if (footer != null) {
      this.adventure$tabListFooter = footer;
    }
    final ClientboundTabListPacket packet = new ClientboundTabListPacket();
    ((ClientboundTabListPacketAccess) packet).setHeader(this.adventure$tabListHeader);
    ((ClientboundTabListPacketAccess) packet).setFooter(this.adventure$tabListFooter);

    this.connection.send(packet);
  }


  // Locale tracking

  @Inject(method = "updateOptions", at = @At("HEAD"))
  private void adventure$handleLocaleUpdate(final ServerboundClientInformationPacket information, final CallbackInfo ci) {
    final String language = ((ServerboundClientInformationPacketAccess) information).getLanguage();
    final @Nullable Locale locale = LocaleHolderBridge.toLocale(language);
    if (!Objects.equals(this.adventure$locale, locale)) {
      this.adventure$locale = locale;
      PlayerLocales.CHANGED_EVENT.invoker().onLocaleChanged((ServerPlayer) (Object) this, locale);
    }
  }

  // Player tracking for boss bars and rendering

  @Inject(method = "restoreFrom", at = @At("RETURN"))
  private void copyData(final ServerPlayer old, final boolean alive, final CallbackInfo ci) {
    FabricServerAudiencesImpl.forEachInstance(controller -> controller.bossBars().replacePlayer(old, (ServerPlayer) (Object) this));
    ((ConnectionAccess) this.connection.connection).getChannel().attr(FriendlyByteBufBridge.CHANNEL_RENDER_DATA).set(this);
  }

  @Inject(method = "disconnect", at = @At("RETURN"))
  private void adventure$removeBossBarsOnDisconnect(final CallbackInfo ci) {
    FabricServerAudiencesImpl.forEachInstance(controller -> controller.bossBars().unsubscribeFromAll((ServerPlayer) (Object) this));
  }
}
