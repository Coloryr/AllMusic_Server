package coloryr.allmusic.core.objs.message;

import coloryr.allmusic.core.AllMusic;

public class MessageObj {
    public MusicPlayObj MusicPlay;
    public AddMusicObj AddMusic;
    public PageObj Page;
    public LyricObj Lyric;
    public VoteObj Vote;
    public SearchObj Search;
    public HudObj Hud;
    public HudListObj HudList;
    public CommandObj Command;
    public CustomObj Custom;
    public PAPIObj PAPI;
    public CostObj Cost;
    public ClickObj Click;
    public HelpObj Help;
    public FunObj Fun;
    public String Version;

    public boolean check() {
        boolean saveConfig = false;
        if (MusicPlay == null || MusicPlay.check()) {
            saveConfig = true;
            MusicPlay = MusicPlayObj.make();
        }
        if (AddMusic == null || AddMusic.check()) {
            saveConfig = true;
            AddMusic = AddMusicObj.make();
        }
        if (Page == null || Page.check()) {
            saveConfig = true;
            Page = PageObj.make();
        }
        if (Lyric == null || Lyric.check()) {
            saveConfig = true;
            Lyric = LyricObj.make();
        }
        if (Vote == null || Vote.check()) {
            saveConfig = true;
            Vote = VoteObj.make();
        }
        if (Search == null || Search.check()) {
            saveConfig = true;
            Search = SearchObj.make();
        }
        if (Hud == null || Hud.check()) {
            saveConfig = true;
            Hud = HudObj.make();
        }
        if (HudList == null || HudList.check()) {
            saveConfig = true;
            HudList = HudListObj.make();
        }
        if (Command == null || Command.check()) {
            saveConfig = true;
            Command = CommandObj.make();
        }
        if (Custom == null || Custom.check()) {
            saveConfig = true;
            Custom = CustomObj.make();
        }
        if (PAPI == null || PAPI.check()) {
            saveConfig = true;
            PAPI = PAPIObj.make();
        }
        if (Cost == null || Cost.check()) {
            saveConfig = true;
            Cost = CostObj.make();
        }
        if (Click == null || Click.check()) {
            saveConfig = true;
            Click = ClickObj.make();
        }
        if (Help == null || Help.check()) {
            saveConfig = true;
            Help = HelpObj.make();
        }
        if (Fun == null || Fun.check()) {
            saveConfig = true;
            Fun = FunObj.make();
        }
        return saveConfig;
    }

    public void init() {
        MusicPlay = MusicPlayObj.make();
        AddMusic = AddMusicObj.make();
        Page = PageObj.make();
        Lyric = LyricObj.make();
        Vote = VoteObj.make();
        Search = SearchObj.make();
        Hud = HudObj.make();
        Command = CommandObj.make();
        HudList = HudListObj.make();
        Custom = CustomObj.make();
        PAPI = PAPIObj.make();
        Cost = CostObj.make();
        Click = ClickObj.make();
        Help = HelpObj.make();
        Fun = FunObj.make();
        Version = AllMusic.messageVersion;
    }

    public static MessageObj make() {
        MessageObj obj = new MessageObj();
        obj.init();

        return obj;
    }
}