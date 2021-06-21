package Color_yr.AllMusic.message;

public class CostObj {
    private String Search;
    private String AddMusic;
    private String NoMoney;

    public CostObj() {
        Search = "§d[ALLMusic]§2你搜歌花费了%Cost%";
        AddMusic = "§d[ALLMusic]§2你点歌花费了%Cost%";
        NoMoney = "§d[ALLMusic]§c你没有足够的钱";
    }

    public String getNoMoney() {
        return NoMoney;
    }

    public String getAddMusic() {
        return AddMusic;
    }

    public String getSearch() {
        return Search;
    }
}
