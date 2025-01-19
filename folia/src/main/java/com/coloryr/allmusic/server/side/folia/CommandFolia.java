package com.coloryr.allmusic.server.side.folia;

import com.coloryr.allmusic.server.core.command.CommandEX;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class CommandFolia implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("music")) {
            CommandEX.execute(sender, sender.getName(), args);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("music")) {
            return CommandEX.getTabList(sender.getName(), args);
        }
        return null;
    }
}
