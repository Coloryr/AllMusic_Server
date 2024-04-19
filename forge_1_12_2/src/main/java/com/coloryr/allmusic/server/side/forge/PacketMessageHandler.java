package com.coloryr.allmusic.server.side.forge;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketMessageHandler implements IMessageHandler<PacketMessage, IMessage> {

    @Override
    public IMessage onMessage(PacketMessage message, MessageContext ctx) {
        return null;
    }
}