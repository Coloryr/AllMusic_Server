package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.play.PlayMusic;
import com.coloryr.allmusic.server.core.objs.message.PAL;

public class CommandVote extends ACommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (AllMusic.getConfig().needPermission &&
                !AllMusic.side.checkPermission(name, "allmusic.vote")) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().vote.noPermission);
            return;
        } else if (PlayMusic.getListSize() == 0 && PlayMusic.getIdleListSize() == 0) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.emptyPlay);
        } else if (PlayMusic.nowPlayMusic == null) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.emptyPlayingMusic);
        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("cancel")) {
                if (!name.equalsIgnoreCase(PlayMusic.getVoteSender())) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().vote.err1);
                    return;
                } else if (PlayMusic.getVoteTime() <= 0) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().vote.err2);
                    return;
                }
                AllMusic.side.bq(AllMusic.getMessage().vote.cancel);
                PlayMusic.clearVote();
            } else {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            }
            return;
        } else if (args.length > 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            return;
        } else if (PlayMusic.getVoteTime() <= 0) {
            PlayMusic.startVote(name);
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().vote.doVote);
            String data = AllMusic.getMessage().vote.bq;
            AllMusic.side.bq(data.replace(PAL.player, name)
                    .replace(PAL.time, String.valueOf(AllMusic.getConfig().voteTime)));
            AllMusic.side.bqRun(AllMusic.getMessage().vote.bq1, AllMusic.getMessage().vote.bq2, "/music vote");
        } else {
            if (!PlayMusic.containVote(name)) {
                PlayMusic.addVote(name);
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().vote.agree);
                String data = AllMusic.getMessage().vote.bqAgree;
                data = data.replace(PAL.player, name)
                        .replace(PAL.count, String.valueOf(PlayMusic.getVoteCount()));
                AllMusic.side.bq(data);
            } else {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().vote.arAgree);
            }
        }
        AllMusic.getConfig().removeNoMusicPlayer(name);
    }
}
