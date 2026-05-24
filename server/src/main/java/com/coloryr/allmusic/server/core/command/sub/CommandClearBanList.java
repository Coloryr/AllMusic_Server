package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.saves.BanSave;

public class CommandClearBanList extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        BanSave.clearBan();
        AllMusic.side.sendMessage(sender, "<light_purple>[AllMusic3]<dark_green>禁止点歌列表已清空");
    }
}
