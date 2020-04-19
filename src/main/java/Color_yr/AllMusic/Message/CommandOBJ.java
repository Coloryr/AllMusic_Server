package Color_yr.AllMusic.Message;

public class CommandOBJ {
    private final String Error;
    private final String NoPer;

    public CommandOBJ() {
        Error = "§d[AllMusic]§c参数错误，请输入/music help获取帮助";
        NoPer = "§d[AllMusic]§c你没有权限点歌";
    }

    public String getNoPer() {
        return NoPer;
    }

    public String getError() {
        return Error;
    }
}
