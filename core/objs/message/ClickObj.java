package coloryr.allmusic.core.objs.message;

public class ClickObj {
    public String clickRun;
    public String clickCheck;

    public boolean check() {
        if (clickRun == null)
            return true;
        return clickCheck == null;
    }

    public void init() {
        clickRun = "§b[§n点我选择§r§b]";
        clickCheck = "§b[§n点我查看§r§b]";
    }

    public static ClickObj make() {
        ClickObj obj = new ClickObj();
        obj.init();

        return obj;
    }
}
