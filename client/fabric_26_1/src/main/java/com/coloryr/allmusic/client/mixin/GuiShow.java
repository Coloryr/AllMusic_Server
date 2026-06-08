package com.coloryr.allmusic.client.mixin;

import com.coloryr.allmusic.client.AllMusicClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiShow {
    @Inject(method = "extractCameraOverlays", at = @At(value = "HEAD"))
    public void guiShow(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        AllMusicClient.update(graphics);
    }
}
