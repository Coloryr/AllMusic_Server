package com.coloryr.allmusic.server.side.forge;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;

public class PacketMessage implements IMessage {
    private String data;

    public PacketMessage() {
    }

    public PacketMessage(String data) {
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        buf.writeBytes(bytes);
    }
}
