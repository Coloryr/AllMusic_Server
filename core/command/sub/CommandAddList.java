package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.utils.Function;

public class CommandAddList extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (args.length != 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            return;
        }

        String musicID;

        if (args[1].contains("id=") && !args[1].contains("/?userid")) {
            if (args[1].contains("&user"))
                musicID = Function.getString(args[1], "id=", "&user");
            else
                musicID = Function.getString(args[1], "id=", null);
        } else
            musicID = args[1];

        if (Function.isInteger(musicID)) {
            AllMusic.getMusicApi().setList(musicID, sender);
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2添加空闲音乐列表" + musicID);
        } else {
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§2请输入有效的音乐列表ID");
        }
    }
}
