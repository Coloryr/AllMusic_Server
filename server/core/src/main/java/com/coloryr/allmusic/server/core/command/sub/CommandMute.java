package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.command.CommandEX;
import com.coloryr.allmusic.server.core.music.PlayMusic;
import com.coloryr.allmusic.server.core.saves.BanSave;
import com.coloryr.allmusic.server.core.saves.HudSave;
import com.coloryr.allmusic.server.core.saves.SaveTask;

import java.util.ArrayList;
import java.util.List;

public class CommandMute extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("list")) {
                if (BanSave.checkMuteListPlayer(name)) {
                    BanSave.removeMuteListPlayer(name);
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.mute2);
                } else {
                    BanSave.addMuteListPlayer(name);
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.mute1);
                    if (PlayMusic.nowPlayMusic != null && PlayMusic.nowPlayMusic.isList()) {
                        AllMusic.side.sendStop(name);
                        AllMusic.side.clearHud(name);
                    }
                }
            } else if (CommandEX.checkAdmin(sender, name)) {
                String finalName = args[1];
                if (BanSave.checkMutePlayer(finalName)) {
                    BanSave.removeMutePlayer(finalName);
                    AllMusic.side.sendMessage(sender, "已取消玩家：" + finalName + "的静音");
                } else {
                    BanSave.addMutePlayer(finalName);
                    AllMusic.side.sendStop(finalName);
                    AllMusic.side.clearHud(finalName);
                    AllMusic.side.sendMessage(sender, "已设置玩家：" + finalName + "的静音");
                }
            }
        } else {
            if (BanSave.checkMutePlayer(name)) {
                BanSave.removeMutePlayer(name);
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.mute3);
            } else {
                BanSave.addMutePlayer(name);
                AllMusic.side.sendStop(name);
                AllMusic.side.clearHud(name);
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.mute);
            }
        }
    }

    @Override
    public List<String> tab(Object player, String name, String[] args, int index) {
        if (args.length == index || (args.length == index + 1)) {
            if (CommandEX.checkAdmin(player, name)) {
                List<String> players = new ArrayList<>();
                for (Object item : AllMusic.side.getPlayers()) {
                    players.add(AllMusic.side.getPlayerName(item));
                }
                return players;
            }
            return new ArrayList<String>() {{
                add("list");
            }};
        } else {
            return super.tab(player, name, args, index);
        }
    }
}
