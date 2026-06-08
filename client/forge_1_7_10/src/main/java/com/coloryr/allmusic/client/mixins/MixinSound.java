package com.coloryr.allmusic.client.mixins;

import com.coloryr.allmusic.client.AllMusic;
import com.coloryr.allmusic.client.IGetSoundHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulscode.sound.Library;
import paulscode.sound.SoundSystem;

@Mixin(SoundSystem.class)
public class MixinSound implements IGetSoundHandler {
    @Shadow
    protected Library soundLibrary;

    public Library allMusic_Client$getSoundLibrary() {
        return soundLibrary;
    }

    @Inject(method = "<init>()V", at = @At("RETURN"))
    public void create(CallbackInfo ci) {
        if (AllMusic.sound == null) {
            AllMusic.sound = (SoundSystem) (Object) this;
        }
    }
}