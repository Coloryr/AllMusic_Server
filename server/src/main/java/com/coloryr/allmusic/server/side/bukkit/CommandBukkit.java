package com.coloryr.allmusic.server.side.bukkit;

import com.coloryr.allmusic.server.core.command.CommandEX;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class CommandBukkit implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("music")) {
            CommandEX.execute(sender, sender.getName().toLowerCase(Locale.ROOT), args);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("music")) {
            return CommandEX.getTabList(sender.getName().toLowerCase(Locale.ROOT), args);
        }
        return null;
    }
}
