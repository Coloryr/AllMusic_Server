package Color_yr.ALLMusic.Message;

public class MusicPlayOBJ {
    private String StopPlay;
    private String NoMusic;
    private String Play;
    private String NoPlay;
    private ListMusicOBJ ListMusic;
    private String NoCanPlay;
    private String NoPlayMusic;
    private String Switch;
    private String AddMusic;
    private String PlayerAdd;
    private String MusicInfo;

    public MusicPlayOBJ() {
        StopPlay = "§d[ALLMusic]§2已停止%PlayerName%的音乐播放";
        NoMusic = "§d[ALLMusic]§2无正在播放的歌曲";
        Play = "§d[ALLMusic]§2正在播放：%MusicName% | %MusicAuthor% | %MusicAl% | %MusicAlia% | by: %PlayerName%";
        NoPlay = "§d[ALLMusic]§2队列中无歌曲";
        ListMusic = new ListMusicOBJ();
        NoCanPlay="§d[ALLMusic]§c无法播放歌曲%MusicID%可能该歌曲为VIP歌曲";
        NoPlayMusic = "§d[ALLMusic]§2你不会再收到点歌了！想要再次参与点歌就点一首歌吧！";
        Switch = "§d[ALLMusic]§2切换到玩家歌曲";
        AddMusic = "§d[ALLMusic]§2音乐列表添加%MusicName% | %MusicAuthor% | %MusicAl% | %MusicAlia%";
        PlayerAdd = "§d[ALLMusic]§2%PlayerName%点歌%MusicID%";
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
