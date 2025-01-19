package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.play.PlayMusic;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.utils.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandBan extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (args.length != 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            return;
        }
        if (Function.isInteger(args[1])) {
            AllMusic.getConfig().addBanMusic(args[1]);
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2已禁止点歌" + args[1]);
        } else {
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2请输入有效的ID");
        }
    }

    @Override
    public List<String> tab(String name, String[] args, int index) {
        if (args.length == index || (args.length == index + 1 && args[index].isEmpty())) {
            List<String> list = new ArrayList<>();
            if (PlayMusic.nowPlayMusic != null) {
                list.add(PlayMusic.nowPlayMusic.getID());
            }
            for (SongInfoObj item : PlayMusic.getList()) {
                list.add(item.getID());
            }

            return list;
        }

        return Collections.emptyList();
    }
}
