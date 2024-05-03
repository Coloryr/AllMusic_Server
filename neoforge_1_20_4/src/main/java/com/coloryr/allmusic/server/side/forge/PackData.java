package com.coloryr.allmusic.server.side.forge;

import com.coloryr.allmusic.server.AllMusicForge;
import com.coloryr.allmusic.server.core.objs.enums.ComType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public record PackData(ComType cmd, String data, int data1) implements CustomPacketPayload {
    @Override
    public void write(@NotNull FriendlyByteBuf pack) {
        pack.writeByte(cmd.ordinal());
        switch (cmd)
        {
            case IMG:
            case PLAY:
            case INFO:
            case LIST:
            case LYRIC:
            case HUD:
                writeString(pack, data);
                break;
            case POS:
                pack.writeInt(data1);
                break;
        }
    }

    private void writeString(ByteBuf buf, String data) {
        byte[] temp = data.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(temp.length)
                .writeBytes(temp);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return AllMusicForge.channel;
    }
}
