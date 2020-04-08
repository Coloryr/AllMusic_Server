package Color_yr.ALLMusic.Message;

public class SearchOBJ {
    private String NoPer;
    private String CantSearch;
    private String Res;
    private String NoSearch;
    private String ErrorNum;
    private String Chose;
    private String CantNext;
    private String CantLast;

    public SearchOBJ() {
        NoPer = "§d[ALLMusic]§c你没有权限搜歌";
        CantSearch = "§d[ALLMusic]§c无法搜索歌曲：%Music%";
        Res = "§d[ALLMusic]§2搜索结果";
        NoSearch = "§d[ALLMusic]§c你没有搜索音乐";
        ErrorNum = "§d[ALLMusic]§c请输入正确的序号";
        Chose = "§d[ALLMusic]§2你选择了序号%Num%";
        CantNext = "§d[ALLMusic]§c无法下一页";
        CantLast = "§d[ALLMusic]§c无法上一页";
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
