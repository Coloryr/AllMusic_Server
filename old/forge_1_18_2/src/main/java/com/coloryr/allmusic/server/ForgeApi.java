package com.coloryr.allmusic.server;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class ForgeApi {
    public static void sendMessageRun(CommandSourceStack obj, String message, String end, String command) {
        MutableComponent send = new TextComponent(message);
        MutableComponent endtext = new TextComponent(end);
        endtext.setStyle(endtext.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
        send.append(endtext);
        obj.sendSuccess(send, false);
    }

    public static void sendMessageSuggest(CommandSourceStack obj, String message, String end, String command) {
        MutableComponent send = new TextComponent(message);
        MutableComponent endtext = new TextComponent(end);
        endtext.setStyle(endtext.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
        send.append(endtext);
        obj.sendSuccess(send, false);
    }

    public static void sendBar(ServerPlayer player, String message) {
        var pack = new ClientboundSetActionBarTextPacket(new TextComponent(message));
        player.connection.send(pack);
    }

    public static void sendMessageBqRun(String message, String end, String command) {
        MutableComponent send = new TextComponent(message);
        MutableComponent endtext = new TextComponent(end);
        endtext.setStyle(endtext.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
        send.append(endtext);
        for (ServerPlayer player : AllMusicForge.server.getPlayerList().getPlayers()) {
            player.sendMessage(send, UUID.randomUUID());
        }
    }
}
