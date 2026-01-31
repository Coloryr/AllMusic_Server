package com.coloryr.allmusic.server;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;

public class ForgeApi {
    public static void sendMessageRun(ICommandSender obj, String message, String end, String command) {
        TextComponentString send = new TextComponentString(message);
        TextComponentString endtext = new TextComponentString(end);
        endtext.setStyle(endtext.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
        send.appendSibling(endtext);
        obj.sendMessage(send);
    }

    public static void sendMessageSuggest(ICommandSender obj, String message, String end, String command) {
        TextComponentString send = new TextComponentString(message);
        TextComponentString endtext = new TextComponentString(end);
        endtext.setStyle(endtext.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
        send.appendSibling(endtext);
        obj.sendMessage(send);
    }

    public static void sendBar(EntityPlayerMP player, String message) {
        SPacketTitle pack = new SPacketTitle(SPacketTitle.Type.ACTIONBAR, new TextComponentString(message));
        player.connection.sendPacket(pack);
    }

    public static void sendMessageBqRun(String message, String end, String command) {
        TextComponentString send = new TextComponentString(message);
        TextComponentString endtext = new TextComponentString(end);
        endtext.setStyle(endtext.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
        send.appendSibling(endtext);
        for (EntityPlayerMP player : AllMusicServer.server.getPlayerList().getPlayers()) {
            player.sendMessage(send);
        }
    }
}
