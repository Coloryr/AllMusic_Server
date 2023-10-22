package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.music.play.PlayMusic;

public class CommandVote implements ICommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (AllMusic.getConfig().NeedPermission &&
                AllMusic.side.checkPermission(name, "allmusic.vote")) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Vote.NoPermission);
            return;
        }
        if (PlayMusic.getSize() == 0 && AllMusic.getConfig().PlayList.size() == 0) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().MusicPlay.NoPlay);
        } else if (PlayMusic.voteTime == 0) {
            PlayMusic.voteTime = AllMusic.getConfig().VoteTime;
            AllMusic.addVote(name);
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Vote.DoVote);
            String data = AllMusic.getMessage().Vote.BQ;
            AllMusic.side.bq(data.replace("%PlayerName%", name)
                    .replace("%Time%", String.valueOf(AllMusic.getConfig().VoteTime)));
        } else if (PlayMusic.voteTime > 0) {
            if (!AllMusic.containVote(name)) {
                AllMusic.addVote(name);
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Vote.Agree);
                String data = AllMusic.getMessage().Vote.BQAgree;
                data = data.replace("%PlayerName%", name)
                        .replace("%Count%", "" + AllMusic.getVoteCount());
                AllMusic.side.bq(data);
            } else {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Vote.ARAgree);
            }
        }
        AllMusic.getConfig().RemoveNoMusicPlayer(name);
    }
}
