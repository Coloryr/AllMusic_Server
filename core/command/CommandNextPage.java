package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.objs.music.SearchPageObj;

public class CommandNextPage implements ICommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (AllMusic.getConfig().NeedPermission &&
                AllMusic.side.checkPermission(name, "allmusic.search")) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.NoPer);
            return;
        }
        SearchPageObj obj = AllMusic.getSearch(name);
        if (obj == null) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.NoSearch);
        } else if (obj.nextPage()) {
            CommandEX.showSearch(sender, obj);
        } else {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.CantNext);
        }
    }
}
