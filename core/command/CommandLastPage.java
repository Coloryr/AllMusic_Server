package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.objs.music.SearchPageObj;

public class CommandLastPage extends ACommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (AllMusic.getConfig().needPermission &&
                AllMusic.side.checkPermission(name, "allmusic.search")) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.noPer);
            return;
        }
        SearchPageObj obj = AllMusic.getSearch(name);
        if (obj == null) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.emptySearch);
        } else if (obj.lastPage()) {
            CommandEX.showSearch(sender, obj);
        } else {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.cantLast);
        }
    }
}
