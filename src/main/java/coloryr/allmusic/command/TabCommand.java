package coloryr.allmusic.command;

import coloryr.allmusic.AllMusic;

import java.util.ArrayList;
import java.util.List;

public class TabCommand {
    private static final List<String> normal = new ArrayList<String>() {{
        this.add("stop");
        this.add("list");
        this.add("vote");
        this.add("nomusic");
        this.add("search");
        this.add("hud");
    }};
    private static final List<String> search = new ArrayList<String>() {{
        this.add("select");
        this.add("nextpage");
        this.add("lastpage");
    }};
    private static final List<String> admin = new ArrayList<String>() {{
        this.add("reload");
        this.add("next");
        this.add("ban");
        this.add("delete");
        this.add("addlist");
        this.add("clearlist");
        this.add("initApi");
        this.add("cancelApi");
        this.add("login");
    }};
    private static final List<String> hudlist = new ArrayList<String>() {{
        this.add("info");
        this.add("list");
        this.add("lyric");
        this.add("pic");
    }};
    private static final List<String> hud = new ArrayList<String>() {{
        this.add("info");
        this.add("list");
        this.add("lyric");
        this.add("pic");
        this.add("picsize");
        this.add("enable");
        this.add("reset");
    }};

    public static List<String> getTabList(String name, String[] arg) {
        List<String> arguments = new ArrayList<>();
        if (arg.length == 1 || arg.length == 0) {
            arguments.addAll(normal);
            if (AllMusic.getSearch(name) != null) {
                arguments.addAll(search);
            }
            if (AllMusic.getConfig().Admin.contains(name)) {
                arguments.addAll(admin);
            }
        } else if (arg[0].equalsIgnoreCase("hud")) {
            if (arg.length == 2) {
                arguments.addAll(hud);
            } else if (arg.length == 3) {
                if (arg[1].equalsIgnoreCase("enable")) {
                    arguments.addAll(hudlist);
                }
            }
        }
        return arguments;
    }
}
