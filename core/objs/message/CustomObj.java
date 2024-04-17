package coloryr.allmusic.core.objs.message;

public class CustomObj {
    public String info;

    public boolean check() {
        return info == null;
    }

    public void init() {
        info = "自定义音乐";
    }

    public static CustomObj make() {
        CustomObj obj = new CustomObj();
        obj.init();

        return obj;
    }
}
