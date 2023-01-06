package coloryr.allmusic.message;

public class VoteOBJ {
    public String NoPermission;
    public String DoVote;
    public String BQ;
    public String Agree;
    public String BQAgree;
    public String ARAgree;
    public String TimeOut;
    public String Do;

    public VoteOBJ() {
        NoPermission = "§d[AllMusic]§c你没有权限切歌";
        DoVote = "§d[AllMusic]§2已发起切歌投票";
        BQ = "§d[AllMusic]§2%PlayerName%发起了切歌投票，30秒后结束，输入/music vote 同意切歌。";
        Agree = "§d[AllMusic]§2你同意切歌";
        BQAgree = "§d[AllMusic]§2%PlayerName%同意切歌，共有%Count%名玩家同意切歌。";
        ARAgree = "§d[AllMusic]§2你已申请切歌";
        TimeOut = "§d[AllMusic]§2切歌时间结束";
        Do = "§d[AllMusic]§2已切歌";
    }

    public boolean check() {
        if (NoPermission == null)
            return true;
        if (DoVote == null)
            return true;
        if (BQ == null)
            return true;
        if (Agree == null)
            return true;
        if (BQAgree == null)
            return true;
        if (ARAgree == null)
            return true;
        if (TimeOut == null)
            return true;
        if (Do == null)
            return true;

        return false;
    }
}
