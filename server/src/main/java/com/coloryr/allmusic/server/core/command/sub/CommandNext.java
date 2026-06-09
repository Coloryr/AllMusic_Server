package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.music.PlayMusic;
import com.coloryr.allmusic.server.core.saves.BanSave;

public class CommandNext extends ACommand {

    @Override
    public void execute(Object sender, String name, String[] args) {
        PlayMusic.musicLessTime = 10;
        AllMusic.side.sendMessage(sender, "<light_purple>[AllMusic]<dark_green>已强制切歌");
        BanSave.removeMutePlayer(name);
    }
}
