package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.objs.enums.HudType;
import coloryr.allmusic.core.objs.enums.HudDirType;
import coloryr.allmusic.core.objs.hud.PosObj;
import coloryr.allmusic.core.utils.HudUtils;

import java.util.*;

public class CommandHudSet extends AHudCommand {
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
        }
    }


    private static class HudEnable extends AHudCommand {
        public HudEnable(HudType type) {
            super(type);
        }

        @Override
        public void ex(Object sender, String name, String[] args) {
            boolean temp = HudUtils.setHudEnable(name, type);
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().hud.state
                    .replace("%State%", temp
                            ? AllMusic.getMessage().hudList.enable
                            : AllMusic.getMessage().hudList.disable)
                    .replace("%Hud%", AllMusic.getMessage().hudList.all));
        }
    }

    private static class HudReset extends AHudCommand {
        public HudReset(HudType type) {
            super(type);
        }

        @Override
        public void ex(Object sender, String name, String[] args) {
            HudUtils.reset(name, type);
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().hud.reset
                    .replace("%Hud%", AllMusic.getMessage().hudList.getHud(type)));
        }
    }

    private static class HudPos extends AHudCommand {
        public HudPos(HudType type) {
            super(type);
        }

        @Override
        public void ex(Object sender, String name, String[] args) {
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
                        .replace("%Hud%", AllMusic.getMessage().hudList.getHud(type))
                        .replace("%x%", args[3])
                        .replace("%y%", args[4]));
            } catch (Exception e) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            }
        }
    }

    private static class HudDir extends AHudCommand {
        public HudDir(HudType type) {
            super(type);
        }

        @Override
        public void ex(Object sender, String name, String[] args) {
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
                    .replace("%Hud%", AllMusic.getMessage().hudList.getHud(type))
                    .replace("%Dir%", AllMusic.getMessage().hudList.getDir(type1)));
        }

        private static final List<String> dir = new ArrayList<String>() {{
            for (HudDirType type : HudDirType.values()) {
                this.add(type.name());
            }
        }};

        @Override
        public List<String> tab(String name, String[] args) {
            if (args.length == 3 || (args.length == 4 && args[3].isEmpty())) {
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
        public void ex(Object sender, String name, String[] args) {
            if (!HudUtils.setColor(name, type, args[3])) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
                return;
            }
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().hud.set2
                    .replace("%Hud%", AllMusic.getMessage().hudList.getHud(type))
                    .replace("%Color%", args[3]));
        }
    }

    private static class PicSize implements ICommand {
        @Override
        public void ex(Object sender, String name, String[] args) {
            if (args.length != 3 || !HudUtils.setPicSize(name, args[2])) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
                return;
            }
            AllMusic.side.sendMessage(sender,
                    AllMusic.getMessage().hud.picSize.replace("%Size%", args[2]));
        }

        @Override
        public List<String> tab(String name, String[] args) {
            return Collections.emptyList();
        }
    }

    private static class PicRotate extends ACommand {
        @Override
        public void ex(Object sender, String name, String[] args) {
            if (args.length != 4) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
                return;
            }
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().hud.picRotate
                    .replace("%State%", HudUtils.setPicRotate(name, args[3])
                            ? AllMusic.getMessage().hudList.enable
                            : AllMusic.getMessage().hudList.disable));
        }
    }

    private static class PicRotateSpeed extends ACommand {
        @Override
        public void ex(Object sender, String name, String[] args) {
            if (args.length != 4 || !HudUtils.setPicRotateSpeed(name, args[3])) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
                return;
            }
            AllMusic.side.sendMessage(sender,
                    AllMusic.getMessage().hud.picSpeed.replace("%Size%", args[2]));
        }
    }

    @Override
    public void ex(Object sender, String name, String[] args) {
        if (args.length == 1) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
        } else {
            ICommand command = commandList.get(args[2]);
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
    }};

    @Override
    public List<String> tab(String name, String[] args) {
        if (args.length == 2 || (args.length == 3 && args[2].isEmpty())) {
            List<String> list = new ArrayList<>(hud);
            if (type == HudType.PIC) {
                list.addAll(pic);
            } else {
                list.addAll(info);
            }

            return list;
        }
        return Collections.emptyList();
    }
}
