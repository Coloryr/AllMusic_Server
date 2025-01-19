package com.coloryr.allmusic.server.side.velocity;

import com.coloryr.allmusic.server.core.command.CommandEX;
import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import java.util.List;

public class CommandVelocity implements SimpleCommand {
    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        String name = "CONSOLE";
        if (invocation.source() instanceof Player) {
            Player player = (Player) invocation.source();
            name = player.getUsername();
        }
        CommandEX.execute(source, name, args);
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return true;
    }

    @Override
    public List<String> suggest(final Invocation invocation) {
        String[] args = invocation.arguments();
        if (invocation.source() instanceof Player) {
            Player player = (Player) invocation.source();
            String name = player.getUsername();
            return CommandEX.getTabList(name, args);
        }
        return ImmutableList.of();
    }
}
