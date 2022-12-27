package coloryr.allmusic.message;

public class CommandOBJ {
    private String Error;
    private String NoPer;

    public CommandOBJ() {
        Error = "§d[AllMusic]§c参数错误，请输入/music help获取帮助";
        NoPer = "§d[AllMusic]§c你没有权限点歌";
    }

    public boolean check() {
        if (Error == null)
            return true;
        if (NoPer == null)
            return true;

        return false;
    }

    public String getNoPer() {
        return NoPer;
    }

    public String getError() {
        return Error;
    }
}
