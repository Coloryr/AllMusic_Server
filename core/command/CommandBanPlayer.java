package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CommandBanPlayer implements ICommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (args.length != 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            return;
        }
        AllMusic.getConfig().addBanPlayer(args[1].toLowerCase(Locale.ROOT));
        AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2已禁止玩家" + args[1] + "点歌");
    }

    @Override
    public List<String> tab(String name, String[] args) {
        if (args.length == 1 || (args.length == 2 && args[1].isEmpty())) {
            return AllMusic.side.getPlayerList();
        }

        return Collections.emptyList();
    }
}
