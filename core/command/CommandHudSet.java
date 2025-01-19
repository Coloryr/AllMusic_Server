package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.enums.HudDirType;
import com.coloryr.allmusic.server.core.objs.enums.HudType;
import com.coloryr.allmusic.server.core.objs.hud.PosObj;
import com.coloryr.allmusic.server.core.objs.message.PAL;
import com.coloryr.allmusic.server.core.utils.HudUtils;

import java.util.*;

public class CommandHudSet extends AHudCommand {
    /**
     * Hud的指令
     */
    private static final List<String> hud = new ArrayList<String>() {{
        this.add("enable");
        this.add("pos");
        this.add("dir");
        this.add("reset");
    }};
    private static final List<String> pic = new ArrayList<String>() {{
        this.add("size");
        this.add("rotate");
        this.add("speed");
    }};
    private static final List<String> info = new ArrayList<String>() {{
        this.add("color");
        this.add("shadow");
    }};
    private static final List<String> tf = new ArrayList<String>() {{
        this.add("true");
        this.add("false");
    }};
    private final Map<String, ICommand> commandList = new HashMap<>();

    public CommandHudSet(HudType type) {
        super(type);
        commandList.put("enable", new HudEnable(type));
        commandList.put("reset", new HudReset(type));
        commandList.put("pos", new HudPos(type));
        commandList.put("dir", new HudDir(type));
        if (type == HudType.PIC) {
            commandList.put("size", new PicSize());
            commandList.put("rotate", new PicRotate());
            commandList.put("speed", new PicRotateSpeed());
        } else {
            commandList.put("color", new HudColor(type));
            commandList.put("shadow", new HudShadow(type));
        }
    }

    @Override
    public void execute(Object sender, String name, String[] args) {
        if (args.length == 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
        } else {
            ICommand command = commandList.get(args[2]);
            if (command != null) {
                command.execute(sender, name, args);
            } else {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            }
        }
    }

    @Override
    public List<String> tab(String name, String[] args, int index) {
        if (args.length == index + 1) {
            List<String> list = new ArrayList<>(hud);
            if (type == HudType.PIC) {
                list.addAll(pic);
            } else {
                list.addAll(info);
            }

            return list;
        } else {
            ICommand command = commandList.get(args[index]);
            if (command != null) {
                return command.tab(name, args, index + 1);
            }
        }
        return Collections.emptyList();
    }

    private static class HudEnable extends AHudCommand {
        public HudEnable(HudType type) {
            super(type);
        }

        @Override
        public void execute(Object sender, String name, String[] args) {
            if (args.length == 3 || args.length == 4) {
                boolean temp = HudUtils.setHudEnable(name, type, args.length == 4 ? args[3] : null);
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().hud.state
                        .replace(PAL.state, temp
                                ? AllMusic.getMessage().hudList.enable
                                : AllMusic.getMessage().hudList.disable)
                        .replace(PAL.hud, AllMusic.getMessage().hudList.getHud(type)));
                return;
            }
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
        }

        @Override
        public List<String> tab(String name, String[] args, int index) {
            if (args.length == index + 1) {
                return tf;
            }
            return Collections.emptyList();
        }
    }

    private static class HudReset extends AHudCommand {
        public HudReset(HudType type) {
            super(type);
        }

        @Override
        public void execute(Object sender, String name, String[] args) {
            HudUtils.reset(name, type);
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().hud.reset
                    .replace(PAL.hud, AllMusic.getMessage().hudList.getHud(type)));
        }
    }

    private static class HudPos extends AHudCommand {
        public HudPos(HudType type) {
            super(type);
        }

        @Override
        public void execute(Object sender, String name, String[] args) {
            try {
                if (args.length != 5) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
                    return;
                }
                PosObj obj = HudUtils.setHudPos(name, type, args[3], args[4]);
                if (obj == null) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
                    return;
                }

                AllMusic.side.sendMessage(sender, AllMusic.getMessage().hud.set
                        .replace(PAL.hud, AllMusic.getMessage().hudList.getHud(type))
                        .replace(PAL.x, args[3])
                        .replace(PAL.y, args[4]));
            } catch (Exception e) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            }
        }
    }

    private static class HudDir extends AHudCommand {
        private static final List<String> dir = new ArrayList<String>() {{
            for (HudDirType type : HudDirType.values()) {
                this.add(type.name());
            }
        }};

        public HudDir(HudType type) {
            super(type);
        }

        @Override
        public void execute(Object sender, String name, String[] args) {
            if (args.length != 4) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
                return;
            }
            HudDirType type1 = HudUtils.setDir(name, type, args[3]);
            if (type1 == null) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
                return;
            }
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().hud.set1
                    .replace(PAL.hud, AllMusic.getMessage().hudList.getHud(type))
                    .replace(PAL.dir, AllMusic.getMessage().hudList.getDir(type1)));
        }

        @Override
        public List<String> tab(String name, String[] args, int index) {
            if (args.length == index + 1) {
                return dir;
            }
            return Collections.emptyList();
        }
    }

    private static class HudColor extends AHudCommand {
        public HudColor(HudType type) {
            super(type);
        }

        @Override
        public void execute(Object sender, String name, String[] args) {
            if (args.length != 4) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
                return;
            }
            if (!HudUtils.setColor(name, type, args[3])) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
                return;
            }
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().hud.set2
                    .replace(PAL.hud, AllMusic.getMessage().hudList.getHud(type))
                    .replace(PAL.color, args[3]));
        }
    }

    private static class HudShadow extends AHudCommand {
        public HudShadow(HudType type) {
            super(type);
        }

        @Override
        public void execute(Object sender, String name, String[] args) {
            if (args.length == 3 || args.length == 4) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().hud.set3
                        .replace(PAL.hud, AllMusic.getMessage().hudList.getHud(type))
                        .replace(PAL.state, HudUtils.setShadow(name, type, args.length == 4 ? args[3] : null)
                                ? AllMusic.getMessage().hudList.enable
                                : AllMusic.getMessage().hudList.disable));
                return;
            }
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
        }

        @Override
        public List<String> tab(String name, String[] args, int index) {
            if (args.length == index + 1) {
                return tf;
            }
            return Collections.emptyList();
        }
    }

    private static class PicSize extends ACommand {
        @Override
        public void execute(Object sender, String name, String[] args) {
            if (args.length != 4 || !HudUtils.setPicSize(name, args[3])) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
                return;
            }
            AllMusic.side.sendMessage(sender,
                    AllMusic.getMessage().hud.picSize.replace(PAL.size, args[2]));
        }
    }

    private static class PicRotate extends ACommand {
        @Override
        public void execute(Object sender, String name, String[] args) {
            if (args.length == 3 || args.length == 4) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().hud.picRotate
                        .replace(PAL.state, HudUtils.setPicRotate(name, args.length == 4 ? args[3] : null)
                                ? AllMusic.getMessage().hudList.enable
                                : AllMusic.getMessage().hudList.disable));
                return;
            }
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
        }

        @Override
        public List<String> tab(String name, String[] args, int index) {
            if (args.length == index + 1) {
                return tf;
            }
            return Collections.emptyList();
        }
    }

    private static class PicRotateSpeed extends ACommand {
        @Override
        public void execute(Object sender, String name, String[] args) {
            if (args.length != 4 || !HudUtils.setPicRotateSpeed(name, args[3])) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
                return;
            }
            AllMusic.side.sendMessage(sender,
                    AllMusic.getMessage().hud.picSpeed.replace(PAL.size, args[3]));
        }
    }
}
