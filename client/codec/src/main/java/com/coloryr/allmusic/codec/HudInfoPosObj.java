package com.coloryr.allmusic.codec;

public class HudInfoPosObj extends HudBasePosObj {
    public int color;
    public boolean shadow;
    public int maxWidth;
    public int gap;
    public boolean loop;

    public HudInfoPosObj copy() {
        HudInfoPosObj obj = new HudInfoPosObj();
        obj.x = x;
        obj.y = y;
        obj.alpha = alpha;
        obj.loop = loop;
        obj.color = color;
        obj.enable = enable;
        obj.gap = gap;
        obj.maxWidth = maxWidth;
        obj.shadow = shadow;
        obj.pos = pos;
        return obj;
    }
}
