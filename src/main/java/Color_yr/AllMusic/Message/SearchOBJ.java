package Color_yr.AllMusic.Message;

public class SearchOBJ {
    private final String NoPer;
    private final String CantSearch;
    private final String Res;
    private final String NoSearch;
    private final String ErrorNum;
    private final String Chose;
    private final String CantNext;
    private final String CantLast;

    public SearchOBJ() {
        NoPer = "§d[AllMusic]§c你没有权限搜歌";
        CantSearch = "§d[AllMusic]§c无法搜索歌曲：%Music%";
        Res = "§d[AllMusic]§2搜索结果";
        NoSearch = "§d[AllMusic]§c你没有搜索音乐";
        ErrorNum = "§d[AllMusic]§c请输入正确的序号";
        Chose = "§d[AllMusic]§2你选择了序号%Num%";
        CantNext = "§d[AllMusic]§c无法下一页";
        CantLast = "§d[AllMusic]§c无法上一页";
    }

    public String getCantLast() {
        return CantLast;
    }

    public String getCantNext() {
        return CantNext;
    }

    public String getChose() {
        return Chose;
    }

    public String getErrorNum() {
        return ErrorNum;
    }

    public String getNoSearch() {
        return NoSearch;
    }

    public String getRes() {
        return Res;
    }

    public String getNoPer() {
        return NoPer;
    }

    public String getCantSearch() {
        return CantSearch;
    }
}
