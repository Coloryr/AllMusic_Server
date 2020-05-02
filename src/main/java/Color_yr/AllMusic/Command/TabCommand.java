package Color_yr.AllMusic.Command;

import Color_yr.AllMusic.AllMusic;

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
            arguments.add("hud");
            if (AllMusic.getSearch(name) != null) {
                arguments.add("select");
                arguments.add("nextpage");
                arguments.add("lastpage");
            }
            if (AllMusic.getConfig().getAdmin().contains(name)) {
                arguments.add("reload");
                arguments.add("next");
                arguments.add("ban");
                arguments.add("delete");
                arguments.add("addlist");
            }
        } else if (arg[0].equalsIgnoreCase("hud")) {
            if (arg.length == 2) {
                arguments.add("info");
                arguments.add("list");
                arguments.add("lyric");
                arguments.add("enable");
            } else if (arg.length == 3) {
                if (arg[1].equalsIgnoreCase("enable")) {
                    arguments.add("info");
                    arguments.add("list");
                    arguments.add("lyric");
                }
            }
        }
        return arguments;
    }
}
