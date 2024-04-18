package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.objs.music.SearchPageObj;
import coloryr.allmusic.core.utils.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandSelect extends ACommand {
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
        } else if (!args[1].isEmpty() && Function.isInteger(args[1])) {
            int a = Integer.parseInt(args[1]);
            if (a == 0) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.errorNum);
                return;
            }
            String[] ID = new String[1];
            ID[0] = obj.getSong((obj.getPage() * 10) + (a - 1));
            AllMusic.side.sendMessage(sender,
                    AllMusic.getMessage().search.choice.replace("%Num%", "" + a));
            CommandEX.addMusic(sender, name, ID);
            AllMusic.removeSearch(name);
        } else {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.errorNum);
        }
    }

    @Override
    public List<String> tab(String name, String[] args, int index) {
        if (args.length == 1 || (args.length == 2 && args[1].isEmpty())) {
            List<String> list = new ArrayList<>();
            SearchPageObj obj = AllMusic.getSearch(name);
            if (obj != null) {
                for (int a = 0; a < obj.getIndex(); a++) {
                    list.add(String.valueOf(a));
                }
            }
            return list;
        }
        return Collections.emptyList();
    }
}
