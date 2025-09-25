package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.objs.enums.ComType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

import java.nio.charset.StandardCharsets;

public record PackPayload(ComType type, String data, int data1) implements CustomPayload {
    public static final ComType[] types = ComType.values();
    public static final Id<PackPayload> ID = new CustomPayload.Id<>(AllMusicFabric.ID);
    public static final PacketCodec<PacketByteBuf, PackPayload> CODEC =
            PacketCodec.of((value, buf) -> com.coloryr.allmusic.server.codec.PacketCodec.pack(buf, value.type, value.data, value.data1),
                    buffer -> {
                        byte type = buffer.readByte();
                        if (type >= types.length || type < 0) {
                            return null;
                        }
                        ComType type1 = types[type];
                        String data = null;
                        int data1 = 0;
                        switch (type1) {
                            case LYRIC:
                            case INFO:
                            case LIST:
                            case PLAY:
                            case IMG:
                            case HUD:
                                data = readString(buffer);
                                break;
                            case POS:
                                data1 = buffer.readInt();
                                break;
                        }
                        return new PackPayload(type1, data, data1);
                    });

    private static String readString(ByteBuf buf) {
        int size = buf.readInt();
        byte[] temp = new byte[size];
        buf.readBytes(temp);

        return new String(temp, StandardCharsets.UTF_8);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
