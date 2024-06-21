package com.coloryr.allmusic.server.core.objs.message;

public class SearchObj {
    public String noPer;
    public String cantSearch;
    public String res;
    public String emptySearch;
    public String errorNum;
    public String choice;
    public String cantNext;
    public String cantLast;
    public String startSearch;

    public static SearchObj make() {
        SearchObj obj = new SearchObj();
        obj.init();

        return obj;
    }

    public boolean check() {
        if (noPer == null)
            return true;
        if (cantSearch == null)
            return true;
        if (res == null)
            return true;
        if (emptySearch == null)
            return true;
        if (errorNum == null)
            return true;
        if (choice == null)
            return true;
        if (cantNext == null)
            return true;
        if (cantLast == null)
            return true;
        return startSearch == null;
    }

    public void init() {
        startSearch = "§d[AllMusic3]§e正在排队搜歌";
        noPer = "§d[AllMusic3]§c你没有权限搜歌";
        cantSearch = "§d[AllMusic3]§c无法搜索歌曲：" + PAL.name;
        res = "§d[AllMusic3]§e搜索结果";
        emptySearch = "§d[AllMusic3]§c你没有搜索音乐";
        errorNum = "§d[AllMusic3]§c请输入正确的序号";
        choice = "§d[AllMusic3]§e你选择了序号：" + PAL.index;
        cantNext = "§d[AllMusic3]§c无法下一页";
        cantLast = "§d[AllMusic3]§c无法上一页";
    }
}

