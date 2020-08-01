package Color_yr.AllMusic.Message;

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
}