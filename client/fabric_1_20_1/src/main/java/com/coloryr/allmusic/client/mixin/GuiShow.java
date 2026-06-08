package com.coloryr.allmusic.client.mixin;

import com.coloryr.allmusic.client.AllMusicClient;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiShow {
    @Inject(method = {"render"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderCrosshair(Lnet/minecraft/client/gui/GuiGraphics;)V")})
    public void Gui(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        AllMusicClient.update(guiGraphics);
    }
}
