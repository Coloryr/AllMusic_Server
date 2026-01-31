package com.coloryr.allmusic.server;

import com.coloryr.allmusic.buffercodec.MusicPacketCodec;
import com.coloryr.allmusic.codec.MusicPack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record MusicCodec(MusicPack pack) implements CustomPacketPayload {
    public static final Type<MusicCodec> TYPE = new Type<>(AllMusicServer.channel);
    public static final StreamCodec<RegistryFriendlyByteBuf, MusicCodec> CODEC = StreamCodec.of((buf, value) -> {
        MusicPacketCodec.pack(buf, value.pack);
    }, (buf -> {
        return new MusicCodec(MusicPacketCodec.decode(buf));
    }));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
