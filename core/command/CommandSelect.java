package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.objs.music.SearchPageObj;
import coloryr.allmusic.core.utils.Function;

public class CommandSelect implements ICommand {
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
        } else if (!args[1].isEmpty() && Function.isInteger(args[1])) {
            int a = Integer.parseInt(args[1]);
            if (a == 0) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.ErrorNum);
                return;
            }
            String[] ID = new String[1];
            ID[0] = obj.getSong((obj.getPage() * 10) + (a - 1));
            AllMusic.side.sendMessage(sender,
                    AllMusic.getMessage().Search.Chose.replace("%Num%", "" + a));
            CommandEX.addMusic(sender, name, ID);
            AllMusic.removeSearch(name);
        } else {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.ErrorNum);
        }
    }
}
