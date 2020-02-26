package Color_yr.ALLMusic.Command;

import Color_yr.ALLMusic.ALLMusic;

import java.util.ArrayList;
import java.util.List;

public class TabCommand {
    public static List<String> GetTabList(String name) {
        List<String> arguments = new ArrayList<>();
        arguments.add("stop");
        arguments.add("list");
        arguments.add("vote");
        arguments.add("nomusic");
        if (ALLMusic.Config.getAdmin().contains(name)) {
            arguments.add("reload");
            arguments.add("next");
            arguments.add("ban");
            arguments.add("delete");
            arguments.add("addlist");
        }
        return arguments;
    }
}
