package coloryr.allmusic.objs.message;

public class MusicPlayOBJ {
    public String StopPlay;
    public String NoMusic;
    public String Play;
    public String NoPlay;
    public ListMusicOBJ ListMusic;
    public String NoCanPlay;
    public String NoPlayMusic;
    public String Switch;
    public String AddMusic;
    public String CheckMusic;
    public String MusicInfo;
    public String Trail;
    public String Cancel;

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
        CheckMusic = "§d[AllMusic]§2正在解析歌曲%MusicID%";
        MusicInfo = "%MusicName% | %MusicAuthor% | %MusicAl% | %MusicAlia% | by: %PlayerName%";
        Trail = "§d[AllMusic]§2该音乐为试听音乐";
        Cancel = "§d[AllMusic]§e播放被取消";
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
        if (CheckMusic == null)
            res = true;
        if (MusicInfo == null)
            res = true;
        if (Trail == null)
            res = true;
        if (Cancel == null)
            res = true;

        return res;
    }
}
