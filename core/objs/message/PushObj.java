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

    public static PushObj make() {
        PushObj obj = new PushObj();
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
        if (noId == null)
            return true;
        if (noId1 == null)
            return true;
        if (idErr == null)
            return true;
        if (pushErr == null)
            return true;
        if (cancel == null)
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
        if (noPermission == null)
            noPermission = "<light_purple>[AllMusic3]<red>你没有权限插歌";
        if (doVote == null)
            doVote = "<light_purple>[AllMusic3]<yellow>已发起插歌投票";
        if (bq == null)
            bq = "<light_purple>[AllMusic3]<yellow>" + ARG.player + "发起了插歌投票，将曲目" + ARG.musicName + "-" + ARG.musicAuthor + "调整到下一首播放，" + ARG.time + "秒后结束，输入/music push 同意插歌。";
        if (bq1 == null)
            bq1 = "<light_purple>[AllMusic3]<yellow>或者点击 ";
        if (bq2 == null)
            bq2 = "<green><underlined>同意插歌";
        if (agree == null)
            agree = "<light_purple>[AllMusic3]<yellow>你同意插歌";
        if (bqAgree == null)
            bqAgree = "<light_purple>[AllMusic3]<yellow>" + ARG.player + "同意插歌，共有" + ARG.count + "名玩家同意插歌。";
        if (arAgree == null)
            arAgree = "<light_purple>[AllMusic3]<yellow>你已申请插歌";
        if (timeOut == null)
            timeOut = "<light_purple>[AllMusic3]<yellow>插歌时间结束";
        if (doPush == null)
            doPush = "<light_purple>[AllMusic3]<yellow>播放顺序已调整";
        if (noId == null)
            noId = "<light_purple>[AllMusic3]<red>没有找到你的点歌";
        if (noId1 == null)
            noId1 = "<light_purple>[AllMusic3]<red>没有找到序号为" + ARG.index + "的点歌";
        if (idErr == null)
            idErr = "<light_purple>[AllMusic3]<red>错误的歌曲序号";
        if (pushErr == null)
            pushErr = "<light_purple>[AllMusic3]<red>这首歌已经是下一首播放了";
        if (err1 == null)
            err1 = "<light_purple>[AllMusic3]<red>你没有发起插歌";
        if (err2 == null)
            err2 = "<light_purple>[AllMusic3]<red>你发起的插歌已超时";
        if (err3 == null)
            err3 = "<light_purple>[AllMusic3]<red>发起失败，已经有人发起了插歌投票";
        if (cancel == null)
            cancel = "<light_purple>[AllMusic3]<yellow>插歌投票已被发起者取消";
    }
}
