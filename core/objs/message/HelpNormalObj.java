package coloryr.allmusic.core.objs.message;

public class HelpNormalObj {
    public String Head;
    public String Base;
    public String Stop;
    public String List;
    public String Vote;
    public String NoMusic;
    public String Search;
    public String Select;
    public String Hud1;
    public String Hud2;
    public String Hud3;
    public String Hud4;
    public String Hud5;

    public boolean check() {
        if (Head == null)
            return true;
        if (Base == null)
            return true;
        if (Stop == null)
            return true;
        if (List == null)
            return true;
        if (Vote == null)
            return true;
        if (NoMusic == null)
            return true;
        if (Search == null)
            return true;
        if (Select == null)
            return true;
        if (Hud1 == null)
            return true;
        if (Hud2 == null)
            return true;
        if (Hud3 == null)
            return true;
        if (Hud4 == null)
            return true;
        return Hud5 == null;
    }

    public void init() {
        Head = "§d[AllMusic]§e帮助手册";
        Base = "§d[AllMusic]§e使用/music [音乐ID] 来点歌";
        Stop = "§d[AllMusic]§e使用/music stop 停止播放歌曲";
        List = "§d[AllMusic]§e使用/music list 查看歌曲队列";
        Vote = "§d[AllMusic]§e使用/music vote 投票切歌";
        NoMusic = "§d[AllMusic]§e使用/music nomusic 不再参与点歌";
        Search = "§d[AllMusic]§e使用/music search [歌名] 搜索歌曲";
        Select = "§d[AllMusic]§e使用/music select [序列] 选择歌曲";
        Hud1 = "§d[AllMusic]§e使用/music hud enable [位置] 启用关闭Hud";
        Hud2 = "§d[AllMusic]§e使用/music hud [位置] [x] [y] 设置某个Hud的位置";
        Hud3 = "§d[AllMusic]§e使用/music hud picsize [尺寸] 设置图片尺寸";
        Hud4 = "§d[AllMusic]§e使用/music hud picrotate [开关] 设置图片旋转模式";
        Hud5 = "§d[AllMusic]§e使用/music hud picrotatespeed [数值] 设置图片旋转速度";
    }

    public static HelpNormalObj make() {
        HelpNormalObj obj = new HelpNormalObj();
        obj.init();

        return obj;
    }
}
