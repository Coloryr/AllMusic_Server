package Color_yr.ALLMusic.Message;

public class MessageOBJ {
    private MusicPlayOBJ MusicPlay;
    private AddMusicOBJ AddMusic;
    private PageOBJ Page;
    private LyricOBJ Lyric;
    private VoteOBJ Vote;

    public MessageOBJ() {
        MusicPlay = new MusicPlayOBJ();
        AddMusic = new AddMusicOBJ();
        Page = new PageOBJ();
        Lyric = new LyricOBJ();
        Vote = new VoteOBJ();
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