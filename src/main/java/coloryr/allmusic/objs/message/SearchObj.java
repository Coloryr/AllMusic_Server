package coloryr.allmusic.objs.message;

public class SearchObj {
    public String NoPer;
    public String CantSearch;
    public String Res;
    public String NoSearch;
    public String ErrorNum;
    public String Chose;
    public String CantNext;
    public String CantLast;
    public String StartSearch;

    public SearchObj() {
        StartSearch = "§d[AllMusic]§2正在排队搜歌";
        NoPer = "§d[AllMusic]§c你没有权限搜歌";
        CantSearch = "§d[AllMusic]§c无法搜索歌曲：%Music%";
        Res = "§d[AllMusic]§2搜索结果";
        NoSearch = "§d[AllMusic]§c你没有搜索音乐";
        ErrorNum = "§d[AllMusic]§c请输入正确的序号";
        Chose = "§d[AllMusic]§2你选择了序号%Num%";
        CantNext = "§d[AllMusic]§c无法下一页";
        CantLast = "§d[AllMusic]§c无法上一页";
    }

    public boolean check() {
        if (NoPer == null)
            return true;
        if (CantSearch == null)
            return true;
        if (Res == null)
            return true;
        if (NoSearch == null)
            return true;
        if (ErrorNum == null)
            return true;
        if (Chose == null)
            return true;
        if (CantNext == null)
            return true;
        if (CantLast == null)
            return true;
        return StartSearch == null;
    }
}
