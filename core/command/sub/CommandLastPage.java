package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;
import com.coloryr.allmusic.server.core.command.PermissionList;
import com.coloryr.allmusic.server.core.music.MusicSearch;
import com.coloryr.allmusic.server.core.objs.music.SearchPageObj;

public class CommandLastPage extends ACommand {
    @Override
    public void execute(Object sender, String name, String[] args) {
        if (AllMusic.getConfig().needPermission &&
                !AllMusic.side.checkPermission(sender, PermissionList.PERMISSION_SEARCH)) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.noPer);
            return;
        }
        SearchPageObj obj = AllMusic.getSearch(name);
        if (obj == null) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.emptySearch);
        } else if (obj.lastPage()) {
            MusicSearch.showSearch(sender, obj);
        } else {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.cantLast);
        }
    }
}
