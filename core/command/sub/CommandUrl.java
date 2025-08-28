package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.music.play.PlayMusic;
import com.coloryr.allmusic.server.core.objs.music.MusicObj;

public class CommandUrl extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (args.length != 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            return;
        }
        MusicObj obj = new MusicObj();
        obj.sender = sender;
        obj.isUrl = true;
        obj.url = args[1];
        PlayMusic.addTask(obj);
        AllMusic.side.sendMessage(sender, AllMusic.getMessage().addMusic.success);
    }
}
