package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.IMusicApi;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.sql.Cache;
import com.coloryr.allmusic.server.core.sql.DataSql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandUnban extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (args.length < 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            return;
        }

        String musicID = null;
        IMusicApi api = null;

        if (args.length == 2) {
            api = AllMusic.MUSIC_APIS.get(AllMusic.getConfig().defaultApi);
            musicID = args[1];
        } else if (args.length == 3) {
            api = AllMusic.MUSIC_APIS.get(args[1]);
            musicID = args[2];
        } else {
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2错误的指令");
        }

        if (api == null) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.error2);
            return;
        }

        musicID = api.getMusicId(musicID);

        if (api.checkId(musicID)) {
            api.setList(musicID, sender);
            DataSql.removeBanMusic(args[1]);
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2音乐API " + api.getId() + "已解封点歌" + musicID);
        } else {
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2请输入有效的ID");
        }
    }

    @Override
    public List<String> tab(Object player, String name, String[] args, int index) {
        if (args.length == index || (args.length == index + 1)) {
            return new ArrayList<>(Cache.banMusic);
        }

        return Collections.emptyList();
    }
}
