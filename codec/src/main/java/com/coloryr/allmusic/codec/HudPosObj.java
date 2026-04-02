package com.coloryr.allmusic.codec;

public class HudPosObj {
    public HudItemPosObj list;
    public HudItemPosObj lyric;
    public HudItemPosObj info;
    public HudItemPosObj pic;
    public int picRotateSpeed;

    public static HudPosObj make() {
        HudPosObj obj = new HudPosObj();
        obj.init();

        return obj;
    }

    public HudPosObj copy() {
        HudPosObj obj1 = new HudPosObj();
        obj1.info = this.info.copy();
        obj1.list = this.list.copy();
        obj1.lyric = this.lyric.copy();
        obj1.pic = this.pic.copy();
        obj1.picRotateSpeed = this.picRotateSpeed;
        return obj1;
    }

    public boolean check() {
        boolean save = false;
        if (list == null) {
            save = true;
            list = new HudItemPosObj(2, 74, HudDirType.TOP_LEFT, 0xffffffff, true, true);
        }
        if (lyric == null) {
            save = true;
            lyric = new HudItemPosObj(74, 53, HudDirType.TOP_LEFT, 0xffffffff, true, true);
        }
        if (info == null) {
            save = true;
            info = new HudItemPosObj(74, 2, HudDirType.TOP_LEFT, 0xffffffff, true, true);
        }
        if (pic == null) {
            save = true;
            pic = new HudItemPosObj(2, 2, HudDirType.TOP_LEFT, 70, true, true);
        }
        return save;
    }

    public void init() {
        picRotateSpeed = 30;
        list = new HudItemPosObj(2, 74, HudDirType.TOP_LEFT, 0xffffffff, true, true);
        lyric = new HudItemPosObj(74, 53, HudDirType.TOP_LEFT, 0xffffffff, true, true);
        info = new HudItemPosObj(74, 2, HudDirType.TOP_LEFT, 0xffffffff, true, true);
        pic = new HudItemPosObj(2, 2, HudDirType.TOP_LEFT, 70, true, true);
    }
}