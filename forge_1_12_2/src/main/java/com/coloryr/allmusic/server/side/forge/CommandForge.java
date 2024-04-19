package com.coloryr.allmusic.server.side.forge;

import com.coloryr.allmusic.server.core.command.CommandEX;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandForge extends CommandBase {
    @Override
    public String getName() {
        return "music";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "music help";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        CommandEX.ex(sender, sender.getName(), args);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return CommandEX.getTabList(sender.getName(), args);
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
