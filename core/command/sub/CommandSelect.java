package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.command.CommandEX;
import com.coloryr.allmusic.server.core.command.PermissionList;
import com.coloryr.allmusic.server.core.objs.message.ARG;
import com.coloryr.allmusic.server.core.objs.music.SearchPageObj;
import com.coloryr.allmusic.server.core.sql.DataSql;
import com.coloryr.allmusic.server.core.utils.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandSelect extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (AllMusic.getConfig().needPermission &&
                !AllMusic.side.checkPermission(sender, PermissionList.PERMISSION_SEARCH)) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.noPer);
            return;
        }
        SearchPageObj obj = AllMusic.getSearch(name);
        if (obj == null) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.emptySearch);
        } else if (!args[1].isEmpty() && Function.isInteger(args[1])) {
            int a = Integer.parseInt(args[1]);
            if (a == 0) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.errorNum);
                return;
            }
            String id = obj.getSong((obj.getPage() * 10) + (a - 1));
            AllMusic.side.sendMessage(sender,
                    AllMusic.getMessage().search.choice.replace(ARG.index, "" + a));
            DataSql.task(() -> CommandEX.addMusic(sender, name, obj.getApi(), id));
            AllMusic.removeSearch(name);
        } else {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.errorNum);
        }
    }

    @Override
    public List<String> tab(Object player, String name, String[] args, int index) {
        if (args.length == 1 || (args.length == 2)) {
            List<String> list = new ArrayList<>();
            SearchPageObj obj = AllMusic.getSearch(name);
            if (obj != null) {
                for (int a = 0; a < obj.getIndex(); a++) {
                    list.add(String.valueOf(a + 1));
                }
            }
            return list;
        }
        return Collections.emptyList();
    }
}
