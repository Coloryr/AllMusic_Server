package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;

public class CommandSearch implements ICommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (AllMusic.getConfig().NeedPermission &&
                AllMusic.side.checkPermission(name, "allmusic.search")) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.NoPer);
            return;
        }
        if (CommandEX.checkMoney(sender, name, AllMusic.getConfig().SearchCost)) {
            return;
        }
        if (CommandEX.cost(sender, name, AllMusic.getConfig().SearchCost,
                AllMusic.getMessage().Cost.Search)) {
            return;
        }

        AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.StartSearch);
        CommandEX.searchMusic(sender, name, args, false);
    }
}
