package Color_yr.ALLMusic.Message;

public class MessageOBJ {
    private MusicPlayOBJ MusicPlay;
    private AddMusicOBJ AddMusic;
    private PageOBJ Page;
    private LyricOBJ Lyric;
    private VoteOBJ Vote;
    private SearchOBJ Search;
    private VVOBJ VV;
    private CommandOBJ Command;

    public MessageOBJ() {
        MusicPlay = new MusicPlayOBJ();
        AddMusic = new AddMusicOBJ();
        Page = new PageOBJ();
        Lyric = new LyricOBJ();
        Vote = new VoteOBJ();
        Search = new SearchOBJ();
        VV = new VVOBJ();
        Command = new CommandOBJ();
    }

    public CommandOBJ getCommand() {
        return Command;
    }

    public VVOBJ getVV() {
        return VV;
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