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

    public static VoteObj make() {
        VoteObj obj = new VoteObj();
        obj.init();

        return obj;
    }

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
        if (noPermission == null)
            noPermission = "§d[AllMusic3]§c你没有权限切歌";
        if (doVote == null)
            doVote = "§d[AllMusic3]§e已发起切歌投票";
        if (bq == null)
            bq = "§d[AllMusic3]§e" + ARG.player + "发起了切歌投票，" + ARG.time + "秒后结束，输入/music vote 同意切歌。";
        if (bq1 == null)
            bq1 = "§d[AllMusic3]§e或者点击 ";
        if (bq2 == null)
            bq2 = "§a同意切歌";
        if (agree == null)
            agree = "§d[AllMusic3]§e你同意切歌";
        if (bqAgree == null)
            bqAgree = "§d[AllMusic3]§e" + ARG.player + "同意切歌，共有" + ARG.count + "名玩家同意切歌。";
        if (arAgree == null)
            arAgree = "§d[AllMusic3]§e你已申请切歌";
        if (timeOut == null)
            timeOut = "§d[AllMusic3]§e切歌时间结束";
        if (err1 == null)
            err1 = "§d[AllMusic3]§c你没有发起切歌";
        if (err2 == null)
            err2 = "§d[AllMusic3]§c你发起的切歌已超时";
        if (err3 == null)
            err3 = "§d[AllMusic3]§c已经有人发起了切歌";
        if (voteDone == null)
            voteDone = "§d[AllMusic3]§e已切歌";
        if (cancel == null)
            cancel = "§d[AllMusic3]§e切歌投票已被发起者取消";
    }
}
