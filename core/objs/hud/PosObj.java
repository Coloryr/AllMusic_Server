package com.coloryr.allmusic.server.core.objs.hud;

import com.coloryr.allmusic.server.core.objs.enums.HudDirType;

public class PosObj {
    public int x;
    public int y;
    public HudDirType dir;
    public int color;
    public boolean shadow;
    public boolean enable;

    public PosObj() {

    }

    public PosObj(int x, int y, HudDirType type, int color, boolean shadow, boolean enable) {
        this.x = x;
        this.y = y;
        this.dir = type;
        this.color = color;
        this.shadow = shadow;
        this.enable = enable;
    }

    public PosObj copy() {
        return new PosObj(this.x, this.y, this.dir, this.color, this.shadow, this.enable);
    }
}
