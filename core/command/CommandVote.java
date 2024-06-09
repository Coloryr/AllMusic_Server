package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.play.PlayMusic;

public class CommandVote extends ACommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (AllMusic.getConfig().needPermission &&
                !AllMusic.side.checkPermission(name, "allmusic.vote")) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().vote.noPermission);
            return;
        } else if (PlayMusic.getListSize() == 0 && PlayMusic.getIdleListSize() == 0) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.emptyPlay);
        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("cancel")) {
                if (!PlayMusic.voteSender.equalsIgnoreCase(name)) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().vote.err1);
                    return;
                } else if (PlayMusic.voteTime <= 0) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().vote.err2);
                    return;
                }
                AllMusic.side.bq(AllMusic.getMessage().vote.cancel);
                PlayMusic.voteTime = -1;
                AllMusic.clearVote();
                PlayMusic.voteSender = null;
            } else {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            }
            return;
        } else if (args.length > 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            return;
        } else if (PlayMusic.voteTime <= 0) {
            PlayMusic.voteTime = AllMusic.getConfig().voteTime;
            AllMusic.addVote(name);
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().vote.doVote);
            String data = AllMusic.getMessage().vote.bq;
            AllMusic.side.bq(data.replace("%PlayerName%", name)
                    .replace("%Time%", String.valueOf(AllMusic.getConfig().voteTime)));
            AllMusic.side.bqRun(AllMusic.getMessage().vote.bq1, AllMusic.getMessage().vote.bq2, "/music vote");
        } else {
            if (!AllMusic.containVote(name)) {
                AllMusic.addVote(name);
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().vote.agree);
                String data = AllMusic.getMessage().vote.bqAgree;
                data = data.replace("%PlayerName%", name)
                        .replace("%Count%", "" + AllMusic.getVoteCount());
                AllMusic.side.bq(data);
            } else {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().vote.arAgree);
            }
        }
        AllMusic.getConfig().removeNoMusicPlayer(name);
    }
}
