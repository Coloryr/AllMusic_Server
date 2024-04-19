package com.coloryr.allmusic.server.side.forge;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;

public class PacketMessage implements IMessage {
    private ByteBuf data;

    public PacketMessage() {
    }

    public PacketMessage(ByteBuf data) {
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBytes(data);
    }
}
