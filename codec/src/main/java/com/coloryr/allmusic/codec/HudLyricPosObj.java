package com.coloryr.allmusic.codec;

public class HudLyricPosObj extends HudBasePosObj {
    public int color;
    public int gap;
    public boolean shadow;
    public int maxWidth;

    public HudLyricPosObj copy() {
        HudLyricPosObj obj = new HudLyricPosObj();
        obj.x = x;
        obj.y = y;
        obj.alpha = alpha;
        obj.color = color;
        obj.enable = enable;
        obj.maxWidth = maxWidth;
        obj.shadow = shadow;
        obj.gap = gap;
        obj.pos = pos;
        return obj;
    }
}
