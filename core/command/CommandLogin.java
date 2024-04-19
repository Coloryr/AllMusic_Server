package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;

public class CommandLogin extends ACommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (args.length != 2) {
            AllMusic.side.sendMessage(sender, "§d[AllMusic3]§c没有手机验证码");
            return;
        }
        AllMusic.side.sendMessage(sender, "§d[AllMusic3]§d登录网易云账户");
        AllMusic.getMusicApi().login(sender, args[1]);
    }
}
