package com.coloryr.allmusic.server.side.forge;

import com.coloryr.allmusic.server.AllMusicForge;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;

public class ForgeApi {
    public static void sendMessageRun(Object obj, String message, String end, String command) {
        ICommandSender sender = (ICommandSender) obj;
        ChatComponentText send = new ChatComponentText(message);
        ChatComponentText endtext = new ChatComponentText(end);
        endtext.setChatStyle(endtext.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
        send.appendSibling(endtext);
        sender.addChatMessage(send);
    }

    public static void sendMessageSuggest(Object obj, String message, String end, String command) {
        ICommandSender sender = (ICommandSender) obj;
        ChatComponentText send = new ChatComponentText(message);
        ChatComponentText endtext = new ChatComponentText(end);
        endtext.setChatStyle(endtext.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
        send.appendSibling(endtext);
        sender.addChatMessage(send);
    }

    public static void sendBar(EntityPlayerMP player, String message) {
//        SPacketTitle pack = new SPacketTitle(SPacketTitle.Type.ACTIONBAR, new TextComponentString(message));
//        player.connection.sendPacket(pack);
        ChatComponentText send = new ChatComponentText(message);
        player.addChatMessage(send);
    }

    public static void sendMessageBqRun(String message, String end, String command) {
        ChatComponentText send = new ChatComponentText(message);
        ChatComponentText endtext = new ChatComponentText(end);
        endtext.setChatStyle(endtext.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
        send.appendSibling(endtext);
        for (Object player : AllMusicForge.server.getConfigurationManager().playerEntityList) {
            EntityPlayerMP player1 = (EntityPlayerMP) player;
            player1.addChatMessage(send);
        }
    }
}
