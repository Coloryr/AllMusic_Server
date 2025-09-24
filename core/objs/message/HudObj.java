package com.coloryr.allmusic.server.core.objs.message;

public class HudObj {
    public String state;
    public String set;
    public String set1;
    public String set2;
    public String set3;
    public String reset;
    public String emptyMusic;
    public String emptyList;
    public String emptyLyric;
    public String picSize;
    public String picRotate;
    public String picSpeed;
    public String music;
    public String lyric;
    public String list;
    public String ktv;

    public static HudObj make() {
        HudObj obj = new HudObj();
        obj.init();

        return obj;
    }

    public boolean check() {
        if (state == null)
            return true;
        if (set == null)
            return true;
        if (set1 == null)
            return true;
        if (set2 == null)
            return true;
        if (set3 == null)
            return true;
        if (reset == null)
            return true;
        if (emptyMusic == null)
            return true;
        if (emptyList == null)
            return true;
        if (emptyLyric == null)
            return true;
        if (picSize == null)
            return true;
        if (music == null)
            return true;
        if (lyric == null)
            return true;
        if (list == null)
            return true;
        if (picRotate == null)
            return true;
        if (picSpeed == null)
            return true;
        return ktv == null;
    }

    public void init() {
        if (state == null)
            state = "§d[AllMusic3]§e设置信息位置：" + ARG.hud + "，状态：" + ARG.state;
        if (set == null)
            set = "§d[AllMusic3]§e已设置[" + ARG.hud + "]的坐标为[" + ARG.x + ", " + ARG.y + "]";
        if (set1 == null)
            set1 = "§d[AllMusic3]§e已设置[" + ARG.hud + "]的对齐方式为[" + ARG.dir + "]";
        if (set2 == null)
            set2 = "§d[AllMusic3]§e已设置[" + ARG.hud + "]的颜色为[" + ARG.color + "]";
        if (set3 == null)
            set3 = "§d[AllMusic3]§e已设置[" + ARG.hud + "]的阴影为[" + ARG.state + "]";
        if (reset == null)
            reset = "§d[AllMusic3]§e界面[" + ARG.hud + "]恢复至默认位置";
        if (emptyMusic == null)
            emptyMusic = "没有播放的音乐";
        if (emptyList == null)
            emptyList = "队列中无歌曲";
        if (emptyLyric == null)
            emptyLyric = "无歌词";
        if (picSize == null)
            picSize = "§d[AllMusic3]§e设置图片尺寸为[" + ARG.size + "]";
        if (picRotate == null)
            picRotate = "§d[AllMusic3]§e设置图片旋转为[" + ARG.state + "]";
        if (picSpeed == null)
            picSpeed = "§d[AllMusic3]§e设置图片旋转速度为[" + ARG.size + "]";
        if (music == null)
            music = ARG.name + " " + ARG.allTime + "/" + ARG.nowTime
                    + "\n" + ARG.musicAuthor + "\n" + ARG.musicAlia + "\n" + ARG.musicAl
                    + "\nby: " + ARG.player;
        if (lyric == null)
            lyric = ARG.lyric + "\n" + ARG.tlyric;
        if (ktv == null)
            ktv = "§e" + ARG.klyric + "§f" + ARG.lyric + "§r\n" + ARG.tlyric;
        if (list == null)
            list = "播放列表里面有" + ARG.size + "首歌\n" + ARG.list;
    }
}
