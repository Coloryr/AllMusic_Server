package com.coloryr.allmusic.server.core.objs.message;

public class PushObj {
    public String noPermission;
    public String doVote;
    public String bq;
    public String bq1;
    public String bq2;
    public String agree;
    public String bqAgree;
    public String arAgree;
    public String timeOut;
    public String doPush;
    public String noId;
    public String noId1;
    public String idErr;
    public String pushErr;
    public String cancel;
    public String err1;
    public String err2;
    public String err3;

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
        if (noId == null)
            return true;
        if (noId1 == null)
            return true;
        if (idErr == null)
            return true;
        if (pushErr == null)
            return true;
        if(cancel == null)
            return true;
        if (err1 == null)
            return true;
        if (err2 == null)
            return true;
        if (err3 == null)
            return true;
        return doPush == null;
    }

    public void init() {
        noPermission = "§d[AllMusic3]§c你没有权限插歌";
        doVote = "§d[AllMusic3]§e已发起插歌投票";
        bq = "§d[AllMusic3]§e%PlayerName%发起了插歌投票，将曲目%MusicName%-%MusicAuthor%调整到下一首播放，%Time%秒后结束，输入/music push 同意插歌。";
        bq1 = "§d[AllMusic3]§e或者点击 ";
        bq2 = "§a同意插歌";
        agree = "§d[AllMusic3]§e你同意插歌";
        bqAgree = "§d[AllMusic3]§e%PlayerName%同意插歌，共有%Count%名玩家同意插歌。";
        arAgree = "§d[AllMusic3]§e你已申请插歌";
        timeOut = "§d[AllMusic3]§e插歌时间结束";
        doPush = "§d[AllMusic3]§e播放顺序已调整";
        noId = "§d[AllMusic3]§c没有找到你的点歌";
        noId1 = "§d[AllMusic3]§c没有找到序号为%Index%的点歌";
        idErr = "§d[AllMusic3]§c错误的歌曲序号";
        pushErr = "§d[AllMusic3]§c这首歌已经是下一首播放了";
        err1 = "§d[AllMusic3]§c你没有发起插歌";
        err2 = "§d[AllMusic3]§c你发起的插歌已超时";
        err3 = "§d[AllMusic3]§c发起失败，已经有人发起了插歌投票";
        cancel = "§d[AllMusic3]§e插歌投票已被发起者取消";
    }

    public static PushObj make() {
        PushObj obj = new PushObj();
        obj.init();

        return obj;
    }
}
