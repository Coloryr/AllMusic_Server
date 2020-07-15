package Color_yr.AllMusic.Message;

public class HudOBJ {
    private String State;
    private String Set;
    private String NoMusic;
    private String NoList;
    private String NoLyric;

    public HudOBJ() {
        State = "§d[ALLMusic]§2设置信息位置：%Hud%，状态：%State%";
        Set = "§d[ALLMusic]§2已设置%Hud%的坐标为%x%, %y%";
        NoMusic = "没有播放的音乐";
        NoList = "队列中无歌曲";
        NoLyric = "无歌词";
    }

    public String getNoLyric() {
        return NoLyric;
    }

    public String getNoList() {
        return NoList;
    }

    public String getNoMusic() {
        return NoMusic;
    }

    public String getSet() {
        return Set;
    }

    public String getState() {
        return State;
    }
}
