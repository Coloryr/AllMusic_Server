package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.sql.DataSql;
import com.coloryr.allmusic.server.core.utils.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandUnban extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (args.length != 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            return;
        }
        if (Function.isInteger(args[1])) {
            DataSql.removeBanMusic(args[1]);
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2已解封点歌" + args[1]);
        } else {
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2请输入有效的ID");
        }
    }

    @Override
    public List<String> tab(Object player, String name, String[] args, int index) {
        if (args.length == index || (args.length == index + 1 )) {
            return new ArrayList<>(DataSql.Cache.banMusic);
        }

        return Collections.emptyList();
    }
}
