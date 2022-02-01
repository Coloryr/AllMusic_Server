package coloryr.allmusic.message;

public class CostObj {
    private String Search;
    private String AddMusic;
    private String NoMoney;

    public CostObj() {
        Search = "§d[ALLMusic]§2你搜歌花费了%Cost%";
        AddMusic = "§d[ALLMusic]§2你点歌花费了%Cost%";
        NoMoney = "§d[ALLMusic]§c你没有足够的钱";
    }

    public boolean check(){
        if(Search == null)
            return true;
        if(AddMusic == null)
            return true;
        if(NoMoney == null)
            return true;

        return false;
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
