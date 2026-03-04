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

import com.mojang.authlib.GameProfile;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.platform.fabric.impl.NonWrappingComponentSerializer;
import net.kyori.adventure.platform.fabric.impl.PointerProviderBridge;
import net.kyori.adventure.pointer.Pointers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements Identified, PointerProviderBridge {
  // @formatter:off
  @Shadow @Final private GameProfile gameProfile;

  @Shadow public abstract GameProfile shadow$getGameProfile();
  // @formatter:on

  private Pointers adventure$pointers;

  protected PlayerMixin(final EntityType<? extends LivingEntity> entityType, final Level level) {
    super(entityType, level);
  }

  @Override
  public @NotNull Identity identity() {
    return (Identity) this.gameProfile;
  }

  @Override
  public @NotNull Pointers adventure$pointers() {
    Pointers pointers = this.adventure$pointers;
    if (pointers == null) {
      synchronized (this) {
        if (this.adventure$pointers != null) {
          return this.adventure$pointers;
        }

        final Pointers.Builder builder = Pointers.builder()
          .withDynamic(Identity.NAME, () -> this.shadow$getGameProfile().getName())
          .withDynamic(Identity.UUID, this::getUUID)
          .withDynamic(Identity.DISPLAY_NAME, () -> NonWrappingComponentSerializer.INSTANCE.deserialize(this.getDisplayName()));

        // add any extra data
        this.adventure$populateExtraPointers(builder);

        this.adventure$pointers = pointers = builder.build();
      }
    }

    return pointers;
  }

  protected void adventure$populateExtraPointers(final Pointers.Builder builder) {
    // for overriding by implementations
    // todo: support permissions here if Luck's permissions API is available
  }
}
