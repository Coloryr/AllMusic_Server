package coloryr.allmusic.core.objs.message;

public class HudObj {
    public String state;
    public String set;
    public String set1;
    public String set2;
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

    public boolean check() {
        if (state == null)
            return true;
        if (set == null)
            return true;
        if (set1 == null)
            return true;
        if (set2 == null)
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
        state = "§d[AllMusic3]§e设置信息位置：%Hud%，状态：%State%";
        set = "§d[AllMusic3]§e已设置[%Hud%]的坐标为[%x%, %y%]";
        set1 = "§d[AllMusic3]§e已设置[%Hud%]的对齐方式为[%Dir%]";
        set2 = "§d[AllMusic3]§e已设置[%Hud%]的颜色为[%Color%]";
        reset = "§d[AllMusic3]§e界面[%Hud%]恢复至默认位置";
        emptyMusic = "没有播放的音乐";
        emptyList = "队列中无歌曲";
        emptyLyric = "无歌词";
        picSize = "§d[AllMusic3]§e设置图片尺寸为%Size%";
        picRotate = "§d[AllMusic3]§e设置图片旋转为%State%";
        picSpeed = "§d[AllMusic3]§e设置图片旋转速度为%Size%";
        music = "%Name%  %AllTime%/%NowTime%\n%Author%\n%Alia%\n%Al%\nby: %Player%";
        lyric = "%Lyric%\n%Tlyric%";
        ktv = "§e%KLyric%§f%Lyric%§r\n%Tlyric%";
        list = "播放列表里面有%Size%首歌\n%List%";
    }

    public static HudObj make() {
        HudObj obj = new HudObj();
        obj.init();

        return obj;
    }
}
