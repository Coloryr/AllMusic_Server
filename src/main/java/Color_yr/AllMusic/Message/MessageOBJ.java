package Color_yr.AllMusic.Message;

public class MessageOBJ {
    private final MusicPlayOBJ MusicPlay;
    private final AddMusicOBJ AddMusic;
    private final PageOBJ Page;
    private final LyricOBJ Lyric;
    private final VoteOBJ Vote;
    private final SearchOBJ Search;
    private final HudOBJ Hud;
    private final HudListOBJ HudList;
    private CommandOBJ Command;

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