package coloryr.allmusic.message;

public class HelpNormalObj {
    private String Head;
    private String Base;
    private String Stop;
    private String List;
    private String Vote;
    private String NoMusic;
    private String Search;
    private String Select;
    private String Hud1;
    private String Hud2;
    private String Hud3;

    public HelpNormalObj() {
        Head = "§d[AllMusic]§2帮助手册";
        Base = "§d[AllMusic]§2使用/music [音乐ID] 来点歌";
        Stop = "§d[AllMusic]§2使用/music stop 停止播放歌曲";
        List = "§d[AllMusic]§2使用/music list 查看歌曲队列";
        Vote = "§d[AllMusic]§2使用/music vote 投票切歌";
        NoMusic = "§d[AllMusic]§2使用/music nomusic 不再参与点歌";
        Search = "§d[AllMusic]§2使用/music search [歌名] 搜索歌曲";
        Select = "§d[AllMusic]§2使用/music select [序列] 选择歌曲";
        Hud1 = "§d[AllMusic]§2使用/music hud enable [位置] 启用关闭Hud";
        Hud2 = "§d[AllMusic]§2使用/music hud [位置] [x] [y] 设置某个Hud的位置";
        Hud3 = "§d[AllMusic]§2使用/music hud picsize [尺寸] 设置图片尺寸";
    }

    public boolean check(){
        if(Head == null)
            return true;
        if(Base == null)
            return true;
        if(Stop == null)
            return true;
        if(List == null)
            return true;
        if(Vote == null)
            return true;
        if(NoMusic == null)
            return true;
        if(Search == null)
            return true;
        if(Select == null)
            return true;
        if(Hud1 == null)
            return true;
        if(Hud2 == null)
            return true;
        if(Hud3 == null)
            return true;

        return false;
    }

    public String getHud3() {
        return Hud3;
    }

    public String getBase() {
        return Base;
    }

    public String getHud1() {
        return Hud1;
    }

    public String getHud2() {
        return Hud2;
    }

    public String getList() {
        return List;
    }

    public String getNoMusic() {
        return NoMusic;
    }

    public String getSearch() {
        return Search;
    }

    public String getSelect() {
        return Select;
    }

    public String getStop() {
        return Stop;
    }

    public String getVote() {
        return Vote;
    }

    public String getHead() {
        return Head;
    }
}
