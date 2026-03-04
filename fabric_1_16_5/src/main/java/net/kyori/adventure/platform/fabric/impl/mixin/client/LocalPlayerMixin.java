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
package net.kyori.adventure.platform.fabric.impl.mixin.client;

import java.util.Locale;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.platform.fabric.FabricClientAudiences;
import net.kyori.adventure.platform.fabric.impl.LocaleHolderBridge;
import net.kyori.adventure.platform.fabric.impl.mixin.PlayerMixin;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.sound.Sound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends PlayerMixin implements ForwardingAudience.Single, LocaleHolderBridge {
  // @formatter:off
  @Shadow @Final protected Minecraft minecraft;
  // @formatter:on

  // TODO: Do we want to enforce synchronization with the client thread?
  private LocalPlayerMixin(final EntityType<? extends LivingEntity> type, final Level level) { // mixin will strip
    super(type, level);
  }

  private final Audience adventure$default = FabricClientAudiences.of().audience();

  @Override
  public @NotNull Audience audience() {
    return this.adventure$default;
  }

  @Override
  public @NotNull Pointers pointers() {
    return this.audience().pointers();
  }

  @Override
  public void playSound(final @NotNull Sound sound) {
    this.audience().playSound(sound, this.getX(), this.getY(), this.getZ());
  }

  @Override
  public @NotNull Locale adventure$locale() {
    return ((LocaleHolderBridge) this.minecraft.options).adventure$locale();
  }

  @Override
  protected void adventure$populateExtraPointers(final Pointers.Builder builder) {
    builder.withDynamic(Identity.LOCALE, this::adventure$locale);
  }
}
