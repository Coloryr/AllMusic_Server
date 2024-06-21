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
        state = "§d[AllMusic3]§e设置信息位置：" + PAL.hud + "，状态：" + PAL.state;
        set = "§d[AllMusic3]§e已设置[" + PAL.hud + "]的坐标为[" + PAL.x + ", " + PAL.y + "]";
        set1 = "§d[AllMusic3]§e已设置[" + PAL.hud + "]的对齐方式为[" + PAL.dir + "]";
        set2 = "§d[AllMusic3]§e已设置[" + PAL.hud + "]的颜色为[" + PAL.color + "]";
        set3 = "§d[AllMusic3]§e已设置[" + PAL.hud + "]的阴影为[" + PAL.state + "]";
        reset = "§d[AllMusic3]§e界面[" + PAL.hud + "]恢复至默认位置";
        emptyMusic = "没有播放的音乐";
        emptyList = "队列中无歌曲";
        emptyLyric = "无歌词";
        picSize = "§d[AllMusic3]§e设置图片尺寸为[" + PAL.size + "]";
        picRotate = "§d[AllMusic3]§e设置图片旋转为[" + PAL.state + "]";
        picSpeed = "§d[AllMusic3]§e设置图片旋转速度为[" + PAL.size + "]";
        music = PAL.name + " " + PAL.allTime + "/" + PAL.nowTime
                + "\n" + PAL.musicAuthor + "\n" + PAL.musicAlia + "\n" + PAL.musicAl
                + "\nby: " + PAL.player;
        lyric = PAL.lyric + "\n" + PAL.tlyric;
        ktv = "§e" + PAL.klyric + "§f" + PAL.lyric + "§r\n" + PAL.tlyric;
        list = "播放列表里面有" + PAL.size + "首歌\n" + PAL.list;
    }
}
