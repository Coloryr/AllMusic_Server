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

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.adventure.key.Key;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * An argument that will be decoded as a Key.
 *
 * @since 4.0.0
 */
public final class KeyArgumentType implements ArgumentType<Key> {
  private static final KeyArgumentType INSTANCE = new KeyArgumentType();

  /**
   * Get an argument type instance for {@link Key}s.
   *
   * @return key argument type
   * @since 4.0.0
   */
  public static @NotNull KeyArgumentType key() {
    return INSTANCE;
  }

  /**
   * Get a {@link Key}-typed value from a parsed {@link CommandContext}.
   *
   * @param ctx context to get the value from
   * @param id id the argument was taken from
   * @return provided argument
   * @since 4.0.0
   */
  public static @NotNull Key key(final @NotNull CommandContext<?> ctx, final @NotNull String id) {
    return ctx.getArgument(id, Key.class);
  }

  private KeyArgumentType() {
  }

  @Override
  public @NotNull Key parse(final @NotNull StringReader reader) throws CommandSyntaxException {
    // TODO: do this without creating a ResourceLocation instance
    return FabricAudiences.toAdventure(ResourceLocation.read(reader));
  }
}
