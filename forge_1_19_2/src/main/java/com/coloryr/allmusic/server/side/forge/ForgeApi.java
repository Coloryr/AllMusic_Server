package com.coloryr.allmusic.server.side.forge;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;

public class ForgeApi {
    public static void sendMessageRun(Object obj, String message, String end, String command) {
        CommandSource sender = (CommandSource) obj;
        MutableComponent send = Component.literal(message);
        MutableComponent endtext = Component.literal(end);
        endtext.setStyle(endtext.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
        send.append(endtext);
        sender.sendSystemMessage(send);
    }

    public static void sendMessageSuggest(Object obj, String message, String end, String command) {
        CommandSource sender = (CommandSource) obj;
        MutableComponent send = Component.literal(message);
        MutableComponent endtext = Component.literal(end);
        endtext.setStyle(endtext.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
        send.append(endtext);
        sender.sendSystemMessage(send);
    }

    public static void sendBar(ServerPlayer player, String message) {
        var pack = new ClientboundSetActionBarTextPacket(Component.literal(message));
        player.connection.send(pack);
    }
}
