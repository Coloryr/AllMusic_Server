package coloryr.allmusic.message;

public class CustomOBJ {
    public String Info;

    public CustomOBJ() {
        Info = "自定义音乐";
    }

    public boolean check() {
        if (Info == null)
            return true;

        return false;
    }
}
