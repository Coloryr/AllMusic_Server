package coloryr.allmusic.message;

public class HudOBJ {
    private String State;
    private String Set;
    private String Reset;
    private String NoMusic;
    private String NoList;
    private String NoLyric;

    public HudOBJ() {
        State = "§d[ALLMusic]§2设置信息位置：%Hud%，状态：%State%";
        Set = "§d[ALLMusic]§2已设置%Hud%的坐标为%x%, %y%";
        Reset = "§d[ALLMusic]§2Hud恢复至默认位置";
        NoMusic = "没有播放的音乐";
        NoList = "队列中无歌曲";
        NoLyric = "无歌词";
    }

    public boolean check(){
        if(State == null)
            return true;
        if(Set == null)
            return true;
        if(Reset == null)
            return true;
        if(NoMusic == null)
            return true;
        if(NoList == null)
            return true;
        if(NoLyric == null)
            return true;

        return false;
    }

    public String getReset() {
        return Reset;
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
