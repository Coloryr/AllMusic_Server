package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.command.CommandEX;
import com.coloryr.allmusic.server.core.command.PermissionList;

public class CommandSearchApi extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (AllMusic.getConfig().needPermission &&
                !AllMusic.side.checkPermission(sender, PermissionList.PERMISSION_SEARCH)) {
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

        if (args.length < 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.error2);
            return;
        }

        AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.startSearch);
        CommandEX.searchMusicApi(sender, name, args, false);
    }
}
