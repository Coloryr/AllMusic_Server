package com.coloryr.allmusic.server.core.objs.message;

public class HelpAdminObj {
    public String reload;
    public String next;
    public String ban;
    public String ban1;
    public String banPlayer;
    public String delete;
    public String addList;
    public String addList1;
    public String clearList;
    public String test;
    public String test1;
    public String unbanPlayer;
    public String unban;
    public String unban1;
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
        if (ban1 == null)
            return true;
        if (unban == null)
            return true;
        if (unban1 == null)
            return true;
        if (unbanPlayer == null)
            return true;
        if (delete == null)
            return true;
        if (addList == null)
            return true;
        if (addList1 == null)
            return true;
        if (clearList == null)
            return true;
        if (banPlayer == null)
            return true;
        if (test == null)
            return true;
        if (clearBanList == null)
            return true;
        return clearBanPlayerList == null;
    }

    public void init() {
        if (reload == null)
            reload = "<light_purple>[AllMusic3]<yellow>使用/music reload 重读配置文件";
        if (next == null)
            next = "<light_purple>[AllMusic3]<yellow>使用/music next 强制切歌";
        if (ban == null)
            ban = "<light_purple>[AllMusic3]<yellow>使用/music ban [ID] 禁止点这首歌";
        if (ban1 == null)
            ban1 = "<light_purple>[AllMusic3]<yellow>使用/music ban [音乐API] [ID] 禁止点这首歌";
        if (banPlayer == null)
            banPlayer = "<light_purple>[AllMusic3]<yellow>使用/music banplayer [ID] 禁止某位玩家点歌";
        if (unban == null)
            unban = "<light_purple>[AllMusic3]<yellow>使用/music unban [ID] 解禁点这首歌";
        if (unban1 == null)
            unban1 = "<light_purple>[AllMusic3]<yellow>使用/music unban [音乐API] [ID] 解禁点这首歌";
        if (unbanPlayer == null)
            unbanPlayer = "<light_purple>[AllMusic3]<yellow>使用/music unbanplayer [ID] 解禁某位玩家点歌";
        if (delete == null)
            delete = "<light_purple>[AllMusic3]<yellow>使用/music delete [序号] 删除队列中的歌曲";
        if (addList == null)
            addList = "<light_purple>[AllMusic3]<yellow>使用/music addlist [歌单ID] 添加歌单到空闲列表";
        if (addList1 == null)
            addList1 = "<light_purple>[AllMusic3]<yellow>使用/music addlist [音乐API] [歌单ID] 添加歌单到空闲列表";
        if (clearList == null)
            clearList = "<light_purple>[AllMusic3]<yellow>使用/music clearlist 清空空闲歌单";
        if (clearBanList == null)
            clearBanList = "<light_purple>[AllMusic3]<yellow>使用/music clearban 清空禁止点歌列表";
        if (clearBanPlayerList == null)
            clearBanPlayerList = "<light_purple>[AllMusic3]<yellow>使用/music clearbanplayer 清空禁止玩家点歌列表";
        if (test == null)
            test = "<light_purple>[AllMusic3]<yellow>使用/music test [ID] 测试解析音乐";
        if (test1 == null)
            test1 = "<light_purple>[AllMusic3]<yellow>使用/music test [音乐API] [ID] 测试解析音乐";
    }
}
