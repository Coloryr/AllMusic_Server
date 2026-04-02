package com.coloryr.allmusic.buffercodec;

import com.coloryr.allmusic.codec.CommandType;
import com.coloryr.allmusic.codec.MusicPack;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class MusicPacketCodec {
    public static final CommandType[] types = CommandType.values();

    public static ByteBuf pack(CommandType type, String data, int data1) {
        ByteBuf buf = Unpooled.buffer(0);
        buf.writeByte(type.ordinal());
        switch (type) {
            case HUD_DATA:
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

    public static void pack(ByteBuf buf, CommandType type, String data, int data1) {
        buf.writeByte(type.ordinal());
        switch (type) {
            case HUD_DATA:
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

    private static String readString(ByteBuf buf) {
        int size = buf.readInt();
        byte[] temp = new byte[size];
        buf.readBytes(temp);

        return new String(temp, StandardCharsets.UTF_8);
    }

    public static MusicPack decode(ByteBuf buf) {
        byte type = buf.readByte();
        if (type >= types.length || type < 0) {
            return new MusicPack(CommandType.STOP, null, 0);
        }
        CommandType type1 = types[type];
        String data = null;
        int data1 = 0;
        switch (type1) {
            case LYRIC:
            case INFO:
            case LIST:
            case PLAY:
            case IMG:
            case HUD_DATA:
                data = readString(buf);
                break;
            case POS:
                data1 = buf.readInt();
                break;
        }
        buf.clear();

        return new MusicPack(type1, data, data1);
    }

    public static void pack(ByteBuf buf, MusicPack value) {
        pack(buf, value.type, value.data, value.data1);
    }
}
