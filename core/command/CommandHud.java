package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.enums.HudType;
import com.coloryr.allmusic.server.core.objs.message.PAL;
import com.coloryr.allmusic.server.core.utils.HudUtils;

import java.util.*;

public class CommandHud extends ACommand {
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
    private final Map<String, ICommand> commandList = new HashMap<>();

    public CommandHud() {
        commandList.put("enable", new HudEnable());
        commandList.put("reset", new HudReset());
        commandList.put("info", new CommandHudSet(HudType.INFO));
        commandList.put("list", new CommandHudSet(HudType.LIST));
        commandList.put("lyric", new CommandHudSet(HudType.LYRIC));
        commandList.put("pic", new CommandHudSet(HudType.PIC));
    }

    @Override
    public void execute(Object sender, String name, String[] args) {
        if (args.length == 1) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
        } else {
            ICommand command = commandList.get(args[1]);
            if (command != null) {
                command.execute(sender, name, args);
            } else {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            }
        }
    }

    @Override
    public List<String> tab(Object player, String name, String[] args, int index) {
        if (args.length == index + 1) {
            return hudlist;
        } else {
            ICommand command = commandList.get(args[index]);
            if (command != null) {
                return command.tab(player, name, args, index + 1);
            }
        }
        return Collections.emptyList();
    }

    private static class HudEnable extends ACommand {
        private static final List<String> tf = new ArrayList<String>() {{
            this.add("true");
            this.add("false");
        }};

        @Override
        public void execute(Object sender, String name, String[] args) {
            if (args.length == 2 || args.length == 3) {
                boolean temp = HudUtils.setHudEnable(name, null, args.length == 3 ? args[2] : null);
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().hud.state
                        .replace(PAL.state, temp ? "启用" : "关闭")
                        .replace(PAL.hud, AllMusic.getMessage().hudList.getHud(null)));
                return;
            }
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
        }

        @Override
        public List<String> tab(Object player, String name, String[] args, int index) {
            if (args.length == index + 1) {
                return tf;
            }
            return Collections.emptyList();
        }
    }

    private static class HudReset extends ACommand {
        @Override
        public void execute(Object sender, String name, String[] args) {
            HudUtils.reset(name);
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().hud.reset
                    .replace(PAL.hud, AllMusic.getMessage().hudList.getHud(null)));
        }
    }
}
