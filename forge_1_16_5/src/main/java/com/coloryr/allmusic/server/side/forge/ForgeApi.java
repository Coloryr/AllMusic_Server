package com.coloryr.allmusic.server.side.forge;

import com.coloryr.allmusic.server.AllMusicForge;
import com.coloryr.allmusic.server.core.AllMusic;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.event.ClickEvent;

import java.util.UUID;

public class ForgeApi {
    public static void sendMessageRun(Object obj, String message, String end, String command) {
        CommandSource sender = (CommandSource) obj;
        StringTextComponent send = new StringTextComponent(message);
        StringTextComponent endtext = new StringTextComponent(end);
        endtext.setStyle(endtext.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
        send.append(endtext);
        sender.sendSuccess(send, false);
    }

    public static void sendMessageSuggest(Object obj, String message, String end, String command) {
        CommandSource sender = (CommandSource) obj;
        StringTextComponent send = new StringTextComponent(message);
        StringTextComponent endtext = new StringTextComponent(end);
        endtext.setStyle(endtext.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
        send.append(endtext);
        sender.sendSuccess(send, false);
    }

    public static void sendBar(ServerPlayerEntity player, String message) {
        STitlePacket pack = new STitlePacket(STitlePacket.Type.ACTIONBAR, new StringTextComponent(message));
        player.connection.send(pack);
    }

    public static void sendMessageBqRun(String message, String end, String command) {
        StringTextComponent send = new StringTextComponent(message);
        StringTextComponent endtext = new StringTextComponent(end);
        endtext.setStyle(endtext.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
        send.append(endtext);
        for (ServerPlayerEntity player : AllMusicForge.server.getPlayerList().getPlayers()) {
            player.sendMessage(send, UUID.randomUUID());
        }
    }
}
