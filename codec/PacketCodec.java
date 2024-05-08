package com.coloryr.allmusic.server.codec;

import com.coloryr.allmusic.server.core.objs.enums.ComType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class PacketCodec {
    public static ByteBuf pack(ComType type, String data, int data1) {
        ByteBuf buf = Unpooled.buffer(0);
        buf.writeByte(type.ordinal());
        switch (type) {
            case HUD:
            case LYRIC:
            case LIST:
            case INFO:
            case IMG:
            case PLAY:
                writeString(buf, data);
                break;
            case POS:
                buf.writeInt(data1);
                break;
        }
        return buf;
    }

    public static void pack(ByteBuf buf, ComType type, String data, int data1) {
        buf.writeByte(type.ordinal());
        switch (type) {
            case HUD:
            case LYRIC:
            case LIST:
            case INFO:
            case IMG:
            case PLAY:
                writeString(buf, data);
                break;
            case POS:
                buf.writeInt(data1);
                break;
        }
    }

    private static void writeString(ByteBuf buf, String text) {
        byte[] temp = text.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(temp.length);
        buf.writeBytes(temp);
    }
}
