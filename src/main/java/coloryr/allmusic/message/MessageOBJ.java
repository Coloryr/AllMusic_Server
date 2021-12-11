package coloryr.allmusic.message;

public class MessageOBJ {
    private MusicPlayOBJ MusicPlay;
    private AddMusicOBJ AddMusic;
    private PageOBJ Page;
    private LyricOBJ Lyric;
    private VoteOBJ Vote;
    private SearchOBJ Search;
    private HudOBJ Hud;
    private HudListOBJ HudList;
    private CommandOBJ Command;
    private CustomOBJ Custom;
    private PAPIOBJ PAPI;
    private CostObj Cost;
    private ClickObj Click;
    private HelpObj Help;

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
    }

    public ClickObj getClick() {
        return Click;
    }

    public CostObj getCost() {
        return Cost;
    }

    public PAPIOBJ getPAPI() {
        return PAPI;
    }

    public CustomOBJ getCustom() {
        return Custom;
    }

    public HudListOBJ getHudList() {
        return HudList;
    }

    public CommandOBJ getCommand() {
        return Command;
    }

    public HudOBJ getHud() {
        return Hud;
    }

    public SearchOBJ getSearch() {
        return Search;
    }

    public VoteOBJ getVote() {
        return Vote;
    }

    public LyricOBJ getLyric() {
        return Lyric;
    }

    public MusicPlayOBJ getMusicPlay() {
        return MusicPlay;
    }

    public PageOBJ getPage() {
        return Page;
    }

    public AddMusicOBJ getAddMusic() {
        return AddMusic;
    }

    public HelpObj getHelp(){return Help;}

    public boolean check() {
        boolean saveConfig = false;
        if (MusicPlay == null) {
            saveConfig = true;
            MusicPlay = new MusicPlayOBJ();
        }
        if (AddMusic == null) {
            saveConfig = true;
            AddMusic = new AddMusicOBJ();
        }
        if (Page == null) {
            saveConfig = true;
            Page = new PageOBJ();
        }
        if (Lyric == null) {
            saveConfig = true;
            Lyric = new LyricOBJ();
        }
        if (Vote == null) {
            saveConfig = true;
            Vote = new VoteOBJ();
        }
        if (Search == null) {
            saveConfig = true;
            Search = new SearchOBJ();
        }
        if (Hud == null) {
            saveConfig = true;
            Hud = new HudOBJ();
        }
        if (HudList == null) {
            saveConfig = true;
            HudList = new HudListOBJ();
        }
        if (Command == null) {
            saveConfig = true;
            Command = new CommandOBJ();
        }
        if (Custom == null) {
            saveConfig = true;
            Custom = new CustomOBJ();
        }
        if (PAPI == null) {
            saveConfig = true;
            PAPI = new PAPIOBJ();
        }
        if (Cost == null) {
            saveConfig = true;
            Cost = new CostObj();
        }
        if (Click == null) {
            saveConfig = true;
            Click = new ClickObj();
        }
        if(Help == null)
        {
            saveConfig = true;
            Help = new HelpObj();
        }
        if(Help.check())
        {
            saveConfig = true;
        }
        return saveConfig;
    }
}