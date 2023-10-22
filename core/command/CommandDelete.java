package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.music.play.PlayMusic;
import coloryr.allmusic.core.utils.Function;

public class CommandDelete implements ICommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (args.length != 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
            return;
        }
        if (!args[1].isEmpty() && Function.isInteger(args[1])) {
            int music = Integer.parseInt(args[1]);
            if (music == 0) {
                AllMusic.side.sendMessage(sender, "§d[AllMusic]§2请输入有效的序列ID");
                return;
            }
            if (music > PlayMusic.getSize()) {
                AllMusic.side.sendMessage(sender, "§d[AllMusic]§2序列号过大");
                return;
            }
            PlayMusic.remove(music - 1);
            AllMusic.side.sendMessage(sender, "§d[AllMusic]§2已删除序列" + music);
        } else {
            AllMusic.side.sendMessage(sender, "§d[AllMusic]§2请输入有效的序列ID");
        }
    }
}
