package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.play.PlayMusic;
import com.coloryr.allmusic.server.core.sql.DataSql;

import java.util.ArrayList;
import java.util.List;

public class CommandMute extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (args.length == 2 && args[1].equalsIgnoreCase("list")) {
            DataSql.task(() -> {
                if (DataSql.checkMuteListPlayer(name)) {
                    DataSql.removeMuteListPlayer(name);
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.mute2);
                } else {
                    DataSql.addMuteListPlayer(name);
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.mute1);
                    if (PlayMusic.nowPlayMusic != null && PlayMusic.nowPlayMusic.isList()) {
                        AllMusic.side.runTask(() -> {
                            AllMusic.side.sendStop(name);
                            AllMusic.side.clearHud(name);
                        });
                    }
                }
            });
        } else {
            DataSql.task(() -> {
                if (DataSql.checkMutePlayer(name)) {
                    DataSql.removeMutePlayer(name);
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.mute3);
                } else {
                    DataSql.addMutePlayer(name);
                    AllMusic.side.runTask(() -> {
                        AllMusic.side.sendStop(name);
                        AllMusic.side.clearHud(name);
                        AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.mute);
                    });
                }
            });
        }
    }

    @Override
    public List<String> tab(Object player, String name, String[] args, int index) {
        if (args.length == index || (args.length == index + 1 )) {
            return new ArrayList<String>() {{
                add("list");
            }};
        } else {
            return super.tab(player, name, args, index);
        }
    }
}
