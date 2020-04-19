package Color_yr.AllMusic.Message;

public class MusicPlayOBJ {
    private final String StopPlay;
    private final String NoMusic;
    private final String Play;
    private final String NoPlay;
    private final ListMusicOBJ ListMusic;
    private final String NoCanPlay;
    private final String NoPlayMusic;
    private final String Switch;
    private final String AddMusic;
    private final String PlayerAdd;
    private final String MusicInfo;

    public MusicPlayOBJ() {
        StopPlay = "§d[AllMusic]§2已停止你的音乐播放";
        NoMusic = "§d[AllMusic]§2无正在播放的歌曲";
        Play = "§d[AllMusic]§2正在播放：%MusicName% | %MusicAuthor% | %MusicAl% | %MusicAlia% | by: %PlayerName%";
        NoPlay = "§d[AllMusic]§2队列中无歌曲";
        ListMusic = new ListMusicOBJ();
        NoCanPlay = "§d[AllMusic]§c无法播放歌曲%MusicID%可能该歌曲为VIP歌曲";
        NoPlayMusic = "§d[AllMusic]§2你不会再收到点歌了！想要再次参与点歌就点一首歌吧！";
        Switch = "§d[AllMusic]§2切换到玩家歌曲";
        AddMusic = "§d[AllMusic]§2音乐列表添加%MusicName% | %MusicAuthor% | %MusicAl% | %MusicAlia%";
        PlayerAdd = "§d[AllMusic]§2%PlayerName%点歌%MusicID%";
        MusicInfo = "%MusicName% | %MusicAuthor% | %MusicAl% | %MusicAlia% | by: %PlayerName%";
    }

    public String getMusicInfo() {
        return MusicInfo;
    }

    public String getPlayerAdd() {
        return PlayerAdd;
    }

    public String getAddMusic() {
        return AddMusic;
    }

    public String getSwitch() {
        return Switch;
    }

    public String getNoPlayMusic() {
        return NoPlayMusic;
    }

    public String getNoCanPlay() {
        return NoCanPlay;
    }

    public ListMusicOBJ getListMusic() {
        return ListMusic;
    }

    public String getNoPlay() {
        return NoPlay;
    }

    public String getPlay() {
        return Play;
    }

    public String getNoMusic() {
        return NoMusic;
    }

    public String getStopPlay() {
        return StopPlay;
    }
}
