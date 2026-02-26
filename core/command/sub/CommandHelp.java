package com.coloryr.allmusic.server.core.command.sub;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.command.ACommand;

public class CommandHelp extends ACommand {

    @Override
    public void execute(Object sender, String name, String[] args) {
        AllMusic.side.sendMessage(sender, AllMusic.getMessage().help.normal.head);
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.normal.base,
                AllMusic.getMessage().click.clickCheck, "/music ");
        AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.normal.stop,
                AllMusic.getMessage().click.clickRun, "/music stop");
        AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.normal.list,
                AllMusic.getMessage().click.clickRun, "/music list");
        AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.normal.cancel,
                AllMusic.getMessage().click.clickRun, "/music cancel");
        AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.normal.vote,
                AllMusic.getMessage().click.clickRun, "/music vote");
        AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.normal.vote1,
                AllMusic.getMessage().click.clickRun, "/music vote cancel");
        AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.normal.push,
                AllMusic.getMessage().click.clickRun, "/music push");
        AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.normal.push1,
                AllMusic.getMessage().click.clickRun, "/music push cancel");
        AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.normal.mute,
                AllMusic.getMessage().click.clickRun, "/music mute");
        AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.normal.mutelist,
                AllMusic.getMessage().click.clickRun, "/music mute list");
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.normal.search,
                AllMusic.getMessage().click.clickCheck, "/music search ");
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.normal.select,
                AllMusic.getMessage().click.clickCheck, "/music select ");
        AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.normal.nextpage,
                AllMusic.getMessage().click.clickRun, "/music nextpage");
        AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.normal.lastpage,
                AllMusic.getMessage().click.clickRun, "/music lastpage");
        AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.normal.hud9,
                AllMusic.getMessage().click.clickRun, "/music hud enable");
        AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.normal.hud10,
                AllMusic.getMessage().click.clickRun, "/music hud reset");
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.normal.hud1,
                AllMusic.getMessage().click.clickCheck, "/music hud ");
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.normal.hud2,
                AllMusic.getMessage().click.clickCheck, "/music hud ");
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.normal.hud6,
                AllMusic.getMessage().click.clickCheck, "/music hud ");
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.normal.hud7,
                AllMusic.getMessage().click.clickCheck, "/music hud ");
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.normal.hud8,
                AllMusic.getMessage().click.clickCheck, "/music hud ");
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.normal.hud3,
                AllMusic.getMessage().click.clickCheck, "/music hud pic size ");
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.normal.hud4,
                AllMusic.getMessage().click.clickCheck, "/music hud pic rotate ");
        AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.normal.hud5,
                AllMusic.getMessage().click.clickCheck, "/music hud pic speed ");
        if (AllMusic.side.checkPermission(sender)) {
            AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.admin.reload,
                    AllMusic.getMessage().click.clickRun, "/music reload");
            AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.admin.next,
                    AllMusic.getMessage().click.clickRun, "/music next");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.admin.ban,
                    AllMusic.getMessage().click.clickCheck, "/music ban ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.admin.ban1,
                    AllMusic.getMessage().click.clickCheck, "/music ban ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.admin.banPlayer,
                    AllMusic.getMessage().click.clickCheck, "/music banplayer ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.admin.unban,
                    AllMusic.getMessage().click.clickCheck, "/music unban ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.admin.unbanPlayer,
                    AllMusic.getMessage().click.clickCheck, "/music unbanplayer ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.admin.delete,
                    AllMusic.getMessage().click.clickCheck, "/music delete ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.admin.addList,
                    AllMusic.getMessage().click.clickCheck, "/music addlist ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.admin.addList1,
                    AllMusic.getMessage().click.clickCheck, "/music addlist ");
            AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.admin.clearList,
                    AllMusic.getMessage().click.clickRun, "/music clearlist");
            AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.admin.clearBanList,
                    AllMusic.getMessage().click.clickRun, "/music clearban");
            AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().help.admin.clearBanPlayerList,
                    AllMusic.getMessage().click.clickRun, "/music clearbanplayer");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.admin.cookie,
                    AllMusic.getMessage().click.clickCheck, "/music cookie ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.admin.test,
                    AllMusic.getMessage().click.clickCheck, "/music test ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().help.admin.test1,
                    AllMusic.getMessage().click.clickCheck, "/music test ");
        }
    }
}
