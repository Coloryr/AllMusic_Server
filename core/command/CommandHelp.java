package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;

public class CommandHelp implements ICommand {
    @Override
    public void ex(Object sender, String name, String[] args) {
        AllMusic.side.sendMessage(sender, AllMusic.getMessage().Help.Normal.Head);
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Normal.Base,
                AllMusic.getMessage().Click.Check, "/music ");
        AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().Help.Normal.Stop,
                AllMusic.getMessage().Click.This, "/music stop");
        AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().Help.Normal.List,
                AllMusic.getMessage().Click.This, "/music list");
        AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().Help.Normal.Vote,
                AllMusic.getMessage().Click.This, "/music vote");
        AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().Help.Normal.NoMusic,
                AllMusic.getMessage().Click.This, "/music nomusic");
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Normal.Search,
                AllMusic.getMessage().Click.Check, "/music search ");
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Normal.Select,
                AllMusic.getMessage().Click.Check, "/music select ");
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Normal.Hud1,
                AllMusic.getMessage().Click.Check, "/music hud enable ");
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Normal.Hud2,
                AllMusic.getMessage().Click.Check, "/music hud ");
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Normal.Hud3,
                AllMusic.getMessage().Click.Check, "/music hud picsize ");
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Normal.Hud4,
                AllMusic.getMessage().Click.Check, "/music hud picrotate ");
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Normal.Hud5,
                AllMusic.getMessage().Click.Check, "/music hud picrotatespeed ");
        if (AllMusic.getConfig().Admin.contains(name)) {
            AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().Help.Admin.Reload,
                    AllMusic.getMessage().Click.This, "/music reload");
            AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().Help.Admin.Next,
                    AllMusic.getMessage().Click.This, "/music next");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Admin.Ban,
                    AllMusic.getMessage().Click.Check, "/music ban ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Admin.BanPlayer,
                    AllMusic.getMessage().Click.Check, "/music banplayer ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Admin.Url,
                    AllMusic.getMessage().Click.Check, "/music url ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Admin.Delete,
                    AllMusic.getMessage().Click.Check, "/music delete ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Admin.AddList,
                    AllMusic.getMessage().Click.Check, "/music addlist ");
            AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().Help.Admin.ClearList,
                    AllMusic.getMessage().Click.This, "/music clearlist");
            AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().Help.Admin.Login,
                    AllMusic.getMessage().Click.Check, "/music login ");
            AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().Help.Admin.Code,
                    AllMusic.getMessage().Click.This, "/music code");
        }
    }
}
