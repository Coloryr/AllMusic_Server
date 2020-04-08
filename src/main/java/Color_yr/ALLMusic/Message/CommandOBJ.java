package Color_yr.ALLMusic.Message;

public class CommandOBJ {
    private String Error;
    private String NoPer;

    public CommandOBJ() {
        Error = "§d[ALLMusic]§c参数错误，请输入/music help获取帮助";
        NoPer = "§d[ALLMusic]§c你没有权限点歌";
    }

    public String getNoPer() {
        return NoPer;
    }

    public String getError() {
        return Error;
    }
}
