package com.coloryr.allmusic.server.core.objs.message;

import com.coloryr.allmusic.server.core.AllMusic;

public class MessageObj {
    public MusicPlayObj musicPlay;
    public AddMusicObj addMusic;
    public PageObj page;
    public LyricObj lyric;
    public VoteObj vote;
    public PushObj push;
    public SearchObj search;
    public HudObj hud;
    public HudNameObj hudList;
    public CommandObj command;
    public CustomObj custom;
    public PAPIObj papi;
    public CostObj cost;
    public ClickObj click;
    public HelpObj help;
    public FunObj fun;
    public CancelObj cancel;
    public String version;

    public static MessageObj make() {
        MessageObj obj = new MessageObj();
        obj.init();

        return obj;
    }

    public boolean check() {
        boolean saveConfig = false;
        if (musicPlay == null) {
            saveConfig = true;
            musicPlay = MusicPlayObj.make();
        } else if (musicPlay.check()) {
            saveConfig = true;
            musicPlay.init();
        }

        if (addMusic == null) {
            saveConfig = true;
            addMusic = AddMusicObj.make();
        } else if (addMusic.check()) {
            saveConfig = true;
            addMusic.init();
        }

        if (page == null) {
            saveConfig = true;
            page = PageObj.make();
        } else if (page.check()) {
            saveConfig = true;
            page.init();
        }

        if (lyric == null) {
            saveConfig = true;
            lyric = LyricObj.make();
        } else if (lyric.check()) {
            saveConfig = true;
            lyric.init();
        }

        if (vote == null) {
            saveConfig = true;
            vote = VoteObj.make();
        } else if (vote.check()) {
            saveConfig = true;
            vote.init();
        }

        if (search == null) {
            saveConfig = true;
            search = SearchObj.make();
        } else if (search.check()) {
            saveConfig = true;
            search.init();
        }

        if (hud == null) {
            saveConfig = true;
            hud = HudObj.make();
        } else if (hud.check()) {
            saveConfig = true;
            hud.init();
        }

        if (hudList == null) {
            saveConfig = true;
            hudList = HudNameObj.make();
        } else if (hudList.check()) {
            saveConfig = true;
            hudList.init();
        }

        if (command == null) {
            saveConfig = true;
            command = CommandObj.make();
        } else if (command.check()) {
            saveConfig = true;
            command.init();
        }

        if (custom == null) {
            saveConfig = true;
            custom = CustomObj.make();
        } else if (custom.check()) {
            saveConfig = true;
            custom.init();
        }

        if (papi == null) {
            saveConfig = true;
            papi = PAPIObj.make();
        } else if (papi.check()) {
            saveConfig = true;
            papi.init();
        }

        if (cost == null) {
            saveConfig = true;
            cost = CostObj.make();
        } else if (cost.check()) {
            saveConfig = true;
            cost.init();
        }

        if (click == null) {
            saveConfig = true;
            click = ClickObj.make();
        } else if (click.check()) {
            saveConfig = true;
            click.init();
        }

        if (help == null) {
            saveConfig = true;
            help = HelpObj.make();
        } else if (help.check()) {
            saveConfig = true;
            help.init();
        }

        if (fun == null) {
            saveConfig = true;
            fun = FunObj.make();
        } else if (fun.check()) {
            saveConfig = true;
            fun.init();
        }

        if (push == null) {
            saveConfig = true;
            push = PushObj.make();
        } else if (push.check()) {
            saveConfig = true;
            push.init();
        }

        if (cancel == null) {
            saveConfig = true;
            cancel = CancelObj.make();
        } else if (cancel.check()) {
            saveConfig = true;
            cancel.init();
        }

        return saveConfig;
    }

    public void init() {
        musicPlay = MusicPlayObj.make();
        addMusic = AddMusicObj.make();
        page = PageObj.make();
        lyric = LyricObj.make();
        vote = VoteObj.make();
        search = SearchObj.make();
        hud = HudObj.make();
        command = CommandObj.make();
        hudList = HudNameObj.make();
        custom = CustomObj.make();
        papi = PAPIObj.make();
        cost = CostObj.make();
        click = ClickObj.make();
        help = HelpObj.make();
        fun = FunObj.make();
        push = PushObj.make();
        cancel = CancelObj.make();
        version = AllMusic.messageVersion;
    }
}