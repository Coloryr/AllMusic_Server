package coloryr.allmusic.core.objs.message;

public class CostObj {
    public String Search;
    public String AddMusic;
    public String NoMoney;
    public String CostFail;

    public boolean check() {
        if (Search == null)
            return true;
        if (AddMusic == null)
            return true;
        if (NoMoney == null)
            return true;
        return CostFail == null;
    }

    public void init() {
        Search = "§d[AllMusic]§e你搜歌花费了%Cost%";
        AddMusic = "§d[ALLMusic]§e你点歌花费了%Cost%";
        NoMoney = "§d[ALLMusic]§c你没有足够的钱";
        CostFail = "§d[ALLMusic]§c扣钱过程中错误";
    }

    public static CostObj make() {
        CostObj obj = new CostObj();
        obj.init();

        return obj;
    }
}
