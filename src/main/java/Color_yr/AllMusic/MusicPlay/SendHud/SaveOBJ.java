package Color_yr.AllMusic.MusicPlay.SendHud;

public class SaveOBJ {
    private PosOBJ List;
    private PosOBJ Lyric;
    private PosOBJ Info;
    private boolean EnableList;
    private boolean EnableLyric;
    private boolean EnableInfo;

    public SaveOBJ() {
        EnableList = true;
        EnableLyric = true;
        EnableInfo = true;
        List = new PosOBJ(2, 80);
        Lyric = new PosOBJ(2, 55);
        Info = new PosOBJ(2, 2);
    }

    public SaveOBJ copy() {
        SaveOBJ saveOBJ = new SaveOBJ();
        saveOBJ.setEnableInfo(this.EnableInfo);
        saveOBJ.setEnableList(this.EnableList);
        saveOBJ.setEnableLyric(this.EnableLyric);
        saveOBJ.setInfo(this.Info.copy());
        saveOBJ.setList(this.List.copy());
        saveOBJ.setLyric(this.Lyric.copy());
        return saveOBJ;
    }

    public boolean isEnableInfo() {
        return EnableInfo;
    }

    public void setEnableInfo(boolean enableInfo) {
        EnableInfo = enableInfo;
    }

    public boolean isEnableLyric() {
        return EnableLyric;
    }

    public void setEnableLyric(boolean enableLyric) {
        EnableLyric = enableLyric;
    }

    public boolean isEnableList() {
        return EnableList;
    }

    public void setEnableList(boolean enableList) {
        EnableList = enableList;
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