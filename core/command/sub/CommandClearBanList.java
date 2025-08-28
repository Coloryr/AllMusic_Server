package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.sql.DataSql;

public class CommandClearBanList extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        DataSql.clearBan();
        AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2禁止点歌列表已清空");
    }
}
