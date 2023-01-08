package coloryr.allmusic.objs.message;

public class CustomOBJ {
    public String Info;

    public CustomOBJ() {
        Info = "自定义音乐";
    }

    public boolean check() {
        return Info == null;
    }
}
