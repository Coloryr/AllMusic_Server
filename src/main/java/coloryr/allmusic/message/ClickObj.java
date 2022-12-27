package coloryr.allmusic.message;

public class ClickObj {
    public String This;
    public String Check;

    public ClickObj() {
        This = "§b[§n点我选择§r§b]";
        Check = "§b[§n点我查看§r§b]";
    }

    public boolean check() {
        if (This == null)
            return true;
        if (Check == null)
            return true;

        return false;
    }
}
