package com.coloryr.allmusic.server.side.forge;

import com.coloryr.allmusic.server.AllMusicForge;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record PackData(ByteBuf buffer) implements CustomPacketPayload {
    public static final Type<PackData> TYPE = new Type<>(AllMusicForge.channel);
    public static final StreamCodec<RegistryFriendlyByteBuf, PackData> CODEC = new PackCodec();

    public static class PackCodec implements StreamCodec<RegistryFriendlyByteBuf, PackData> {
        @Override
        public PackData decode(RegistryFriendlyByteBuf pack) {
            return new PackData(pack.asByteBuf());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf pack, PackData buffer) {
            pack.writeBytes(buffer.buffer);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
