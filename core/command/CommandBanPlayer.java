package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.utils.Function;

import java.util.Locale;

public class CommandBanPlayer implements ICommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (args.length != 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
            return;
        }
        if (Function.isInteger(args[1])) {
            AllMusic.getConfig().addBanPlayer(args[1].toLowerCase(Locale.ROOT));
            AllMusic.side.sendMessage(sender, "§d[AllMusic]§2已禁止" + args[1]);
        } else {
            AllMusic.side.sendMessage(sender, "§d[AllMusic]§2请输入有效的ID");
        }
    }
}
