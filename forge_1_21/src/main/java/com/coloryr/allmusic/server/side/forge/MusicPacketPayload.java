package com.coloryr.allmusic.server.side.forge;

import com.coloryr.allmusic.server.AllMusicForge;
import com.coloryr.allmusic.server.codec.PacketCodec;
import com.coloryr.allmusic.server.core.objs.enums.ComType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record MusicPacketPayload(ComType cmd, String data, int data1) implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, MusicPacketPayload> STREAM_CODEC = CustomPacketPayload.codec(MusicPacketPayload::write, MusicPacketPayload::read);
    public static final Type<MusicPacketPayload> TYPE = CustomPacketPayload.createType(AllMusicForge.channel);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private static MusicPacketPayload read(RegistryFriendlyByteBuf data1) {
        return new MusicPacketPayload(ComType.CLEAR, null, 0);
    }

    private void write(RegistryFriendlyByteBuf pack) {
        PacketCodec.pack(pack, cmd, data, data1);
    }
}
