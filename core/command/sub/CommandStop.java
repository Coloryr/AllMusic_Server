package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.command.CommandEX;
import com.coloryr.allmusic.server.core.utils.HudUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandStop extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (args.length == 2 && CommandEX.checkAdmin(sender, name)) {
            name = args[1];
            AllMusic.side.sendMessage(sender, "已停止玩家：" + name + "的音乐播放");
        } else {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.stopPlaying);
        }
        AllMusic.side.sendStop(name);
        HudUtils.clearHud(name);
    }

    @Override
    public List<String> tab(Object player, String name, String[] args, int index) {
        if (args.length == index || (args.length == index + 1) && CommandEX.checkAdmin(player, name)) {
            return new ArrayList<>(AllMusic.getNowPlayPlayer());
        } else {
            return Collections.emptyList();
        }
    }
}
