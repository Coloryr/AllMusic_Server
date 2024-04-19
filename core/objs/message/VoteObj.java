package com.coloryr.allmusic.server.core.objs.message;

public class VoteObj {
    public String noPermission;
    public String doVote;
    public String bq;
    public String bq1;
    public String bq2;
    public String agree;
    public String bqAgree;
    public String arAgree;
    public String timeOut;
    public String voteDone;
    public String err1;
    public String err2;
    public String err3;
    public String cancel;

    public boolean check() {
        if (noPermission == null)
            return true;
        if (doVote == null)
            return true;
        if (bq == null)
            return true;
        if (bq1 == null)
            return true;
        if (bq2 == null)
            return true;
        if (agree == null)
            return true;
        if (bqAgree == null)
            return true;
        if (arAgree == null)
            return true;
        if (timeOut == null)
            return true;
        if (err1 == null)
            return true;
        if (err2 == null)
            return true;
        if (err3 == null)
            return true;
        if (cancel == null)
            return true;
        return voteDone == null;
    }

    public void init() {
        noPermission = "§d[AllMusic3]§c你没有权限切歌";
        doVote = "§d[AllMusic3]§e已发起切歌投票";
        bq = "§d[AllMusic3]§e%PlayerName%发起了切歌投票，%Time%秒后结束，输入/music vote 同意切歌。";
        bq1 = "§d[AllMusic3]§e或者点击 ";
        bq2 = "§a同意切歌";
        agree = "§d[AllMusic3]§e你同意切歌";
        bqAgree = "§d[AllMusic3]§e%PlayerName%同意切歌，共有%Count%名玩家同意切歌。";
        arAgree = "§d[AllMusic3]§e你已申请切歌";
        timeOut = "§d[AllMusic3]§e切歌时间结束";
        err1 = "§d[AllMusic3]§c你没有发起切歌";
        err2 = "§d[AllMusic3]§c你发起的切歌已超时";
        err3 = "§d[AllMusic3]§c已经有人发起了切歌";
        voteDone = "§d[AllMusic3]§e已切歌";
        cancel = "§d[AllMusic3]§e切歌投票已被发起者取消";
    }

    public static VoteObj make() {
        VoteObj obj = new VoteObj();
        obj.init();

        return obj;
    }
}
