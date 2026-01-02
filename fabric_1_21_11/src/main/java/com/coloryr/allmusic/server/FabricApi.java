package com.coloryr.allmusic.server;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;

public class FabricApi {

    public static void sendMessageRun(CommandSourceStack obj, String message, String end, String command) {
        MutableComponent send = Component.literal(message);
        var endText = Component.literal(end);
        endText.setStyle(endText.getStyle().withClickEvent(new ClickEvent.RunCommand(command)));
        send.append(endText);
        obj.sendSystemMessage(send);
    }

    public static void sendMessageSuggest(CommandSourceStack obj, String message, String end, String command) {
        MutableComponent send = Component.literal(message);
        var endText = Component.literal(end);
        endText.setStyle(endText.getStyle().withClickEvent(new ClickEvent.SuggestCommand(command)));
        send.append(endText);
        obj.sendSystemMessage(send);
    }

    public static void sendBar(ServerPlayer player, String message) {
        var pack = new ClientboundSetActionBarTextPacket(Component.literal(message));
        player.connection.send(pack);
    }

    public static void sendMessageBqRun(String message, String end, String command) {
        MutableComponent send = Component.literal(message);
        MutableComponent endText = Component.literal(end);
        endText.setStyle(endText.getStyle().withClickEvent(new ClickEvent.SuggestCommand(command)));
        send.append(endText);
        for (var player : AllMusicFabric.server.getPlayerList().getPlayers()) {
            player.sendSystemMessage(send);
        }
    }
}
