package coloryr.allmusic.core.objs.message;

public class MusicPlayObj {
    public String StopPlay;
    public String NoMusic;
    public String Play;
    public String NoPlay;
    public ListMusicObj ListMusic;
    public String NoCanPlay;
    public String NoPlayMusic;
    public String Switch;
    public String AddMusic;
    public String CheckMusic;
    public String MusicInfo;
    public String Trail;
    public String Cancel;

    public boolean check() {
        boolean res = StopPlay == null;
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

    public void init() {
        StopPlay = "§d[AllMusic]§e已停止你的音乐播放";
        NoMusic = "§d[AllMusic]§e无正在播放的歌曲";
        Play = "§d[AllMusic]§e正在播放：%MusicName% | %MusicAuthor% | %MusicAl% | %MusicAlia% | by: %PlayerName%";
        NoPlay = "§d[AllMusic]§e队列中无歌曲";
        ListMusic = ListMusicObj.make();
        NoCanPlay = "§d[AllMusic]§c无法播放歌曲%MusicID%可能该歌曲为VIP歌曲";
        NoPlayMusic = "§d[AllMusic]§e你不会再收到点歌了！想要再次参与点歌就点一首歌吧！";
        Switch = "§d[AllMusic]§e切换到玩家歌曲";
        AddMusic = "§d[AllMusic]§e音乐列表添加%MusicName% | %MusicAuthor% | %MusicAl% | %MusicAlia%";
        CheckMusic = "§d[AllMusic]§e正在解析歌曲%MusicID%";
        MusicInfo = "%MusicName% | %MusicAuthor% | %MusicAl% | %MusicAlia% | by: %PlayerName%";
        Trail = "§d[AllMusic]§e该音乐为试听音乐";
        Cancel = "§d[AllMusic]§e播放被取消";
    }

    public static MusicPlayObj make() {
        MusicPlayObj obj = new MusicPlayObj();
        obj.init();

        return obj;
    }
}
