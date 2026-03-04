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
package net.kyori.adventure.platform.fabric;

import java.util.Locale;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.fabric.impl.AdventureCommon;
import net.kyori.adventure.platform.fabric.impl.server.FabricServerAudiencesImpl;
import net.kyori.adventure.platform.fabric.impl.server.MinecraftServerBridge;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

/**
 * Provides {@link Audience} instances for a specific server instance.
 *
 * @since 4.0.0
 */
public interface FabricServerAudiences extends AudienceProvider, FabricAudiences {
  /**
   * Get the shared audience provider for the server.
   *
   * <p>This provider will render messages using the global translation registry.</p>
   *
   * @param server server instance to work with
   * @return common audience provider
   * @since 4.0.0
   */
  static @NotNull FabricServerAudiences of(final @NotNull MinecraftServer server) {
    return ((MinecraftServerBridge) server).adventure$globalProvider();
  }

  /**
   * Create an audience provider for this server with customized settings.
   *
   * @param server the server
   * @return audience provider builder
   * @since 4.0.0
   */
  static @NotNull Builder builder(final @NotNull MinecraftServer server) {
    return new FabricServerAudiencesImpl.Builder(requireNonNull(server, "server"));
  }

  /**
   * Get a customized audience provider for the server.
   *
   * <p>This provider will render messages using the global translation registry.</p>
   *
   * @param server server to work in
   * @param renderer renderer for messages.
   * @return new audience provider
   * @since 4.0.0
   * @deprecated for removal, use {@link #builder(MinecraftServer)} instead
   */
  @Deprecated
  static @NotNull FabricServerAudiences of(final @NotNull MinecraftServer server, final @NotNull ComponentRenderer<Locale> renderer) {
    return FabricServerAudiences.builder(server)
      .componentRenderer(AdventureCommon.localePartition(), renderer)
      .build();
  }

  /**
   * Get an audience to send to a {@link CommandSourceStack}.
   *
   * <p>This will delegate to the native implementation by the command source, or
   * otherwise use a wrapping implementation.</p>
   *
   * @param source source to send to.
   * @return the audience
   * @since 4.0.0
   */
  @NotNull AdventureCommandSourceStack audience(@NotNull CommandSourceStack source);

  /**
   * Get an audience that will send to the provided {@link CommandSource}.
   *
   * <p>Depending on the specific source, the returned audience may only support
   * a subset of abilities.</p>
   *
   * @param source source to send to
   * @return an audience for the source
   * @since 4.0.0
   */
  @NotNull Audience audience(@NotNull CommandSource source);

  /**
   * Create an audience that will send to every listed player.
   *
   * @param players Players to send to.
   * @return a new audience
   * @since 4.0.0
   */
  @NotNull Audience audience(@NotNull Iterable<ServerPlayer> players);

  /**
   * Builder for {@link FabricServerAudiences} with custom attributes.
   *
   * @since 4.0.0
   */
  interface Builder extends AudienceProvider.Builder<FabricServerAudiences, Builder> {
  }
}
