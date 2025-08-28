package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.music.play.PlayMusic;
import com.coloryr.allmusic.server.core.sql.DataSql;

public class CommandNext extends ACommand {

    @Override
    public void execute(Object sender, String name, String[] args) {
        PlayMusic.musicLessTime = 1;
        AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2已强制切歌");
        DataSql.removeMutePlayer(name);
    }
}
