package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.music.play.PlayMusic;
import coloryr.allmusic.core.objs.music.MusicObj;

public class CommandUrl implements ICommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (args.length != 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
            return;
        }
        MusicObj obj = new MusicObj();
        obj.sender = sender;
        obj.isUrl = true;
        obj.url = args[1];
        PlayMusic.addTask(obj);
        AllMusic.side.sendMessage(sender, AllMusic.getMessage().AddMusic.Success);
    }
}
