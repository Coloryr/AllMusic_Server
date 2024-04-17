package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;

public class CommandSearch extends ACommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        if (AllMusic.getConfig().needPermission &&
                AllMusic.side.checkPermission(name, "allmusic.search")) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.noPer);
            return;
        }
        if (CommandEX.checkMoney(sender, name, AllMusic.getConfig().searchCost)) {
            return;
        }
        if (CommandEX.cost(sender, name, AllMusic.getConfig().searchCost,
                AllMusic.getMessage().cost.search)) {
            return;
        }

        AllMusic.side.sendMessage(sender, AllMusic.getMessage().search.startSearch);
        CommandEX.searchMusic(sender, name, args, false);
    }
}
