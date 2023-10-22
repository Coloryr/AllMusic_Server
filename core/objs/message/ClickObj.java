package coloryr.allmusic.core.objs.message;

public class ClickObj {
    public String This;
    public String Check;

    public boolean check() {
        if (This == null)
            return true;
        return Check == null;
    }

    public void init() {
        This = "§b[§n点我选择§r§b]";
        Check = "§b[§n点我查看§r§b]";
    }

    public static ClickObj make() {
        ClickObj obj = new ClickObj();
        obj.init();

        return obj;
    }
}
