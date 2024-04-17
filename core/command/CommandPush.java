package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.music.play.PlayMusic;
import coloryr.allmusic.core.objs.music.SongInfoObj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandPush extends ACommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (AllMusic.getConfig().needPermission &&
                AllMusic.side.checkPermission(name, "allmusic.push")) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.noPermission);
            return;
        }
        if (PlayMusic.getListSize() == 0 && PlayMusic.getIdleListSize() == 0) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.emptyPlay);
        }
        SongInfoObj id = null;
        if (args.length == 1) {
            id = PlayMusic.findPlayerMusic(name);
            if (id == null) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.noId);
                return;
            }
            SongInfoObj id1 = PlayMusic.findMusicIndex(1);
            if (id1 != null && id1.getID().equalsIgnoreCase(id.getID())) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.pushErr);
                return;
            }
        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("cancel")) {
                if (!PlayMusic.pushSender.equalsIgnoreCase(name)) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.err1);
                    return;
                }
                if (PlayMusic.pushTime <= 0) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.err2);
                    return;
                }
                PlayMusic.pushTime = -1;
                AllMusic.clearPush();
                AllMusic.side.bq(AllMusic.getMessage().push.cancel);
                PlayMusic.pushSender = null;
                return;
            } else {
                try {
                    int index = Integer.parseInt(args[1]);
                    id = PlayMusic.findMusicIndex(index);
                } catch (Exception e) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.noId);
                    return;
                }
                if (id == null) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.noId1.replace("%Index%", args[1]));
                    return;
                }
            }
        } else if (args.length > 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            return;
        }
        if (PlayMusic.pushTime <= 0) {
            if (id == null) {
                return;
            }
            PlayMusic.pushTime = AllMusic.getConfig().voteTime;
            PlayMusic.push = id;
            AllMusic.addPush(name);
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.doVote);
            String data = AllMusic.getMessage().push.bq;
            AllMusic.side.bq(data
                    .replace("%PlayerName%", name)
                    .replace("%Time%", String.valueOf(AllMusic.getConfig().voteTime))
                    .replace("%MusicName%", id.getName())
                    .replace("%MusicAuthor%", id.getAuthor()));
            AllMusic.side.bqRun(AllMusic.getMessage().push.bq1, AllMusic.getMessage().push.bq2, "/music push");
        } else {
            if (id != null) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.err3);
                return;
            }
            if (!AllMusic.containVote(name)) {
                AllMusic.addPush(name);
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.agree);
                String data = AllMusic.getMessage().push.bqAgree;
                data = data.replace("%PlayerName%", name)
                        .replace("%Count%", "" + AllMusic.getVoteCount());
                AllMusic.side.bq(data);
            } else {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().push.arAgree);
            }
        }
        AllMusic.getConfig().RemoveNoMusicPlayer(name);
    }

    @Override
    public List<String> tab(String name, String[] args) {
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
