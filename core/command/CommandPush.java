package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.play.PlayMusic;
import com.coloryr.allmusic.server.core.objs.message.PAL;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandPush extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (AllMusic.getConfig().needPermission &&
                !AllMusic.side.checkPermission(name, "allmusic.push")) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.noPermission);
            return;
        }
        if (PlayMusic.getListSize() == 0 && PlayMusic.getIdleListSize() == 0) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.emptyPlay);
        }
        SongInfoObj music = null;
        if (args.length == 1) {
            music = PlayMusic.findPlayerMusic(name);
            if (music == null) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.noId);
                return;
            }
            SongInfoObj id1 = PlayMusic.findMusicIndex(1);
            if (id1 != null && id1.getID().equalsIgnoreCase(music.getID())) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.pushErr);
                return;
            }
        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("cancel")) {
                if (!name.equalsIgnoreCase(PlayMusic.getPushSender())) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.err1);
                    return;
                }
                if (PlayMusic.getPushTime() <= 0) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.err2);
                    return;
                }
                PlayMusic.clearPush();
                AllMusic.side.broadcast(AllMusic.getMessage().push.cancel);
                return;
            } else {
                try {
                    int index = Integer.parseInt(args[1]);
                    music = PlayMusic.findMusicIndex(index);
                } catch (Exception e) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.noId);
                    return;
                }
                if (music == null) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.noId1.replace(PAL.index, args[1]));
                    return;
                }
            }
        } else if (args.length > 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            return;
        }
        if (PlayMusic.getPushTime() <= 0) {
            if (music == null) {
                return;
            }
            PlayMusic.startPush(name, music);
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.doVote);
            String data = AllMusic.getMessage().push.bq;
            AllMusic.side.broadcast(data
                    .replace(PAL.player, name)
                    .replace(PAL.time, String.valueOf(AllMusic.getConfig().voteTime))
                    .replace(PAL.musicName, music.getName())
                    .replace(PAL.musicAuthor, music.getAuthor()));
            AllMusic.side.broadcastWithRun(AllMusic.getMessage().push.bq1, AllMusic.getMessage().push.bq2, "/music push");
        } else {
            if (music != null) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.err3);
                return;
            }
            if (!PlayMusic.containPush(name)) {
                PlayMusic.addPush(name);
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.agree);
                String data = AllMusic.getMessage().push.bqAgree;
                data = data.replace(PAL.player, name)
                        .replace(PAL.count, String.valueOf(PlayMusic.getVoteCount()));
                AllMusic.side.broadcast(data);
            } else {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.arAgree);
            }
        }
        AllMusic.getConfig().removeNoMusicPlayer(name);
    }

    @Override
    public List<String> tab(String name, String[] args, int index) {
        if (args.length == 1 || (args.length == 2 && args[1].isEmpty())) {
            List<String> list = new ArrayList<>();
            List<SongInfoObj> list1 = PlayMusic.getList();
            for (int a = 1; a < list1.size(); a++) {
                SongInfoObj item = list1.get(a);
                if (item.getCall().equalsIgnoreCase(name)) {
                    list.add(String.valueOf(a));
                }
            }

            return list;
        }
        return Collections.emptyList();
    }
}
