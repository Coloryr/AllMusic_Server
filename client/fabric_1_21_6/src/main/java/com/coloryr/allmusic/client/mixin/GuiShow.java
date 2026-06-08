package com.coloryr.allmusic.client.mixin;

import com.coloryr.allmusic.client.AllMusicClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiShow {
    @Inject(method = {"renderChat"}, at = {@At(value = "RETURN")})
    public void guiShow(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
        AllMusicClient.update(context);
    }
}
