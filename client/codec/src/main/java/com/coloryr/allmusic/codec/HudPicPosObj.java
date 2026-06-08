package com.coloryr.allmusic.codec;

public class HudPicPosObj extends HudBasePosObj {
    public int size;
    public boolean rotate;
    public int speed;

    public HudPicPosObj copy() {
        HudPicPosObj obj = new HudPicPosObj();
        obj.x = x;
        obj.y = y;
        obj.pos = pos;
        obj.size = size;
        obj.rotate = rotate;
        obj.enable = enable;
        obj.alpha = alpha;
        obj.speed = speed;
        return obj;
    }
}
