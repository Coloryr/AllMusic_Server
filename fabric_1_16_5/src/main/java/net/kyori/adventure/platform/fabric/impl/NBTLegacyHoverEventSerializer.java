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

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.io.IOException;
import java.util.UUID;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.util.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import org.jetbrains.annotations.NotNull;

public final class NBTLegacyHoverEventSerializer implements LegacyHoverEventSerializer {
  public static final NBTLegacyHoverEventSerializer INSTANCE = new NBTLegacyHoverEventSerializer();
  private static final Codec<CompoundTag, String, CommandSyntaxException, RuntimeException> SNBT_CODEC = Codec.of(TagParser::parseTag, Tag::toString);

  static final String ITEM_TYPE = "id";
  static final String ITEM_COUNT = "Count";
  static final String ITEM_TAG = "tag";

  static final String ENTITY_NAME = "name";
  static final String ENTITY_TYPE = "type";
  static final String ENTITY_ID = "id";

  private NBTLegacyHoverEventSerializer() {
  }

  @Override
  public HoverEvent.@NotNull ShowItem deserializeShowItem(final @NotNull Component input) throws IOException {
    final String raw = PlainTextComponentSerializer.plainText().serialize(input);
    try {
      final CompoundTag contents = SNBT_CODEC.decode(raw);
      final CompoundTag tag = contents.getCompound(ITEM_TAG);
      return HoverEvent.ShowItem.of(
        Key.key(contents.getString(ITEM_TYPE)),
        contents.contains(ITEM_COUNT) ? contents.getByte(ITEM_COUNT) : 1,
        tag.isEmpty() ? null : BinaryTagHolder.encode(tag, SNBT_CODEC)
      );
    } catch (final CommandSyntaxException ex) {
      throw new IOException(ex);
    }
  }

  @Override
  public HoverEvent.@NotNull ShowEntity deserializeShowEntity(final @NotNull Component input, final Codec.Decoder<Component, String, ? extends RuntimeException> componentCodec) throws IOException {
    final String raw = PlainTextComponentSerializer.plainText().serialize(input);
    try {
      final CompoundTag contents = SNBT_CODEC.decode(raw);
      return HoverEvent.ShowEntity.of(
        Key.key(contents.getString(ENTITY_TYPE)),
        UUID.fromString(contents.getString(ENTITY_ID)),
        componentCodec.decode(contents.getString(ENTITY_NAME))
      );
    } catch (final CommandSyntaxException ex) {
      throw new IOException(ex);
    }
  }

  @Override
  public @NotNull Component serializeShowItem(final HoverEvent.@NotNull ShowItem input) throws IOException {
    final CompoundTag tag = new CompoundTag();
    tag.putString(ITEM_TYPE, input.item().asString());
    tag.putByte(ITEM_COUNT, (byte) input.count());
    if (input.nbt() != null) {
      try {
        tag.put(ITEM_TAG, input.nbt().get(SNBT_CODEC));
      } catch (final CommandSyntaxException ex) {
        throw new IOException(ex);
      }
    }

    return Component.text(SNBT_CODEC.encode(tag));
  }

  @Override
  public @NotNull Component serializeShowEntity(final HoverEvent.@NotNull ShowEntity input, final Codec.Encoder<Component, String, ? extends RuntimeException> componentCodec) throws IOException {
    final CompoundTag tag = new CompoundTag();
    tag.putString(ENTITY_ID, input.id().toString());
    tag.putString(ENTITY_TYPE, input.type().asString());
    if (input.name() != null) {
      tag.putString(ENTITY_NAME, componentCodec.encode(input.name()));
    }
    return Component.text(SNBT_CODEC.encode(tag));
  }
}
