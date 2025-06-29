package com.coloryr.allmusic.server.core.objs.config;

import com.coloryr.allmusic.server.core.objs.enums.HudDirType;
import com.coloryr.allmusic.server.core.objs.hud.PosObj;

public class SaveObj {
    public PosObj list;
    public PosObj lyric;
    public PosObj info;
    public PosObj pic;
    public int picRotateSpeed;

    public static SaveObj make() {
        SaveObj obj = new SaveObj();
        obj.init();

        return obj;
    }

    public SaveObj copy() {
        SaveObj obj1 = new SaveObj();
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
            list = new PosObj(2, 74, HudDirType.TOP_LEFT, 0xffffffff, true, true);
        }
        if (lyric == null) {
            save = true;
            lyric = new PosObj(74, 53, HudDirType.TOP_LEFT, 0xffffffff, true, true);
        }
        if (info == null) {
            save = true;
            info = new PosObj(74, 2, HudDirType.TOP_LEFT, 0xffffffff, true, true);
        }
        if (pic == null) {
            save = true;
            pic = new PosObj(2, 2, HudDirType.TOP_LEFT, 70, true, true);
        }
        return save;
    }

    public void init() {
        picRotateSpeed = 30;
        list = new PosObj(2, 74, HudDirType.TOP_LEFT, 0xffffffff, true, true);
        lyric = new PosObj(74, 53, HudDirType.TOP_LEFT, 0xffffffff, true, true);
        info = new PosObj(74, 2, HudDirType.TOP_LEFT, 0xffffffff, true, true);
        pic = new PosObj(2, 2, HudDirType.TOP_LEFT, 70, true, true);
    }
}