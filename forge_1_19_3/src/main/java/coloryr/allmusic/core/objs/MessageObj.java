package coloryr.allmusic.core.objs;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.objs.message.SearchObj;
import coloryr.allmusic.core.objs.message.*;

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

    public MessageObj() {
        MusicPlay = new MusicPlayObj();
        AddMusic = new AddMusicObj();
        Page = new PageObj();
        Lyric = new LyricObj();
        Vote = new VoteObj();
        Search = new SearchObj();
        Hud = new HudObj();
        Command = new CommandObj();
        HudList = new HudListObj();
        Custom = new CustomObj();
        PAPI = new PAPIObj();
        Cost = new CostObj();
        Click = new ClickObj();
        Help = new HelpObj();
        Fun = new FunObj();
        Version = AllMusic.configVersion;
    }

    public boolean check() {
        boolean saveConfig = false;
        if (MusicPlay == null || MusicPlay.check()) {
            saveConfig = true;
            MusicPlay = new MusicPlayObj();
        }
        if (AddMusic == null || AddMusic.check()) {
            saveConfig = true;
            AddMusic = new AddMusicObj();
        }
        if (Page == null || Page.check()) {
            saveConfig = true;
            Page = new PageObj();
        }
        if (Lyric == null || Lyric.check()) {
            saveConfig = true;
            Lyric = new LyricObj();
        }
        if (Vote == null || Vote.check()) {
            saveConfig = true;
            Vote = new VoteObj();
        }
        if (Search == null || Search.check()) {
            saveConfig = true;
            Search = new SearchObj();
        }
        if (Hud == null || Hud.check()) {
            saveConfig = true;
            Hud = new HudObj();
        }
        if (HudList == null || HudList.check()) {
            saveConfig = true;
            HudList = new HudListObj();
        }
        if (Command == null || Command.check()) {
            saveConfig = true;
            Command = new CommandObj();
        }
        if (Custom == null || Custom.check()) {
            saveConfig = true;
            Custom = new CustomObj();
        }
        if (PAPI == null || PAPI.check()) {
            saveConfig = true;
            PAPI = new PAPIObj();
        }
        if (Cost == null || Cost.check()) {
            saveConfig = true;
            Cost = new CostObj();
        }
        if (Click == null || Click.check()) {
            saveConfig = true;
            Click = new ClickObj();
        }
        if (Help == null || Help.check()) {
            saveConfig = true;
            Help = new HelpObj();
        }
        if (Fun == null || Fun.check()) {
            saveConfig = true;
            Fun = new FunObj();
        }
        return saveConfig;
    }
}