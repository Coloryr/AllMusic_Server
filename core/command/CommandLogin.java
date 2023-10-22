package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;

public class CommandLogin implements ICommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (args.length != 2) {
            AllMusic.side.sendMessage(sender, "§d[AllMusic]§c没有手机验证码");
            return;
        }
        AllMusic.side.sendMessage(sender, "§d[AllMusic]§d重新登录网易云账户");
        AllMusic.getMusicApi().login(sender, args[1]);
    }
}
