package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;

public class CommandClearList implements ICommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        AllMusic.getConfig().PlayList.clear();
        AllMusic.saveConfig();
        AllMusic.side.sendMessage(sender, "§d[AllMusic]§2添加空闲音乐列表已清空");
    }
}
