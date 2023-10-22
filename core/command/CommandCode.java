package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;

public class CommandCode implements ICommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        AllMusic.getMusicApi().sendCode(sender);
    }
}
