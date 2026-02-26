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
    public String error1;

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
        if (startSearch == null)
            startSearch = "§d[AllMusic3]§e正在排队搜歌";
        if (noPer == null)
            noPer = "§d[AllMusic3]§c你没有权限搜歌";
        if (cantSearch == null)
            cantSearch = "§d[AllMusic3]§c无法搜索歌曲：" + ARG.name;
        if (res == null)
            res = "§d[AllMusic3]§e搜索结果";
        if (emptySearch == null)
            emptySearch = "§d[AllMusic3]§c你没有搜索音乐";
        if (errorNum == null)
            errorNum = "§d[AllMusic3]§c请输入正确的序号";
        if (choice == null)
            choice = "§d[AllMusic3]§e你选择了序号：" + ARG.index;
        if (cantNext == null)
            cantNext = "§d[AllMusic3]§c无法下一页";
        if (cantLast == null)
            cantLast = "§d[AllMusic3]§c无法上一页";
    }
}

