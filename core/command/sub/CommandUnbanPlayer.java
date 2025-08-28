package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ICommand;
import com.coloryr.allmusic.server.core.sql.Cache;
import com.coloryr.allmusic.server.core.sql.DataSql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandUnbanPlayer implements ICommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (args.length != 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            return;
        }
        DataSql.removeBanPlayer(args[1]);
        AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2已解封玩家" + args[1] + "点歌");
    }

    @Override
    public List<String> tab(Object player, String name, String[] args, int index) {
        if (args.length == index || (args.length == index + 1 )) {
            return new ArrayList<>(Cache.banPlayers);
        }

        return Collections.emptyList();
    }
}
