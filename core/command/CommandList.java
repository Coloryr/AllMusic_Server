package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.play.PlayMusic;

public class CommandList extends ACommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (PlayMusic.nowPlayMusic == null || PlayMusic.nowPlayMusic.isNull()) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.emptyPlayingMusic);
        } else {
            String info = AllMusic.getMessage().musicPlay.nowPlay;
            info = info.replace("%MusicName%", PlayMusic.nowPlayMusic.getName())
                    .replace("%MusicAuthor%", PlayMusic.nowPlayMusic.getAuthor())
                    .replace("%MusicAl%", PlayMusic.nowPlayMusic.getAl())
                    .replace("%MusicAlia%", PlayMusic.nowPlayMusic.getAlia())
                    .replace("%PlayerName%", PlayMusic.nowPlayMusic.getCall());
            AllMusic.side.sendMessage(sender, info);
        }
        if (PlayMusic.getListSize() == 0) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.emptyPlay);
        } else {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.listMusic.head
                    .replace("%Count%", "" + PlayMusic.getListSize()));
            AllMusic.side.sendMessage(sender, PlayMusic.getAllList());
        }
    }
}
