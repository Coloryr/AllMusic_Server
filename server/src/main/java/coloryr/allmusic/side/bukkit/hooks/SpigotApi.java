package coloryr.allmusic.side.bukkit.hooks;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpigotApi {
    public static void sendMessageRun(Object obj, String message, String command) {
        CommandSender sender = (CommandSender) obj;
        TextComponent send = new TextComponent(message);
        send.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        sender.spigot().sendMessage(send);
    }

    public static void sendMessageSuggest(Object obj, String message, String command) {
        CommandSender sender = (CommandSender) obj;
        TextComponent send = new TextComponent(message);
        send.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        sender.spigot().sendMessage(send);
    }

    public static void sendBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}
