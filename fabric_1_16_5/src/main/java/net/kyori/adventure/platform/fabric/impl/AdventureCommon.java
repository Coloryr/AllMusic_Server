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

import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.platform.fabric.ComponentArgumentType;
import net.kyori.adventure.platform.fabric.KeyArgumentType;
import net.kyori.adventure.platform.fabric.PlayerLocales;
import net.kyori.adventure.platform.fabric.impl.server.FabricServerAudiencesImpl;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.translation.Translator;
import net.minecraft.client.KeyMapping;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdventureCommon implements ModInitializer {

  public static final ComponentFlattener FLATTENER;
  private static final Pattern LOCALIZATION_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?s");

  static {
    final ComponentFlattener.Builder flattenerBuilder = ComponentFlattener.basic().toBuilder();

    if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
      flattenerBuilder.mapper(KeybindComponent.class, keybind -> KeyMapping.createNameSupplier(keybind.keybind()).get().getContents());
    }

    flattenerBuilder.complexMapper(TranslatableComponent.class, (translatable, consumer) -> {
      final String key = translatable.key();
      for (final Translator registry : GlobalTranslator.translator().sources()) {
        if (registry instanceof TranslationRegistry && ((TranslationRegistry) registry).contains(key)) {
          consumer.accept(GlobalTranslator.render(translatable, Locale.getDefault()));
          return;
        }
      }

      final @NotNull String translated = Language.getInstance().getOrDefault(key);
      final Matcher matcher = LOCALIZATION_PATTERN.matcher(translated);
      final List<Component> args = translatable.args();
      int argPosition = 0;
      int lastIdx = 0;
      while (matcher.find()) {
        // append prior
        if (lastIdx < matcher.start()) consumer.accept(Component.text(translated.substring(lastIdx, matcher.start())));
        lastIdx = matcher.end();

        final @Nullable String argIdx = matcher.group(1);
        // calculate argument position
        if (argIdx != null) {
          try {
            final int idx = Integer.parseInt(argIdx) - 1;
            if (idx < args.size()) {
              consumer.accept(args.get(idx));
            }
          } catch (final NumberFormatException ex) {
            // ignore, drop the format placeholder
          }
        } else {
          final int idx = argPosition++;
          if (idx < args.size()) {
            consumer.accept(args.get(idx));
          }
        }
      }

      // append tail
      if (lastIdx < translated.length()) {
        consumer.accept(Component.text(translated.substring(lastIdx)));
      }
    });

    FLATTENER = flattenerBuilder.build();
  }

  static ResourceLocation res(final @NotNull String value) {
    return new ResourceLocation("adventure", value);
  }

  @Override
  public void onInitialize() {
    // Register custom argument types
    ArgumentTypes.register("adventure:component", ComponentArgumentType.class, new ComponentArgumentTypeSerializer());
    ArgumentTypes.register("adventure:key", KeyArgumentType.class, new EmptyArgumentSerializer<>(KeyArgumentType::key));

    PlayerLocales.CHANGED_EVENT.register((player, locale) -> {
      FabricServerAudiencesImpl.forEachInstance(instance -> {
        instance.bossBars().refreshTitles(player);
      });
    });
  }

  public static Function<Pointered, Locale> localePartition() {
    return ptr -> ptr.getOrDefault(Identity.LOCALE, Locale.US);
  }

  public static Pointered pointered(final FPointered pointers) {
    return pointers;
  }

  @FunctionalInterface
  interface FPointered extends Pointered {
    @Override
    @NotNull Pointers pointers();
  }
}
