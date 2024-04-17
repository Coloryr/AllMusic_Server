package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;

public class CommandMute extends ACommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        AllMusic.side.sendStop(name);
        AllMusic.side.clearHud(name);
        AllMusic.getConfig().AddNoMusicPlayer(name);
        AllMusic.side.sendMessage(sender, AllMusic.getMessage().musicPlay.mute);
    }
}
