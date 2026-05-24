package com.coloryr.allmusic.server.core.objs.message;

public class AddMusicObj {
    public String listFull;
    public String playerToMany;
    public String banMusic;
    public String playerBan;
    public String existMusic;
    public String success;
    public String noPlayer;
    public String noID;
    public String cancel;
    public String timeOut;

    public static AddMusicObj make() {
        AddMusicObj obj = new AddMusicObj();
        obj.init();

        return obj;
    }

    public boolean check() {
        if (listFull == null)
            return true;
        if (banMusic == null)
            return true;
        if (existMusic == null)
            return true;
        if (success == null)
            return true;
        if (noPlayer == null)
            return true;
        if (noID == null)
            return true;
        if (cancel == null)
            return true;
        if (playerToMany == null)
            return true;
        if (playerBan == null)
            return true;
        return timeOut == null;
    }

    public void init() {
        if (listFull == null)
            listFull = "<light_purple>[AllMusic3]<red>错误，队列已满";
        if (playerToMany == null)
            playerToMany = "<light_purple>[AllMusic3]<red>错误，你点了太多的歌";
        if (playerBan == null)
            playerBan = "<light_purple>[AllMusic3]<red>错误，你已被禁止点歌";
        if (banMusic == null)
            banMusic = "<light_purple>[AllMusic3]<red>错误，这首歌被禁点了";
        if (existMusic == null)
            existMusic = "<light_purple>[AllMusic3]<red>错误，这首歌已经存在了";
        if (success == null)
            success = "<light_purple>[AllMusic3]<dark_green>点歌成功";
        if (noPlayer == null)
            noPlayer = "<light_purple>[AllMusic3]<red>没有播放的玩家";
        if (noID == null)
            noID = "<light_purple>[AllMusic3]<red>错误，请输入歌曲数字ID";
        if (cancel == null)
            cancel = "<light_purple>[AllMusic3]<yellow>点歌被取消";
        if (timeOut == null)
            timeOut = "<light_purple>[AllMusic3]<yellow>点歌被取消，音乐长度过长";
    }
}
