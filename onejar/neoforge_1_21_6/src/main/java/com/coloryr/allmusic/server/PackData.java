package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.codec.PacketCodec;
import com.coloryr.allmusic.server.core.objs.enums.ComType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record PackData(ComType cmd, String data, int data1) implements CustomPacketPayload {
    public static final Type<PackData> TYPE = new Type<>(AllMusicNeoForge.channel);
    public static final StreamCodec<RegistryFriendlyByteBuf, PackData> CODEC = new PackCodec();

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class PackCodec implements StreamCodec<RegistryFriendlyByteBuf, PackData> {
        @Override
        public @NotNull PackData decode(RegistryFriendlyByteBuf pack) {
            pack.clear();
            return new PackData(ComType.CLEAR, "", 0);
        }

        @Override
        public void encode(@NotNull RegistryFriendlyByteBuf pack, PackData buffer) {
            PacketCodec.pack(pack, buffer.cmd, buffer.data, buffer.data1);
        }
    }
}
