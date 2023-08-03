package coloryr.allmusic.core.objs.config;

import coloryr.allmusic.core.objs.hud.PosObj;

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

    public boolean check() {
        boolean save = false;
        if (List == null) {
            save = true;
            List = new PosObj(2, 74);
        }
        if (Lyric == null) {
            save = true;
            Lyric = new PosObj(74, 53);
        }
        if (Info == null) {
            save = true;
            Info = new PosObj(74, 2);
        }
        if (Pic == null) {
            save = true;
            Pic = new PosObj(2, 2);
        }
        return save;
    }

    public void init() {
        EnableList = true;
        EnableLyric = true;
        EnableInfo = true;
        EnablePic = true;
        EnablePicRotate = true;
        PicRotateSpeed = 10;
        List = new PosObj(2, 74);
        Lyric = new PosObj(74, 53);
        Info = new PosObj(74, 2);
        Pic = new PosObj(2, 2);
        PicSize = 70;
    }

    public static SaveObj make() {
        SaveObj obj = new SaveObj();
        obj.init();

        return obj;
    }
}