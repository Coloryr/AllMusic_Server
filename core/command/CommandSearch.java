package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;

public class CommandSearch extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (AllMusic.getConfig().needPermission &&
                !AllMusic.side.checkPermission(sender, "allmusic.search")) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.noPer);
            return;
        }
        if (CommandEX.checkMoney(sender, name, AllMusic.getConfig().cost.searchCost)) {
            return;
        }
        if (CommandEX.cost(sender, name, AllMusic.getConfig().cost.searchCost,
                AllMusic.getMessage().cost.search)) {
            return;
        }

        AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.startSearch);
        CommandEX.searchMusic(sender, name, args, false);
    }
}
