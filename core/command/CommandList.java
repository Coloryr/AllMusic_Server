package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.music.play.PlayMusic;

public class CommandList implements ICommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (PlayMusic.nowPlayMusic == null || PlayMusic.nowPlayMusic.isNull()) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().MusicPlay.NoMusic);
        } else {
            String info = AllMusic.getMessage().MusicPlay.Play;
            info = info.replace("%MusicName%", PlayMusic.nowPlayMusic.getName())
                    .replace("%MusicAuthor%", PlayMusic.nowPlayMusic.getAuthor())
                    .replace("%MusicAl%", PlayMusic.nowPlayMusic.getAl())
                    .replace("%MusicAlia%", PlayMusic.nowPlayMusic.getAlia())
                    .replace("%PlayerName%", PlayMusic.nowPlayMusic.getCall());
            AllMusic.side.sendMessage(sender, info);
        }
        if (PlayMusic.getSize() == 0) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().MusicPlay.NoPlay);
        } else {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().MusicPlay.ListMusic.Head
                    .replace("%Count%", "" + PlayMusic.getSize()));
            AllMusic.side.sendMessage(sender, PlayMusic.getAllList());
        }
    }
}
