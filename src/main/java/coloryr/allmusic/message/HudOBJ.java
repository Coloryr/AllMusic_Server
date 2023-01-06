package coloryr.allmusic.message;

public class HudOBJ {
    public String State;
    public String Set;
    public String Reset;
    public String NoMusic;
    public String NoList;
    public String NoLyric;
    public String PicSize;
    public String Music;
    public String Lyric;
    public String List;

    public HudOBJ() {
        State = "§d[ALLMusic]§2设置信息位置：%Hud%，状态：%State%";
        Set = "§d[ALLMusic]§2已设置%Hud%的坐标为%x%, %y%";
        Reset = "§d[ALLMusic]§2Hud恢复至默认位置";
        NoMusic = "没有播放的音乐";
        NoList = "队列中无歌曲";
        NoLyric = "无歌词";
        PicSize = "§d[ALLMusic]§2设置图片尺寸为%Size%";
        Music = "%Name%  %AllTime%/%NowTime%\n%Author%\n%Alia%\n%Al%\nby: %Player%";
        Lyric = "%Lyric%\n%Tlyric%";
        List = "播放列表里面有%Size%首歌\n%List%";
    }

    public boolean check() {
        if (State == null)
            return true;
        if (Set == null)
            return true;
        if (Reset == null)
            return true;
        if (NoMusic == null)
            return true;
        if (NoList == null)
            return true;
        if (NoLyric == null)
            return true;
        if (PicSize == null)
            return true;
        if (Music == null)
            return true;
        if (Lyric == null)
            return true;
        if (List == null)
            return true;

        return false;
    }
}
