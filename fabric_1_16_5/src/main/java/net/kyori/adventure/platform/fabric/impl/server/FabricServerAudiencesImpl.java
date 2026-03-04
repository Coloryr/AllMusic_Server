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

import com.google.common.collect.Iterables;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.fabric.AdventureCommandSourceStack;
import net.kyori.adventure.platform.fabric.FabricAudiences;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.kyori.adventure.platform.fabric.impl.AdventureCommandSourceStackInternal;
import net.kyori.adventure.platform.fabric.impl.AdventureCommon;
import net.kyori.adventure.platform.fabric.impl.WrappedComponent;
import net.kyori.adventure.platform.fabric.impl.accessor.ComponentSerializerAccess;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * The entry point for accessing Adventure.
 */
public final class FabricServerAudiencesImpl implements FabricServerAudiences {
  private static final Set<FabricServerAudiencesImpl> INSTANCES = Collections.newSetFromMap(new WeakHashMap<>());

  /**
   * Perform an action on every audience provider instance.
   *
   * @param actor a consumer that will be called for every provider
   */
  public static void forEachInstance(final Consumer<FabricServerAudiencesImpl> actor) {
    synchronized (INSTANCES) {
      for (final FabricServerAudiencesImpl instance : INSTANCES) {
        actor.accept(instance);
      }
    }
  }

  private final MinecraftServer server;
  private final Function<Pointered, ?> partition;
  private final ComponentRenderer<Pointered> renderer;
  final ServerBossBarListener bossBars;

  public FabricServerAudiencesImpl(final MinecraftServer server, final Function<Pointered, ?> partition, final ComponentRenderer<Pointered> renderer) {
    this.server = server;
    this.partition = partition;
    this.renderer = renderer;
    this.bossBars = new ServerBossBarListener(this);
    synchronized (INSTANCES) {
      INSTANCES.add(this);
    }
  }

  @Override
  public @NotNull Audience all() {
    return Audience.audience(this.console(), this.players());
  }

  @Override
  public @NotNull Audience console() {
    return this.audience(this.server);
  }

  @Override
  public @NotNull Audience players() {
    return Audience.audience(this.audiences(this.server.getPlayerList().getPlayers()));
  }

  @Override
  public @NotNull Audience player(final @NotNull UUID playerId) {
    final @Nullable ServerPlayer player = this.server.getPlayerList().getPlayer(playerId);
    return player != null ? this.audience(player) : Audience.empty();
  }

  private Iterable<Audience> audiences(final Iterable<? extends ServerPlayer> players) {
    return Iterables.transform(players, this::audience);
  }

  @Override
  public @NotNull Audience permission(final @NotNull String permission) {
    return Audience.empty(); // TODO: permissions api
  }

  @Override
  public @NotNull AdventureCommandSourceStack audience(final @NotNull CommandSourceStack source) {
    if (!(source instanceof AdventureCommandSourceStackInternal)) {
      throw new IllegalArgumentException("The AdventureCommandSource mixin failed!");
    }

    final AdventureCommandSourceStackInternal internal = (AdventureCommandSourceStackInternal) source;
    return internal.adventure$audience(this.audience(internal.adventure$source()), this);
  }

  @Override
  public @NotNull Audience audience(final @NotNull CommandSource source) {
    if (source instanceof RenderableAudience) {
      return ((RenderableAudience) source).renderUsing(this);
    } else if (source instanceof Audience) {
      // TODO: How to pass component renderer through
      return (Audience) source;
    } else {
      return new CommandSourceAudience(source, this);
    }
  }

  @Override
  public @NotNull Audience audience(final @NotNull Iterable<ServerPlayer> players) {
    return Audience.audience(this.audiences(players));
  }

  @Override
  public @NotNull Audience world(final @NotNull Key worldId) {
    final @Nullable ServerLevel level = this.server.getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY,
      FabricAudiences.toNative(requireNonNull(worldId, "worldId"))));
    if (level != null) {
      return this.audience(level.players());
    }
    return Audience.empty();
  }

  @Override
  public @NotNull Audience server(final @NotNull String serverName) {
    return Audience.empty();
  }

  @Override
  public @NotNull ComponentFlattener flattener() {
    return AdventureCommon.FLATTENER;
  }

  @Override
  @Deprecated
  public @NotNull PlainComponentSerializer plainSerializer() {
    return PlainComponentSerializer.plain();
  }

  @Override
  public @NotNull ComponentRenderer<Pointered> renderer() {
    return this.renderer;
  }

  @Override
  public net.minecraft.network.chat.@NotNull Component toNative(final @NotNull Component adventure) {
    if (adventure == Component.empty()) return TextComponent.EMPTY;

    return new WrappedComponent(requireNonNull(adventure, "adventure"), this.partition, this.renderer);
  }

  @Override
  public @NotNull Component toAdventure(final net.minecraft.network.chat.@NotNull Component vanilla) {
    if (vanilla instanceof WrappedComponent) {
      return ((WrappedComponent) vanilla).wrapped();
    } else {
      return ComponentSerializerAccess.getGSON().fromJson(net.minecraft.network.chat.Component.Serializer.toJsonTree(vanilla), Component.class);
    }
  }

  public ServerBossBarListener bossBars() {
    return this.bossBars;
  }

  @Override
  public void close() {
  }

  public static final class Builder implements FabricServerAudiences.Builder {
    private final MinecraftServer server;
    private Function<Pointered, ?> partition;
    private ComponentRenderer<Pointered> renderer;

    public Builder(final MinecraftServer server) {
      this.server = server;
      this.componentRenderer(AdventureCommon.localePartition(), GlobalTranslator.renderer());
    }

    @Override
    public @NotNull FabricServerAudiences.Builder componentRenderer(final @NotNull ComponentRenderer<Pointered> componentRenderer) {
      this.renderer = requireNonNull(componentRenderer, "componentRenderer");
      return this;
    }

    @Override
    public @NotNull FabricServerAudiences.Builder partition(final @NotNull Function<Pointered, ?> partitionFunction) {
      this.partition = requireNonNull(partitionFunction, "partitionFunction");
      return this;
    }

    @Override
    public @NotNull FabricServerAudiencesImpl build() {
      return new FabricServerAudiencesImpl(this.server, this.partition, this.renderer);
    }
  }
}
