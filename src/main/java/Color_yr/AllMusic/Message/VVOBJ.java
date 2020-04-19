package Color_yr.AllMusic.Message;

public class VVOBJ {
    private final String State;
    private final String Set;

    public VVOBJ() {
        State = "§d[AllMusic]§2当前VV状态：%State%";
        Set = "§d[AllMusic]§2已设置";
    }

    public String getSet() {
        return Set;
    }

    public String getState() {
        return State;
    }
}
