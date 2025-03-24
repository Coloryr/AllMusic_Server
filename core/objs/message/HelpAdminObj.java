package com.coloryr.allmusic.server.core.objs.message;

public class HelpAdminObj {
    public String reload;
    public String next;
    public String ban;
    public String banPlayer;
    public String url;
    public String delete;
    public String addList;
    public String clearList;
    public String cookie;
    public String test;
    public String unbanPlayer;
    public String unban;
    public String clearBanList;
    public String clearBanPlayerList;

    public static HelpAdminObj make() {
        HelpAdminObj obj = new HelpAdminObj();
        obj.init();

        return obj;
    }

    public boolean check() {
        if (reload == null)
            return true;
        if (next == null)
            return true;
        if (ban == null)
            return true;
        if (unban == null)
            return true;
        if (unbanPlayer == null)
            return true;
        if (url == null)
            return true;
        if (delete == null)
            return true;
        if (addList == null)
            return true;
        if (clearList == null)
            return true;
        if (banPlayer == null)
            return true;
        if (test == null)
            return true;
        if (clearBanList == null)
            return true;
        if (clearBanPlayerList == null)
            return true;
        return cookie == null;
    }

    public void init() {
        if (reload == null)
            reload = "§d[AllMusic3]§e使用/music reload 重读配置文件";
        if (next == null)
            next = "§d[AllMusic3]§e使用/music next 强制切歌";
        if (ban == null)
            ban = "§d[AllMusic3]§e使用/music ban [ID] 禁止点这首歌";
        if (banPlayer == null)
            banPlayer = "§d[AllMusic3]§e使用/music banplayer [ID] 禁止某位玩家点歌";
        if (unban == null)
            unban = "§d[AllMusic3]§e使用/music unban [ID] 解禁点这首歌";
        if (unbanPlayer == null)
            unbanPlayer = "§d[AllMusic3]§e使用/music unbanplayer [ID] 解禁某位玩家点歌";
        if (url == null)
            url = "§d[AllMusic3]§e使用/music url [url] 播放一首自定义歌曲";
        if (delete == null)
            delete = "§d[AllMusic3]§e使用/music delete [序号] 删除队列中的歌曲";
        if (addList == null)
            addList = "§d[AllMusic3]§e使用/music addlist [歌单ID] 添加歌单到空闲列表";
        if (clearList == null)
            clearList = "§d[AllMusic3]§e使用/music clearlist 清空空闲歌单";
        if (clearBanList == null)
            clearBanList = "§d[AllMusic3]§e使用/music clearban 清空禁止点歌列表";
        if (clearBanPlayerList == null)
            clearBanPlayerList = "§d[AllMusic3]§e使用/music clearbanplayer 清空禁止玩家点歌列表";
        if (cookie == null)
            cookie = "§d[AllMusic3]§e使用/music cookie [cookie] 填写cookie";
        if (test == null)
            test = "§d[AllMusic3]§e使用/music test [音乐ID] 测试解析音乐";
    }
}
