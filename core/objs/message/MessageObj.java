package coloryr.allmusic.core.objs.message;

import coloryr.allmusic.core.AllMusic;

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

    public boolean check() {
        boolean saveConfig = false;
        if (musicPlay == null || musicPlay.check()) {
            saveConfig = true;
            musicPlay = MusicPlayObj.make();
        }
        if (addMusic == null || addMusic.check()) {
            saveConfig = true;
            addMusic = AddMusicObj.make();
        }
        if (page == null || page.check()) {
            saveConfig = true;
            page = PageObj.make();
        }
        if (lyric == null || lyric.check()) {
            saveConfig = true;
            lyric = LyricObj.make();
        }
        if (vote == null || vote.check()) {
            saveConfig = true;
            vote = VoteObj.make();
        }
        if (search == null || search.check()) {
            saveConfig = true;
            search = SearchObj.make();
        }
        if (hud == null || hud.check()) {
            saveConfig = true;
            hud = HudObj.make();
        }
        if (hudList == null || hudList.check()) {
            saveConfig = true;
            hudList = HudNameObj.make();
        }
        if (command == null || command.check()) {
            saveConfig = true;
            command = CommandObj.make();
        }
        if (custom == null || custom.check()) {
            saveConfig = true;
            custom = CustomObj.make();
        }
        if (papi == null || papi.check()) {
            saveConfig = true;
            papi = PAPIObj.make();
        }
        if (cost == null || cost.check()) {
            saveConfig = true;
            cost = CostObj.make();
        }
        if (click == null || click.check()) {
            saveConfig = true;
            click = ClickObj.make();
        }
        if (help == null || help.check()) {
            saveConfig = true;
            help = HelpObj.make();
        }
        if (fun == null || fun.check()) {
            saveConfig = true;
            fun = FunObj.make();
        }
        if (push == null || push.check()) {
            saveConfig = true;
            push = PushObj.make();
        }
        if (cancel == null || cancel.check()) {
            saveConfig = true;
            cancel = CancelObj.make();
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

    public static MessageObj make() {
        MessageObj obj = new MessageObj();
        obj.init();

        return obj;
    }
}