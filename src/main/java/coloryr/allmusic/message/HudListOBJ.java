package coloryr.allmusic.message;

public class HudListOBJ {
    private String List;
    private String Lyric;
    private String Info;
    private String All;
    private String Pic;

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
        if (Pic == null)
            return true;

        return false;
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

    public String getPic() {
        return Pic;
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
