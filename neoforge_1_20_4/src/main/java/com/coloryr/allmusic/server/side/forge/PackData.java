package com.coloryr.allmusic.server.side.forge;

import com.coloryr.allmusic.server.AllMusicForge;
import com.coloryr.allmusic.server.codec.PacketCodec;
import com.coloryr.allmusic.server.core.objs.enums.ComType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record PackData(ComType cmd, String data, int data1) implements CustomPacketPayload {
    @Override
    public void write(@NotNull FriendlyByteBuf pack) {
        PacketCodec.pack(pack, cmd, data, data1);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return AllMusicForge.channel;
    }
}
