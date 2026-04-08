package com.coloryr.allmusic.server.core.objs.message;

public class HelpNormalObj {
    public String head;
    public String base;
    public String stop;
    public String list;
    public String cancel;
    public String vote;
    public String vote1;
    public String push;
    public String push1;
    public String mute;
    public String mutelist;
    public String search;
    public String select;
    public String nextpage;
    public String lastpage;
    public String hud1;
    public String hud2;
    public String hud3;
    public String hud4;
    public String hud5;
    public String hud6;
    public String hud7;
    public String hud8;
    public String hud9;
    public String hud10;
    public String join;

    public static HelpNormalObj make() {
        HelpNormalObj obj = new HelpNormalObj();
        obj.init();

        return obj;
    }

    public boolean check() {
        if (head == null)
            return true;
        if (base == null)
            return true;
        if (stop == null)
            return true;
        if (list == null)
            return true;
        if (cancel == null)
            return true;
        if (vote == null)
            return true;
        if (vote1 == null)
            return true;
        if (push == null)
            return true;
        if (push1 == null)
            return true;
        if (mute == null)
            return true;
        if (mutelist == null)
            return true;
        if (search == null)
            return true;
        if (select == null)
            return true;
        if (hud1 == null)
            return true;
        if (hud2 == null)
            return true;
        if (hud3 == null)
            return true;
        if (hud4 == null)
            return true;
        if (hud6 == null)
            return true;
        if (hud7 == null)
            return true;
        if (hud8 == null)
            return true;
        if (hud9 == null)
            return true;
        if (hud10 == null)
            return true;
        if (nextpage == null)
            return true;
        if (lastpage == null)
            return true;
        if (join == null)
            return true;
        return hud5 == null;
    }

    public void init() {
        if (head == null)
            head = "<light_purple>[AllMusic3]<yellow>帮助手册";
        if (base == null)
            base = "<light_purple>[AllMusic3]<yellow>/music [音乐ID] 来点歌";
        if (stop == null)
            stop = "<light_purple>[AllMusic3]<yellow>/music stop 停止播放歌曲";
        if (list == null)
            list = "<light_purple>[AllMusic3]<yellow>/music list 查看歌曲队列";
        if (cancel == null)
            cancel = "<light_purple>[AllMusic3]<yellow>/music cancel (序号) 取消你的点歌";
        if (vote == null)
            vote = "<light_purple>[AllMusic3]<yellow>/music vote 投票切歌";
        if (vote1 == null)
            vote1 = "<light_purple>[AllMusic3]<yellow>/music vote cancel 取消发起的切歌";
        if (push == null)
            push = "<light_purple>[AllMusic3]<yellow>/music push (序号) 投票将歌曲插入到队列头";
        if (push1 == null)
            push1 = "<light_purple>[AllMusic3]<yellow>/music push cancel 取消发起的插歌";
        if (mute == null)
            mute = "<light_purple>[AllMusic3]<yellow>/music mute 不再参与点歌";
        if (mutelist == null)
            mutelist = "<light_purple>[AllMusic3]<yellow>/music mute list 不再接收空闲列表点歌";
        if (search == null)
            search = "<light_purple>[AllMusic3]<yellow>/music search [歌名] 搜索歌曲";
        if (select == null)
            select = "<light_purple>[AllMusic3]<yellow>/music select [序列] 选择歌曲";
        if (nextpage == null)
            nextpage = "<light_purple>[AllMusic3]<yellow>/music nextpage 切换下一页歌曲搜索结果";
        if (lastpage == null)
            lastpage = "<light_purple>[AllMusic3]<yellow>/music lastpage 切换上一页歌曲搜索结果";
        if (hud9 == null)
            hud9 = "<light_purple>[AllMusic3]<yellow>/music hud enable 启用/关闭全部界面";
        if (hud10 == null)
            hud10 = "<light_purple>[AllMusic3]<yellow>/music hud reset 重置全部界面";
        if (hud1 == null)
            hud1 = "<light_purple>[AllMusic3]<yellow>/music hud [位置] enable 启用关闭单一界面";
        if (hud2 == null)
            hud2 = "<light_purple>[AllMusic3]<yellow>/music hud [位置] pos [x] [y] 设置某个界面的位置";
        if (hud6 == null)
            hud6 = "<light_purple>[AllMusic3]<yellow>/music hud [位置] dir [对齐方式] 设置某个界面的对齐方式";
        if (hud7 == null)
            hud7 = "<light_purple>[AllMusic3]<yellow>/music hud [位置] color [颜色HEX] 设置某个界面的颜色";
        if (hud8 == null)
            hud8 = "<light_purple>[AllMusic3]<yellow>/music hud [位置] reset 重置单一界面";
        if (hud3 == null)
            hud3 = "<light_purple>[AllMusic3]<yellow>/music hud pic size [尺寸] 设置图片尺寸";
        if (hud4 == null)
            hud4 = "<light_purple>[AllMusic3]<yellow>/music hud pic rotate [开关] 设置图片旋转模式";
        if (hud5 == null)
            hud5 = "<light_purple>[AllMusic3]<yellow>/music hud pic speed [数值] 设置图片旋转速度";
        if (join == null)
            join = "<light_purple>[AllMusic3]<yellow>/music join 重新播放当前歌曲";
    }
}
