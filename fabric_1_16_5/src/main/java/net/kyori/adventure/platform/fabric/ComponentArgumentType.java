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

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import net.kyori.adventure.platform.fabric.impl.accessor.ComponentSerializerAccess;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.arguments.ComponentArgument;
import org.jetbrains.annotations.NotNull;

/**
 * An argument that takes JSON-format text.
 *
 * <p>At the moment, using this argument type will require this mod
 * <strong>both server- and clientside</strong>, unless the
 * <a href="https://gitlab.com/stellardrift/colonel">Colonel</a> mod is present on the server.</p>
 *
 * @since 4.0.0
 */
public final class ComponentArgumentType implements ArgumentType<Component> {

  private static final ComponentArgumentType INSTANCE = new ComponentArgumentType();
  private static final Set<String> EXAMPLES = ImmutableSet.of(
    "\"Hello world!\"",
    "[\"Message\", {\"text\": \"example\", \"color\": \"#aabbcc\"}]"
  );

  /**
   * Get the argument type for component arguments.
   *
   * @return argument type instance
   * @since 4.0.0
   */
  public static @NotNull ComponentArgumentType component() {
    return INSTANCE;
  }

  /**
   * Get the component from the provided context.
   *
   * @param ctx Context to get from
   * @param key argument key
   * @return parsed component
   * @since 4.0.0
   */
  public static @NotNull Component component(final @NotNull CommandContext<?> ctx, final @NotNull String key) {
    return ctx.getArgument(key, Component.class);
  }

  private ComponentArgumentType() {
  }

  @Override
  public @NotNull Component parse(final @NotNull StringReader reader) throws CommandSyntaxException {
    try (final JsonReader json = new JsonReader(new java.io.StringReader(reader.getRemaining()))) {
      final Component ret = ComponentSerializerAccess.getGSON().fromJson(json, Component.class);
      reader.setCursor(reader.getCursor() + ComponentSerializerAccess.getPos(json));
      return ret;
    } catch (final JsonParseException | IOException ex) {
      final String message = ex.getCause() == null ? ex.getMessage() : ex.getCause().getMessage();
      throw ComponentArgument.ERROR_INVALID_JSON.createWithContext(reader, message);
    }
  }

  @Override
  public @NotNull Collection<String> getExamples() {
    return EXAMPLES;
  }

}
