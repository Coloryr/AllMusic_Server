package coloryr.allmusic.message;

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
    private String Trail;

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
        Trail = "§d[AllMusic]§2该音乐为试听音乐";
    }

    public boolean check() {
        boolean res = false;
        if (StopPlay == null)
            res = true;
        if (NoMusic == null)
            res = true;
        if (Play == null)
            res = true;
        if (NoPlay == null)
            res = true;
        if (ListMusic == null || ListMusic.check())
            res = true;
        if (NoCanPlay == null)
            res = true;
        if (NoPlayMusic == null)
            res = true;
        if (Switch == null)
            res = true;
        if (AddMusic == null)
            res = true;
        if (PlayerAdd == null)
            res = true;
        if (MusicInfo == null)
            res = true;
        if (Trail == null)
            res = true;

        return res;
    }

    public String getTrail() {
        return Trail;
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
