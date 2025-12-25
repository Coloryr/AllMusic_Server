package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.objs.enums.ComType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record PackPayload(ComType type, String data, int data1) implements CustomPayload {
    public static final Id<PackPayload> ID = new CustomPayload.Id<>(AllMusicFabric.ID);
    public static final PacketCodec<PacketByteBuf, PackPayload> CODEC =
            PacketCodec.of((value, buf) -> com.coloryr.allmusic.server.codec.PacketCodec.pack(buf, value.type, value.data, value.data1),
                    buffer -> new PackPayload(ComType.CLEAR, null, 0));

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
