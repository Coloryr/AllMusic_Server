package coloryr.allmusic.message;

import coloryr.allmusic.AllMusic;

public class MessageOBJ {
    public MusicPlayOBJ MusicPlay;
    public AddMusicOBJ AddMusic;
    public PageOBJ Page;
    public LyricOBJ Lyric;
    public VoteOBJ Vote;
    public SearchOBJ Search;
    public HudOBJ Hud;
    public HudListOBJ HudList;
    public CommandOBJ Command;
    public CustomOBJ Custom;
    public PAPIOBJ PAPI;
    public CostObj Cost;
    public ClickObj Click;
    public HelpObj Help;
    public String Version;

    public MessageOBJ() {
        MusicPlay = new MusicPlayOBJ();
        AddMusic = new AddMusicOBJ();
        Page = new PageOBJ();
        Lyric = new LyricOBJ();
        Vote = new VoteOBJ();
        Search = new SearchOBJ();
        Hud = new HudOBJ();
        Command = new CommandOBJ();
        HudList = new HudListOBJ();
        Custom = new CustomOBJ();
        PAPI = new PAPIOBJ();
        Cost = new CostObj();
        Click = new ClickObj();
        Help = new HelpObj();
        Version = AllMusic.configVersion;
    }

    public boolean check() {
        boolean saveConfig = false;
        if (MusicPlay == null || MusicPlay.check()) {
            saveConfig = true;
            MusicPlay = new MusicPlayOBJ();
        }
        if (AddMusic == null || AddMusic.check()) {
            saveConfig = true;
            AddMusic = new AddMusicOBJ();
        }
        if (Page == null || Page.check()) {
            saveConfig = true;
            Page = new PageOBJ();
        }
        if (Lyric == null || Lyric.check()) {
            saveConfig = true;
            Lyric = new LyricOBJ();
        }
        if (Vote == null || Vote.check()) {
            saveConfig = true;
            Vote = new VoteOBJ();
        }
        if (Search == null || Search.check()) {
            saveConfig = true;
            Search = new SearchOBJ();
        }
        if (Hud == null || Hud.check()) {
            saveConfig = true;
            Hud = new HudOBJ();
        }
        if (HudList == null || HudList.check()) {
            saveConfig = true;
            HudList = new HudListOBJ();
        }
        if (Command == null || Command.check()) {
            saveConfig = true;
            Command = new CommandOBJ();
        }
        if (Custom == null || Custom.check()) {
            saveConfig = true;
            Custom = new CustomOBJ();
        }
        if (PAPI == null || PAPI.check()) {
            saveConfig = true;
            PAPI = new PAPIOBJ();
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
        return saveConfig;
    }
}