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
            listFull = "§d[AllMusic3]§c错误，队列已满";
        if (playerToMany == null)
            playerToMany = "§d[AllMusic3]§c错误，你点了太多的歌";
        if (playerBan == null)
            playerBan = "§d[AllMusic3]§c错误，你已被禁止点歌";
        if (banMusic == null)
            banMusic = "§d[AllMusic3]§c错误，这首歌被禁点了";
        if (existMusic == null)
            existMusic = "§d[AllMusic3]§c错误，这首歌已经存在了";
        if (success == null)
            success = "§d[AllMusic3]§2点歌成功";
        if (noPlayer == null)
            noPlayer = "§d[AllMusic3]§c没有播放的玩家";
        if (noID == null)
            noID = "§d[AllMusic3]§c错误，请输入歌曲数字ID";
        if (cancel == null)
            cancel = "§d[AllMusic3]§e点歌被取消";
        if (timeOut == null)
            timeOut = "§d[AllMusic3]§e点歌被取消，音乐长度过长";
    }
}
