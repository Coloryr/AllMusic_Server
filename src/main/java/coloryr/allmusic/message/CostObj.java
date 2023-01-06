package coloryr.allmusic.message;

public class CostObj {
    public String Search;
    public String AddMusic;
    public String NoMoney;
    public String CostFail;

    public CostObj() {
        Search = "§d[ALLMusic]§2你搜歌花费了%Cost%";
        AddMusic = "§d[ALLMusic]§2你点歌花费了%Cost%";
        NoMoney = "§d[ALLMusic]§c你没有足够的钱";
        CostFail = "§d[ALLMusic]§c扣钱过程中错误";
    }

    public boolean check() {
        if (Search == null)
            return true;
        if (AddMusic == null)
            return true;
        if (NoMoney == null)
            return true;
        if(CostFail == null)
            return true;

        return false;
    }
}
