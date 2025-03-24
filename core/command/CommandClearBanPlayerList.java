package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.play.PlayMusic;
import com.coloryr.allmusic.server.core.sql.DataSql;

public class CommandClearBanPlayerList extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        DataSql.clearBanPlayer();
        AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2禁止玩家点歌列表已清空");
    }
}
