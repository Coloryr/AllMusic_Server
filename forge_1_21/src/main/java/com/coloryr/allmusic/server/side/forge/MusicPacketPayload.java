package com.coloryr.allmusic.server.side.forge;

import com.coloryr.allmusic.server.AllMusicForge;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record MusicPacketPayload(ByteBuf data) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, MusicPacketPayload> STREAM_CODEC = CustomPacketPayload.codec(MusicPacketPayload::write, MusicPacketPayload::new);
    public static final Type<MusicPacketPayload> TYPE = CustomPacketPayload.createType(AllMusicForge.channel);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private void write(FriendlyByteBuf data1) {
        data1.writeBytes(data);
    }
}
