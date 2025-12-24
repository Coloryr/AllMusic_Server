package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.codec.PacketCodec;
import com.coloryr.allmusic.server.core.objs.enums.ComType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record PackPayload(ComType type1, String data, int data1) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PackPayload> ID = new CustomPacketPayload.Type<>(AllMusicFabric.ID);
    public static final StreamCodec<FriendlyByteBuf, PackPayload> CODEC =
            StreamCodec.of((value, buf) -> PacketCodec.pack(value, buf.type1, buf.data, buf.data1),
                    buffer -> new PackPayload(ComType.CLEAR, null, 0));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
