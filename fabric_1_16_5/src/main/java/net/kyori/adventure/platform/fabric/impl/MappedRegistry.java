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

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Represents a mapping between an enum-like set of values controlled by Minecraft, and an enum-like set of values controlled by
 * Adventure.
 *
 * <p>Because these enums both refer to known constants, it is relatively easy to generate an automated
 * mapping between the two of them. As the two enums represent the same set of values, it is
 * required that any element that appears in one is present in the other, so mappings will never
 * return null.</p>
 *
 * @param <Mc> The Minecraft type
 * @param <Adv> The Adventure type
 */
public class MappedRegistry<Mc, Adv> {
  private final Map<Mc, Adv> mcToAdventure;
  private final Map<Adv, Mc> adventureToMc;

  static <Mc extends Enum<Mc>, Adv extends Enum<Adv>> MappedRegistry<Mc, Adv> named(final Class<Mc> mcType, final Function<String, @Nullable Mc> mcByName, final Class<Adv> advType, final Index<String, Adv> names) {
    return new OfEnum<>(mcType, mcByName, advType, names::key);
  }

  static <Mc, Adv> MappedRegistry<Mc, Adv> named(final Function<String, @Nullable Mc> mcByName, final Supplier<Iterable<Mc>> mcValues, final Index<String, Adv> names, final Supplier<Iterable<Adv>> adventureValues) {
    return new MappedRegistry<>(new HashMap<>(), new HashMap<>(), mcByName, mcValues, names::key, adventureValues);
  }

  MappedRegistry(final Map<Mc, Adv> mcMap, final Map<Adv, Mc> adventureMap, final Function<String, @Nullable Mc> mcByName, final Supplier<Iterable<Mc>> mcValues, final Function<Adv, @Nullable String> advToName, final Supplier<Iterable<Adv>> adventureValues) {
    this.mcToAdventure = mcMap;
    this.adventureToMc = adventureMap;

    for (final Adv advElement : adventureValues.get()) {
      final @Nullable String mcName = advToName.apply(advElement);
      if (mcName == null) {
        throw new ExceptionInInitializerError("Unable to get name for enum-like element " + advElement);
      }
      final @Nullable Mc mcElement = mcByName.apply(mcName);
      if (mcElement == null) {
        throw new ExceptionInInitializerError("Unknown MC element for Adventure " + mcName);
      }
      this.mcToAdventure.put(mcElement, advElement);
      this.adventureToMc.put(advElement, mcElement);
    }

    checkCoverage(this.mcToAdventure, mcValues.get());
  }

  /**
   * Validates that all members of an enum are present in the given map Throws {@link
   * IllegalStateException} if there is a missing value.
   *
   * @param toCheck The map to check
   * @param values The values to verify are keys of the provided map
   * @param <T> The type of enum
   * @throws IllegalStateException if a value is missing
   */
  private static <T> void checkCoverage(final Map<T, ?> toCheck, final Iterable<T> values) throws IllegalStateException {
    for (final T value : values) {
      if (!toCheck.containsKey(value)) {
        throw new IllegalStateException("Unmapped " + value.getClass().getSimpleName() + " element '" + value + '!');
      }
    }
  }

  /**
   * Given a Minecraft enum element, return the equivalent Adventure element.
   *
   * @param mcItem The Minecraft element
   * @return The adventure equivalent.
   */
  public Adv toAdventure(final Mc mcItem) {
    return requireNonNull(this.mcToAdventure.get(mcItem), "Invalid enum value presented: " + mcItem);
  }

  /**
   * Given an Adventure enum element, return the equivalent Minecraft element.
   *
   * @param advItem The Minecraft element
   * @return The adventure equivalent.
   */
  public Mc toMinecraft(final Adv advItem) {
    return this.adventureToMc.get(advItem);
  }

  static class OfEnum<Mc extends Enum<Mc>, Adv extends Enum<Adv>> extends MappedRegistry<Mc, Adv> {

    OfEnum(final Class<Mc> mcType, final Function<String, @Nullable Mc> mcByName, final Class<Adv> advType, final Function<Adv, @Nullable String> advToName) {
      super(new EnumMap<>(mcType), new EnumMap<>(advType), mcByName, () -> Arrays.asList(mcType.getEnumConstants()), advToName, () -> Arrays.asList(advType.getEnumConstants()));
    }
  }
}
