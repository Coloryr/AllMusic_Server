package Color_yr.AllMusic.Message;

public class HudOBJ {
    private final String State;
    private final String Set;

    public HudOBJ() {
        State = "§d[ALLMusic]§2设置信息位置：%Hud%，状态：%State%";
        Set = "§d[ALLMusic]§2已设置%Hud%的坐标为%x%, %y%";
    }

    public String getSet() {
        return Set;
    }

    public String getState() {
        return State;
    }
}
