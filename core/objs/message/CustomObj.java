package coloryr.allmusic.core.objs.message;

public class CustomObj {
    public String Info;

    public boolean check() {
        return Info == null;
    }

    public void init() {
        Info = "自定义音乐";
    }

    public static CustomObj make() {
        CustomObj obj = new CustomObj();
        obj.init();

        return obj;
    }
}
