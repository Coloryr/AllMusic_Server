package coloryr.allmusic.objs.message;

public class HudListOBJ {
    public String List;
    public String Lyric;
    public String Info;
    public String All;
    public String Pic;

    public HudListOBJ() {
        List = "待播放列表";
        Lyric = "歌词";
        Info = "歌曲信息";
        Pic = "图片";
        All = "所有位置";
    }

    public boolean check() {
        if (List == null)
            return true;
        if (Lyric == null)
            return true;
        if (Info == null)
            return true;
        if (All == null)
            return true;
        return Pic == null;
    }

    public String Get(String data) {
        if (data.equalsIgnoreCase("List")) {
            return List;
        } else if (data.equalsIgnoreCase("Lyric")) {
            return Lyric;
        } else if (data.equalsIgnoreCase("Info")) {
            return Info;
        } else if (data.equalsIgnoreCase("Pic")) {
            return Pic;
        }
        return null;
    }
}
