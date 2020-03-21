package Color_yr.ALLMusic.Side.SideBukkit;

public class VVSaveOBJ {
    private PosOBJ List;
    private PosOBJ Lyric;
    private PosOBJ Info;
    private boolean Enable;

    public VVSaveOBJ() {
        Enable = true;
        List = new PosOBJ(2, 80);
        Lyric = new PosOBJ(2, 325);
        Info = new PosOBJ(2, 2);
    }

    public boolean isEnable() {
        return Enable;
    }

    public void setEnable(boolean enable) {
        Enable = enable;
    }

    public PosOBJ getInfo() {
        return Info;
    }

    public void setInfo(PosOBJ info) {
        Info = info;
    }

    public PosOBJ getList() {
        return List;
    }

    public void setList(PosOBJ list) {
        List = list;
    }

    public PosOBJ getLyric() {
        return Lyric;
    }

    public void setLyric(PosOBJ lyric) {
        Lyric = lyric;
    }
}