package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.objs.enums.HudType;
import coloryr.allmusic.core.utils.HudUtils;

import java.util.*;

public class CommandHud extends ACommand {
    private final Map<String, ICommand> commandList = new HashMap<>();

    public CommandHud() {
        commandList.put("enable", new HudEnable());
        commandList.put("reset", new HudReset());
        commandList.put("info", new CommandHudSet(HudType.INFO));
        commandList.put("list", new CommandHudSet(HudType.LIST));
        commandList.put("lyric", new CommandHudSet(HudType.LYRIC));
        commandList.put("pic", new CommandHudSet(HudType.PIC));
    }

    public static class HudEnable implements  ICommand {
        @Override
        public void ex(Object sender, String name, String[] args) {
            boolean temp = HudUtils.setHudEnable(name, null);
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().hud.state
                    .replace("%State%", temp ? "启用" : "关闭")
                    .replace("%Hud%", AllMusic.getMessage().hudList.getHud(null)));
        }

        @Override
        public List<String> tab(String name, String[] args) {
            return Collections.emptyList();
        }
    }

    public static class HudReset implements  ICommand {
        @Override
        public void ex(Object sender, String name, String[] args) {
            HudUtils.reset(name);
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().hud.reset
                    .replace("%Hud%", AllMusic.getMessage().hudList.getHud(null)));
        }

        @Override
        public List<String> tab(String name, String[] args) {
            return Collections.emptyList();
        }
    }

    @Override
    public void ex(Object sender, String name, String[] args) {
        if (args.length == 1) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
        } else {
            ICommand command = commandList.get(args[1]);
            if (command != null) {
                command.ex(sender, name, args);
            } else {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            }
        }
    }

    /**
     * Hud的指令
     */
    private static final List<String> hudlist = new ArrayList<String>() {{
        this.add("enable");
        this.add("reset");
        this.add("info");
        this.add("list");
        this.add("lyric");
        this.add("pic");
    }};

    @Override
    public List<String> tab(String name, String[] args) {
        if (args.length == 1 || (args.length == 2 && args[1].isEmpty())) {
            return hudlist;
        }
        return Collections.emptyList();
    }
}
