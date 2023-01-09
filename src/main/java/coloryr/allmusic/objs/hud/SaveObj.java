package coloryr.allmusic.objs.hud;

public class SaveObj {
    public PosObj List;
    public PosObj Lyric;
    public PosObj Info;
    public PosObj Pic;
    public int PicSize;
    public int PicRotateSpeed;
    public boolean EnablePicRotate;
    public boolean EnableList;
    public boolean EnableLyric;
    public boolean EnableInfo;
    public boolean EnablePic;

    public SaveObj() {
        EnableList = true;
        EnableLyric = true;
        EnableInfo = true;
        EnablePic = true;
        EnablePicRotate = true;
        PicRotateSpeed = 10;
        List = new PosObj();
        Lyric = new PosObj();
        Info = new PosObj();
        Pic = new PosObj();
        PicSize = 70;
    }

    public SaveObj copy() {
        SaveObj obj1 = new SaveObj();
        obj1.EnableInfo = this.EnableInfo;
        obj1.EnableList = this.EnableList;
        obj1.EnableLyric = this.EnableLyric;
        obj1.EnablePic = this.EnablePic;
        obj1.Info = this.Info.copy();
        obj1.List = this.List.copy();
        obj1.Lyric = this.Lyric.copy();
        obj1.Pic = this.Pic.copy();
        obj1.PicSize = this.PicSize;
        obj1.EnablePicRotate = this.EnablePicRotate;
        obj1.PicRotateSpeed = this.PicRotateSpeed;
        return obj1;
    }
}