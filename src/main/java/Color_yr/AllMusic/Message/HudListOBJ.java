package Color_yr.AllMusic.Message;

public class HudListOBJ {
    private String List;
    private String Lyric;
    private String Info;
    private String All;

    public HudListOBJ() {
        List = "待播放列表";
        Lyric = "歌词";
        Info = "歌曲信息";
        All = "所有位置";
    }

    public String getAll() {
        return All;
    }

    public String getInfo() {
        return Info;
    }

    public String getList() {
        return List;
    }

    public String getLyric() {
        return Lyric;
    }

    public String Get(String data) {
        if (data.equalsIgnoreCase("List")) {
            return List;
        } else if (data.equalsIgnoreCase("Lyric")) {
            return Lyric;
        } else if (data.equalsIgnoreCase("Info")) {
            return Info;
        }
        return null;
    }
}
