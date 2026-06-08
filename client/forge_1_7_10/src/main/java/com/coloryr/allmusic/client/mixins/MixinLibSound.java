package com.coloryr.allmusic.client.mixins;

import com.coloryr.allmusic.client.IGetSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import paulscode.sound.Channel;
import paulscode.sound.Library;

import java.util.List;

@Mixin(Library.class)
public class MixinLibSound implements IGetSound {
    @Shadow
    protected List<Channel> streamingChannels;

    @Shadow
    protected List<Channel> normalChannels;

    public List<Channel> allMusic_Client$getNormalChannels() {
        return normalChannels;
    }

    public List<Channel> allMusic_Client$getStreamingChannels() {
        return streamingChannels;
    }
}
