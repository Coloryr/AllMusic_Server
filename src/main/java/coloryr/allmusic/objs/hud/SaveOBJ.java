package coloryr.allmusic.objs.hud;

public class SaveOBJ {
    public PosOBJ List;
    public PosOBJ Lyric;
    public PosOBJ Info;
    public PosOBJ Pic;
    public int PicSize;
    public boolean EnableList;
    public boolean EnableLyric;
    public boolean EnableInfo;
    public boolean EnablePic;

    public SaveOBJ() {
        EnableList = true;
        EnableLyric = true;
        EnableInfo = true;
        EnablePic = true;
        List = new PosOBJ();
        Lyric = new PosOBJ();
        Info = new PosOBJ();
        Pic = new PosOBJ();
        PicSize = 70;
    }

    public SaveOBJ copy() {
        SaveOBJ obj1 = new SaveOBJ();
        obj1.EnableInfo = this.EnableInfo;
        obj1.EnableList = this.EnableList;
        obj1.EnableLyric = this.EnableLyric;
        obj1.EnablePic = this.EnablePic;
        obj1.Info = this.Info.copy();
        obj1.List = this.List.copy();
        obj1.Lyric = this.Lyric.copy();
        obj1.Pic = this.Pic.copy();
        obj1.PicSize = this.PicSize;
        return obj1;
    }
}