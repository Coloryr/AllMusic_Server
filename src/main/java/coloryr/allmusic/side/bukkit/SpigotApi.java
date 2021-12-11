package coloryr.allmusic.side.bukkit;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

public class SpigotApi {
    public SpigotApi() {
        new TextComponent();
    }

    public static void sendMessageRun(Object obj, String Message, String Command) {
        CommandSender sender = (CommandSender) obj;
        TextComponent send = new TextComponent(Message);
        send.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, Command));
        sender.spigot().sendMessage(send);
    }

    public static void sendMessageSuggest(Object obj, String Message, String Command) {
        CommandSender sender = (CommandSender) obj;
        TextComponent send = new TextComponent(Message);
        send.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, Command));
        sender.spigot().sendMessage(send);
    }
}
