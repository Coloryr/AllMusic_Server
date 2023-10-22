package coloryr.allmusic.core.objs.message;

public class HudListObj {
    public String List;
    public String Lyric;
    public String Info;
    public String All;
    public String Pic;
    public String PicRoute;
    public String PicRouteSpeed;

    public boolean check() {
        if (List == null)
            return true;
        if (Lyric == null)
            return true;
        if (Info == null)
            return true;
        if (All == null)
            return true;
        if (PicRoute == null)
            return true;
        if (PicRouteSpeed == null)
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

    public void init() {
        List = "待播放列表";
        Lyric = "歌词";
        Info = "歌曲信息";
        Pic = "图片";
        All = "所有位置";
        PicRoute = "图片旋转";
        PicRouteSpeed = "图片旋转速度";
    }

    public static HudListObj make() {
        HudListObj obj = new HudListObj();
        obj.init();

        return obj;
    }
}
