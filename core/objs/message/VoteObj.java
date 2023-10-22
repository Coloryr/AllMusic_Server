package coloryr.allmusic.core.objs.message;

public class VoteObj {
    public String NoPermission;
    public String DoVote;
    public String BQ;
    public String Agree;
    public String BQAgree;
    public String ARAgree;
    public String TimeOut;
    public String Do;

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
        return Do == null;
    }

    public void init() {
        NoPermission = "§d[AllMusic]§c你没有权限切歌";
        DoVote = "§d[AllMusic]§e已发起切歌投票";
        BQ = "§d[AllMusic]§e%PlayerName%发起了切歌投票，%Time%秒后结束，输入/music vote 同意切歌。";
        Agree = "§d[AllMusic]§e你同意切歌";
        BQAgree = "§d[AllMusic]§e%PlayerName%同意切歌，共有%Count%名玩家同意切歌。";
        ARAgree = "§d[AllMusic]§e你已申请切歌";
        TimeOut = "§d[AllMusic]§e切歌时间结束";
        Do = "§d[AllMusic]§e已切歌";
    }

    public static VoteObj make() {
        VoteObj obj = new VoteObj();
        obj.init();

        return obj;
    }
}
