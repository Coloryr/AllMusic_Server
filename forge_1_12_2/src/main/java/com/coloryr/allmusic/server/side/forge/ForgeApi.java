package com.coloryr.allmusic.server.side.forge;

import com.coloryr.allmusic.server.AllMusicForge;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;

public class ForgeApi {
    public static void sendMessageRun(Object obj, String message, String end, String command) {
        ICommandSender sender = (ICommandSender) obj;
        TextComponentString send = new TextComponentString(message);
        TextComponentString endtext = new TextComponentString(end);
        endtext.setStyle(endtext.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
        send.appendSibling(endtext);
        sender.sendMessage(send);
    }

    public static void sendMessageSuggest(Object obj, String message, String end, String command) {
        ICommandSender sender = (ICommandSender) obj;
        TextComponentString send = new TextComponentString(message);
        TextComponentString endtext = new TextComponentString(end);
        endtext.setStyle(endtext.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
        send.appendSibling(endtext);
        sender.sendMessage(send);
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
        for (EntityPlayerMP player : AllMusicForge.server.getPlayerList().getPlayers()) {
            player.sendMessage(send);
        }
    }
}
