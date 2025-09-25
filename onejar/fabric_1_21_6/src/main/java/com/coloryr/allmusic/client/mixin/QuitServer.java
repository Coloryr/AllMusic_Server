package com.coloryr.allmusic.client.mixin;

import com.coloryr.allmusic.client.core.AllMusicCore;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class QuitServer {
    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;reset()V"))
    public void quit(CallbackInfo info) {
        AllMusicCore.onServerQuit();
    }
}
