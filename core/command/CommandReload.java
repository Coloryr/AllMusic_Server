package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;

public class CommandReload implements ICommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        AllMusic.side.reload();
        AllMusic.side.sendMessage(sender, "§d[AllMusic]§2已重读配置文件");
    }
}
