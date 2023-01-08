package coloryr.allmusic.objs.message;

public class CommandOBJ {
    public String Error;
    public String NoPer;

    public CommandOBJ() {
        Error = "§d[AllMusic]§c参数错误，请输入/music help获取帮助";
        NoPer = "§d[AllMusic]§c你没有权限点歌";
    }

    public boolean check() {
        if (Error == null)
            return true;
        return NoPer == null;
    }
}
