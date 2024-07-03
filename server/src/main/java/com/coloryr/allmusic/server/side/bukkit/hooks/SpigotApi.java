package com.coloryr.allmusic.server.side.bukkit.hooks;

import com.coloryr.allmusic.server.core.AllMusic;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpigotApi {
    public static void sendMessageRun(Object obj, String message, String end, String command) {
        CommandSender sender = (CommandSender) obj;
        TextComponent send = new TextComponent(message);
        TextComponent endtext = new TextComponent(end);
        endtext.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        send.addExtra(endtext);
        sender.spigot().sendMessage(send);
    }

    public static void sendMessageSuggest(Object obj, String message, String end, String command) {
        CommandSender sender = (CommandSender) obj;
        TextComponent send = new TextComponent(message);
        TextComponent endtext = new TextComponent(end);
        endtext.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        send.addExtra(endtext);
        sender.spigot().sendMessage(send);
    }

    public static void sendBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    public static void sendMessageBqRun(String message, String end, String command) {
        TextComponent send = new TextComponent(message);
        TextComponent endtext = new TextComponent(end);
        endtext.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        send.addExtra(endtext);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!AllMusic.isSkip(player.getName(), null, false)) {
                player.spigot().sendMessage(send);
            }
        }
    }
}
