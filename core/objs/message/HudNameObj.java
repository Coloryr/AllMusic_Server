package com.coloryr.allmusic.server.core.objs.message;

import com.coloryr.allmusic.server.core.objs.enums.HudDirType;
import com.coloryr.allmusic.server.core.objs.enums.HudType;

public class HudNameObj {
    public String list;
    public String lyric;
    public String info;
    public String all;
    public String pic;
    public String picRoute;
    public String picSpeed;
    public String enable;
    public String disable;
    public String pos1;
    public String pos2;
    public String pos3;
    public String pos4;
    public String pos5;
    public String pos6;
    public String pos7;
    public String pos8;
    public String pos9;

    public static HudNameObj make() {
        HudNameObj obj = new HudNameObj();
        obj.init();

        return obj;
    }

    public boolean check() {
        if (list == null)
            return true;
        if (lyric == null)
            return true;
        if (info == null)
            return true;
        if (all == null)
            return true;
        if (picRoute == null)
            return true;
        if (picSpeed == null)
            return true;
        if (enable == null)
            return true;
        if (pos1 == null)
            return true;
        if (pos2 == null)
            return true;
        if (pos3 == null)
            return true;
        if (pos4 == null)
            return true;
        if (pos5 == null)
            return true;
        if (pos6 == null)
            return true;
        if (pos7 == null)
            return true;
        if (pos8 == null)
            return true;
        if (pos9 == null)
            return true;
        return pic == null;
    }

    public String getHud(HudType data) {
        if (data == null) {
            return all;
        }
        switch (data) {
            case PIC:
                return pic;
            case INFO:
                return info;
            case LYRIC:
                return lyric;
            case LIST:
                return list;
        }
        return null;
    }

    public String getDir(HudDirType data) {
        if (data == null) {
            return all;
        }
        switch (data) {
            case TOP_LEFT:
                return pos1;
            case TOP_CENTER:
                return pos2;
            case TOP_RIGHT:
                return pos3;
            case LEFT:
                return pos4;
            case CENTER:
                return pos5;
            case RIGHT:
                return pos6;
            case BOTTOM_LEFT:
                return pos7;
            case BOTTOM_CENTER:
                return pos8;
            case BOTTOM_RIGHT:
                return pos9;
        }
        return null;
    }

    public void init() {
        if (list == null)
            list = "待播放列表";
        if (lyric == null)
            lyric = "歌词";
        if (info == null)
            info = "歌曲信息";
        if (pic == null)
            pic = "图片";
        if (all == null)
            all = "所有位置";
        if (picRoute == null)
            picRoute = "图片旋转";
        if (picSpeed == null)
            picSpeed = "图片旋转速度";
        if (enable == null)
            enable = "启用";
        if (disable == null)
            disable = "关闭";
        if (pos1 == null)
            pos1 = "左上";
        if (pos2 == null)
            pos2 = "上";
        if (pos3 == null)
            pos3 = "右上";
        if (pos4 == null)
            pos4 = "左";
        if (pos5 == null)
            pos5 = "居中";
        if (pos6 == null)
            pos6 = "右";
        if (pos7 == null)
            pos7 = "左下";
        if (pos8 == null)
            pos8 = "下";
        if (pos9 == null)
            pos9 = "右下";
    }
}
