package Color_yr.AllMusic.side.sideBukkit;

import Color_yr.AllMusic.command.CommandEX;
import Color_yr.AllMusic.command.TabCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class CommandBukkit implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("music")) {
            CommandEX.ex(sender, sender.getName(), args);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("music")) {
            return TabCommand.getTabList(sender.getName(), args);
        }
        return null;
    }
}
