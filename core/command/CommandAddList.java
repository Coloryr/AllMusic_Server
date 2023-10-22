package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.utils.Function;

public class CommandAddList implements ICommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (args.length != 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
            return;
        }
        if (Function.isInteger(args[1])) {
            AllMusic.getMusicApi().setList(args[1], sender);
            AllMusic.side.sendMessage(sender, "§d[AllMusic]§2添加空闲音乐列表" + args[1]);
        } else {
            AllMusic.side.sendMessage(sender, "§d[AllMusic]§2请输入有效的音乐列表ID");
        }
    }
}
