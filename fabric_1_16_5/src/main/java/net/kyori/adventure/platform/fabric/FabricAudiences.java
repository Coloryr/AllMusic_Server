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

import com.mojang.authlib.GameProfile;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.platform.fabric.impl.NonWrappingComponentSerializer;
import net.kyori.adventure.platform.fabric.impl.WrappedComponent;
import net.kyori.adventure.platform.fabric.impl.accessor.ComponentSerializerAccess;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Common operations in both the client and server environments.
 *
 * <p>See {@link FabricServerAudiences} for logical server-specific operations,
 * and {@link FabricClientAudiences} for logical client-specific operations</p>
 *
 * @since 4.0.0
 */
public interface FabricAudiences {

  /**
   * Given an existing native component, convert it into an Adventure component for working with.
   *
   * @param input source component
   * @param modifier operator to transform the component
   * @return new component
   * @since 4.0.0
   */
  static net.minecraft.network.chat.@NotNull Component update(final net.minecraft.network.chat.@NotNull Component input, final UnaryOperator<Component> modifier) {
    final Component modified;
    final @Nullable Function<Pointered, ?> partition;
    final @Nullable ComponentRenderer<Pointered> renderer;
    if (input instanceof WrappedComponent) {
      modified = requireNonNull(modifier).apply(((WrappedComponent) input).wrapped());
      partition = ((WrappedComponent) input).partition();
      renderer = ((WrappedComponent) input).renderer();
    } else {
      final Component original = ComponentSerializerAccess.getGSON().fromJson(net.minecraft.network.chat.Component.Serializer.toJsonTree(input), Component.class);
      modified = modifier.apply(original);
      partition = null;
      renderer = null;
    }
    return new WrappedComponent(modified, partition, renderer);
  }

  /**
   * Convert a MC {@link ResourceLocation} instance to a text Key.
   *
   * @param loc The Identifier to convert
   * @return The equivalent data as a Key
   * @since 4.0.0
   */
  @Contract("null -> null; !null -> !null")
  static Key toAdventure(final ResourceLocation loc) {
    if (loc == null) {
      return null;
    }
    return Key.key(loc.getNamespace(), loc.getPath());
  }

  /**
   * Convert a Kyori {@link Key} instance to a MC ResourceLocation.
   *
   * @param key The Key to convert
   * @return The equivalent data as an Identifier
   * @since 4.0.0
   */
  @Contract("null -> null; !null -> !null")
  static ResourceLocation toNative(final Key key) {
    if (key == null) {
      return null;
    }
    return new ResourceLocation(key.namespace(), key.value());
  }

  /**
   * Get an {@link Entity}'s representation as an {@link Sound.Emitter} of sounds.
   *
   * @param entity the entity to convert
   * @return the entity as a sound emitter
   * @since 4.0.0
   */
  static Sound.@NotNull Emitter asEmitter(final @NotNull Entity entity) {
    return (Sound.Emitter) entity;
  }

  /**
   * Return a TextSerializer instance that will do deep conversions between
   * Adventure {@link Component Components} and Minecraft {@link net.minecraft.network.chat.Component Components}.
   *
   * <p>This serializer will never wrap text, and can provide {@link net.minecraft.network.chat.MutableComponent}
   * instances suitable for passing around the game.</p>
   *
   * @return a serializer instance
   * @since 4.0.0
   */
  static @NotNull ComponentSerializer<Component, Component, net.minecraft.network.chat.Component> nonWrappingSerializer() {
    return NonWrappingComponentSerializer.INSTANCE;
  }

  /**
   * Get the appropriate Gson component serializer for the running game version.
   *
   * <p>This can be used to convert components to their JSON representation.</p>
   *
   * @return the appropriate serializer
   * @since 4.0.0
   * @deprecated for removal, use {@link GsonComponentSerializer#gson()} instead.
   */
  @Deprecated
  static @NotNull GsonComponentSerializer gsonSerializer() {
    return GsonComponentSerializer.gson();
  }

  /**
   * Get a {@link Player} identified by their profile's {@link java.util.UUID}.
   *
   * @param player the player to identify
   * @return an identified representation of the player
   * @since 4.0.0
   */
  static @NotNull Identified identified(final @NotNull Player player) {
    return (Identified) player;
  }

  /**
   * Get an {@link Identity} representation of a {@link GameProfile}.
   *
   * @param profile the profile to represent
   * @return an identity of the game profile
   * @since 4.0.0
   */
  static @NotNull Identity identity(final @NotNull GameProfile profile) {
    return (Identity) profile;
  }

  /**
   * Return a component flattener that can use game data to resolve extra information about components.
   *
   * @return the flattener
   * @since 4.0.0
   */
  @NotNull ComponentFlattener flattener();

  /**
   * Return a {@link PlainComponentSerializer} instance that can resolve key bindings and translations using the game's data.
   *
   * @return the plain serializer instance
   * @since 4.0.0
   * @deprecated for removal since 4.0.0, use {@link PlainTextComponentSerializer#plainText()} instead. Will be removed in 1.18 snapshots.
   */
  @Deprecated
  @NotNull PlainComponentSerializer plainSerializer();

  /**
   * Active renderer to render components.
   *
   * @return Shared renderer
   * @since 4.0.0
   */
  @NotNull ComponentRenderer<Pointered> renderer();

  /**
   * Get a native {@link net.minecraft.network.chat.Component} from an adventure {@link Component}.
   *
   * <p>The specific type of the returned component is undefined. For example, it may be a wrapper object.</p>
   *
   * @param adventure adventure input
   * @return native representation
   * @since 4.0.0
   */
  net.minecraft.network.chat.@NotNull Component toNative(final @NotNull Component adventure);

  /**
   * Get an adventure {@link Component} from a native {@link net.minecraft.network.chat.Component}.
   *
   * @param vanilla the native component
   * @return adventure component
   * @since 4.0.0
   */
  @NotNull Component toAdventure(final net.minecraft.network.chat.@NotNull Component vanilla);
}
