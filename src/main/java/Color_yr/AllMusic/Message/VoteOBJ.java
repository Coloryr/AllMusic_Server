package Color_yr.AllMusic.Message;

public class VoteOBJ {
    private final String NoPermission;
    private final String DoVote;
    private final String BQ;
    private final String Agree;
    private final String BQAgree;
    private final String ARAgree;
    private final String TimeOut;
    private final String Do;

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

    public String getDo() {
        return Do;
    }

    public String getTimeOut() {
        return TimeOut;
    }

    public String getARAgree() {
        return ARAgree;
    }

    public String getBQAgree() {
        return BQAgree;
    }

    public String getAgree() {
        return Agree;
    }

    public String getBQ() {
        return BQ;
    }

    public String getDoVote() {
        return DoVote;
    }

    public String getNoPermission() {
        return NoPermission;
    }
}
