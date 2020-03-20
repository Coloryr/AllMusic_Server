package Color_yr.ALLMusic.Message;

public class CheckOBJ {
    private String NoMOD;
    private String MOD;

    public CheckOBJ() {
        NoMOD = "§d[ALLMusic]§2你没有安装AllMusic配套mod，点歌将被禁用";
        MOD = "§d[ALLMusic]§2AllMusic已启用";
    }

    public String getMOD() {
        return MOD;
    }

    public String getNoMOD() {
        return NoMOD;
    }
}
