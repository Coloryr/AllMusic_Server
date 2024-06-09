package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.play.PlayMusic;

public class CommandNext extends ACommand {

    @Override
    public void ex(Object sender, String name, String[] args) {
        PlayMusic.musicLessTime = 1;
        AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2已强制切歌");
        AllMusic.getConfig().removeNoMusicPlayer(name);
    }
}
