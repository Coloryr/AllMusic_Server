package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.hud.HudUtils;
import coloryr.allmusic.core.objs.hud.PosObj;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CommandHud implements ICommand {
    private final Map<String, ICommand> CommandList = new HashMap<>();

    public CommandHud() {
        CommandList.put("enable", new HudEnable());
        CommandList.put("reset", new Reset());
        CommandList.put("picsize", new PicSize());
        CommandList.put("picrotate", new PicRotate());
        CommandList.put("picrotatespeed", new PicRotateSpeed());
    }

    private static class HudEnable implements ICommand {
        @Override
        public void ex(Object sender, String name, String[] args) {
            if (args.length == 3) {
                try {
                    String pos = args[2].toLowerCase(Locale.ROOT);
                    boolean temp = HudUtils.setHudEnable(name, pos);
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().Hud.State
                            .replace("%State%", temp ? "启用" : "关闭")
                            .replace("%Hud%", AllMusic.getMessage().HudList.Get(pos)));
                } catch (Exception e) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
                }
            } else {
                boolean temp = HudUtils.setHudEnable(name, null);
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Hud.State
                        .replace("%State%", temp ? "启用" : "关闭")
                        .replace("%Hud%", AllMusic.getMessage().HudList.All));
            }
        }
    }

    private static class Reset implements ICommand {
        @Override
        public void ex(Object sender, String name, String[] args) {
            HudUtils.reset(name);
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Hud.Reset);
        }
    }

    private static class PicSize implements ICommand {
        @Override
        public void ex(Object sender, String name, String[] args) {
            if (args.length != 3 || !HudUtils.setPicSize(name, args[2])) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
                return;
            }
            AllMusic.side.sendMessage(sender,
                    AllMusic.getMessage().Hud.PicSize.replace("%Size%", args[2]));
        }
    }

    private static class PicRotate implements ICommand {
        @Override
        public void ex(Object sender, String name, String[] args) {
            if (args.length != 3) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
                return;
            }
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Hud.PicRotate
                    .replace("%State%", String.valueOf(HudUtils.setPicRotate(name, args[2]))));
        }
    }

    private static class PicRotateSpeed implements ICommand {
        @Override
        public void ex(Object sender, String name, String[] args) {
            if (args.length != 3 || !HudUtils.setPicRotateSpeed(name, args[2])) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
                return;
            }
            AllMusic.side.sendMessage(sender,
                    AllMusic.getMessage().Hud.PicRotateSpeed.replace("%Size%", args[2]));
        }
    }

    @Override
    public void ex(Object sender, String name, String[] args) {
        if (args.length == 1) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
        } else {
            ICommand command = CommandList.get(args[1]);
            if (command != null) {
                command.ex(sender, name, args);
            } else if (args.length != 4) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
            } else {
                try {
                    PosObj obj = HudUtils.setHudPos(name, args[1], args[2], args[3]);
                    if (obj == null) {
                        AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
                    } else {
                        String temp = AllMusic.getMessage().Hud.Set
                                .replace("%Hud%", args[1])
                                .replace("%x%", args[2])
                                .replace("%y%", args[3]);
                        AllMusic.side.sendMessage(sender, temp);
                    }
                } catch (Exception e) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
                }
            }
        }
    }
}
