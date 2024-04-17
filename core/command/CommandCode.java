package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;

public class CommandCode extends ACommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (args.length != 2) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            return;
        }
        AllMusic.getMusicApi().sendCode(sender, args[1]);
    }
}
