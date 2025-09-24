package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.command.CommandEX;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.List;

public class CommandForge extends CommandBase {
    @Override
    public String getCommandName() {
        return "music";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "music help";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        CommandEX.execute(sender, sender.getCommandSenderName(), args);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return CommandEX.getTabList(sender, sender.getCommandSenderName(), args);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
