package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.saves.BanSave;

public class CommandClearBanPlayerList extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        BanSave.clearBanPlayer();
        AllMusic.side.sendMessage(sender, "<light_purple>[AllMusic3]<dark_green>禁止玩家点歌列表已清空");
    }
}
