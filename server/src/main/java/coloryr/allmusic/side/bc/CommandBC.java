package coloryr.allmusic.side.bc;

import coloryr.allmusic.core.command.CommandEX;
import coloryr.allmusic.core.command.TabCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class CommandBC extends Command implements TabExecutor {

    public CommandBC() {
        super("music");
    }

    public void execute(CommandSender sender, String[] args) {
        CommandEX.ex(sender, sender.getName(), args);
    }

    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return TabCommand.getTabList(sender.getName(), args);
    }
}
