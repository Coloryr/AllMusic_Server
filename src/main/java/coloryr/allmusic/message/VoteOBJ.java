package coloryr.allmusic.message;

public class VoteOBJ {
    private String NoPermission;
    private String DoVote;
    private String BQ;
    private String Agree;
    private String BQAgree;
    private String ARAgree;
    private String TimeOut;
    private String Do;

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
