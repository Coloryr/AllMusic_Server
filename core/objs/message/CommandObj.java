package coloryr.allmusic.core.objs.message;

public class CommandObj {
    public String Error;
    public String NoPer;

    public boolean check() {
        if (Error == null)
            return true;
        return NoPer == null;
    }

    public void init() {
        Error = "§d[AllMusic]§c参数错误，请输入/music help获取帮助";
        NoPer = "§d[AllMusic]§c你没有权限点歌";
    }

    public static CommandObj make() {
        CommandObj obj = new CommandObj();
        obj.init();

        return obj;
    }
}
