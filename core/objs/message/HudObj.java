package coloryr.allmusic.core.objs.message;

public class HudObj {
    public String State;
    public String Set;
    public String Reset;
    public String NoMusic;
    public String NoList;
    public String NoLyric;
    public String PicSize;
    public String PicRotate;
    public String PicRotateSpeed;
    public String Music;
    public String Lyric;
    public String List;
    public String Ktv;

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
        if (PicRotate == null)
            return true;
        if (PicRotateSpeed == null)
            return true;
        return Ktv == null;
    }

    public void init() {
        State = "§d[AllMusic]§e设置信息位置：%Hud%，状态：%State%";
        Set = "§d[AllMusic]§e已设置%Hud%的坐标为%x%, %y%";
        Reset = "§d[AllMusic]§eHud恢复至默认位置";
        NoMusic = "没有播放的音乐";
        NoList = "队列中无歌曲";
        NoLyric = "无歌词";
        PicSize = "§d[AllMusic]§e设置图片尺寸为%Size%";
        PicRotate = "§d[AllMusic]§e设置图片旋转为%State%";
        PicRotateSpeed = "§d[AllMusic]§e设置图片旋转速度为%Size%";
        Music = "%Name%  %AllTime%/%NowTime%\n%Author%\n%Alia%\n%Al%\nby: %Player%";
        Lyric = "%Lyric%\n%Tlyric%";
        Ktv = "§e%KLyric%§f%Lyric%§r\n%Tlyric%";
        List = "播放列表里面有%Size%首歌\n%List%";
    }

    public static HudObj make() {
        HudObj obj = new HudObj();
        obj.init();

        return obj;
    }
}
