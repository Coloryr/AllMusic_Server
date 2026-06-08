package com.coloryr.allmusic.codec;

public class HudStatePosObj extends HudBasePosObj {
    public int color;
    public boolean shadow;
    public int gap;

    public HudStatePosObj copy() {
        HudStatePosObj obj = new HudStatePosObj();
        obj.x = x;
        obj.y = y;
        obj.alpha = alpha;
        obj.color = color;
        obj.enable = enable;
        obj.gap = gap;
        obj.shadow = shadow;
        obj.pos = pos;
        return obj;
    }
}
