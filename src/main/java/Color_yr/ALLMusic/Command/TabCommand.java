package Color_yr.ALLMusic.Command;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.SongSearch.PlayerSearch;

import java.util.ArrayList;
import java.util.List;

public class TabCommand {
    public static List<String> GetTabList(String name, String[] arg) {
        List<String> arguments = new ArrayList<>();
        if (arg.length == 1) {
            arguments.add("stop");
            arguments.add("list");
            arguments.add("vote");
            arguments.add("nomusic");
            arguments.add("search");
            if (PlayerSearch.SearchSave.get(name) != null) {
                arguments.add("select");
                arguments.add("nextpage");
                arguments.add("lastpage");
            }
            if (ALLMusic.VV != null)
                arguments.add("vv");
            if (ALLMusic.Config.getAdmin().contains(name)) {
                arguments.add("reload");
                arguments.add("next");
                arguments.add("ban");
                arguments.add("delete");
                arguments.add("addlist");
            }
        } else if (ALLMusic.VV != null && arg[0].equalsIgnoreCase("vv")) {
            if (arg.length == 2) {
                arguments.add("info");
                arguments.add("list");
                arguments.add("lyric");
                arguments.add("enable");
            } else if (arg.length == 3) {
                arguments.add("x");
                arguments.add("y");
            }
        }
        return arguments;
    }
}
