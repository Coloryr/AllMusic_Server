package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.command.CommandEX;
import com.coloryr.allmusic.server.core.music.PlayMusic;
import com.coloryr.allmusic.server.core.sql.DataSql;

import java.util.ArrayList;
import java.util.List;

public class CommandMute extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("list")) {
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
            } else if (CommandEX.checkAdmin(sender, name)) {
                String finalName = args[1];
                DataSql.task(() -> {
                    if (DataSql.checkMutePlayer(finalName)) {
                        DataSql.removeMutePlayer(finalName);
                        AllMusic.side.sendMessage(sender, "已取消玩家：" + finalName + "的静音");
                    } else {
                        DataSql.addMutePlayer(finalName);
                        AllMusic.side.runTask(() -> {
                            AllMusic.side.sendStop(finalName);
                            AllMusic.side.clearHud(finalName);
                            AllMusic.side.sendMessage(sender, "已设置玩家：" + finalName + "的静音");
                        });
                    }
                });
            }
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
