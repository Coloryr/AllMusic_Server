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
            noPermission = "<light_purple>[AllMusic3]<red>你没有权限切歌";
        if (doVote == null)
            doVote = "<light_purple>[AllMusic3]<yellow>已发起切歌投票";
        if (bq == null)
            bq = "<light_purple>[AllMusic3]<yellow>" + ARG.player + "发起了切歌投票，" + ARG.time + "秒后结束，输入/music vote 同意切歌。";
        if (bq1 == null)
            bq1 = "<light_purple>[AllMusic3]<yellow>或者点击 ";
        if (bq2 == null)
            bq2 = "<green><underlined>同意切歌";
        if (agree == null)
            agree = "<light_purple>[AllMusic3]<yellow>你同意切歌";
        if (bqAgree == null)
            bqAgree = "<light_purple>[AllMusic3]<yellow>" + ARG.player + "同意切歌，共有" + ARG.count + "名玩家同意切歌。";
        if (arAgree == null)
            arAgree = "<light_purple>[AllMusic3]<yellow>你已申请切歌";
        if (timeOut == null)
            timeOut = "<light_purple>[AllMusic3]<yellow>切歌时间结束";
        if (err1 == null)
            err1 = "<light_purple>[AllMusic3]<red>你没有发起切歌";
        if (err2 == null)
            err2 = "<light_purple>[AllMusic3]<red>你发起的切歌已超时";
        if (err3 == null)
            err3 = "<light_purple>[AllMusic3]<red>已经有人发起了切歌";
        if (voteDone == null)
            voteDone = "<light_purple>[AllMusic3]<yellow>已切歌";
        if (cancel == null)
            cancel = "<light_purple>[AllMusic3]<yellow>切歌投票已被发起者取消";
    }
}
