package com.coloryr.allmusic.client.mixin;

import com.coloryr.allmusic.client.AllMusic;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class GuiShow {
    @Inject(method = {"renderMiscOverlays"}, at = {@At(value = "RETURN")})
    public void guiShow(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        AllMusic.update(context);
    }
}
