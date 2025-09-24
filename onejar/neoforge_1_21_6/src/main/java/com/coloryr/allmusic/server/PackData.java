package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.codec.PacketCodec;
import com.coloryr.allmusic.server.core.objs.enums.ComType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public record PackData(ComType cmd, String data, int data1) implements CustomPacketPayload {
    public static final ComType[] types = ComType.values();

    public static final Type<PackData> TYPE = new Type<>(AllMusicForge.channel);
    public static final StreamCodec<RegistryFriendlyByteBuf, PackData> CODEC = new PackCodec();

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class PackCodec implements StreamCodec<RegistryFriendlyByteBuf, PackData> {
        @Override
        public @NotNull PackData decode(RegistryFriendlyByteBuf pack) {
            byte type = pack.readByte();
            if (type >= types.length || type < 0) {
                return new PackData(ComType.CLEAR, "", 0);
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
                    data = readString(pack);
                    break;
                case POS:
                    data1 = pack.readInt();
                    break;
            }
            pack.clear();
            return new PackData(type1, data, data1);
        }

        @Override
        public void encode(@NotNull RegistryFriendlyByteBuf pack, PackData buffer) {
            PacketCodec.pack(pack, buffer.cmd, buffer.data, buffer.data1);
        }

        private static String readString(ByteBuf buf) {
            int size = buf.readInt();
            byte[] temp = new byte[size];
            buf.readBytes(temp);

            return new String(temp, StandardCharsets.UTF_8);
        }
    }
}
