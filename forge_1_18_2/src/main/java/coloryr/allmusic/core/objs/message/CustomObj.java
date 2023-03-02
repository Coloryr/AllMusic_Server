package coloryr.allmusic.core.objs.message;

public class CustomObj {
    public String Info;

    public CustomObj() {
        Info = "自定义音乐";
    }

    public boolean check() {
        return Info == null;
    }
}
