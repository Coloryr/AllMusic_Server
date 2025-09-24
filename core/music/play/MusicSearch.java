package com.coloryr.allmusic.server.core.music.play;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.SearchMusicObj;
import com.coloryr.allmusic.server.core.objs.message.ARG;
import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import com.coloryr.allmusic.server.core.objs.music.SearchPageObj;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MusicSearch {
    private static final Queue<MusicObj> tasks = new ConcurrentLinkedQueue<>();
    private static boolean isRun;

    private static void task() {
        while (isRun) {
            try {
                MusicObj obj = tasks.poll();
                if (obj != null) {
                    SearchPageObj search = AllMusic.getMusicApi().search(obj.args, obj.isDefault);
                    if (search == null)
                        AllMusic.side.sendMessageTask(obj.sender, AllMusic.getMessage().search
                                .cantSearch.replace(ARG.name, obj.isDefault ? obj.args[0] : obj.args[1]));
                    else {
                        AllMusic.side.sendMessageTask(obj.sender, AllMusic.getMessage().search.res);
                        AllMusic.addSearch(obj.name, search);
                        AllMusic.side.runTask(() -> showSearch(obj.sender, search));
                    }
                }
                Thread.sleep(100);
            } catch (Exception e) {
                AllMusic.log.warning("搜歌出现问题");
                e.printStackTrace();
            }
        }
    }

    public static void start() {
        Thread taskT = new Thread(MusicSearch::task, "AllMusic_search");
        isRun = true;
        taskT.start();
    }

    public static void stop() {
        isRun = false;
    }

    public static void addSearch(MusicObj obj) {
        tasks.add(obj);
    }

    /**
     * 展示搜歌结果
     *
     * @param sender 发送者
     * @param search 搜歌结果
     */
    public static void showSearch(Object sender, SearchPageObj search) {
        int index = search.getIndex();
        SearchMusicObj item;
        String info;
        AllMusic.side.sendMessage(sender, "");
        if (search.haveLastPage()) {
            AllMusic.side.sendMessageRun(sender, "§d[AllMusic3]§2输入/music lastpage上一页",
                    AllMusic.getMessage().page.last, "/music lastpage");
        }
        for (int a = 0; a < index; a++) {
            item = search.getRes(a + search.getPage() * 10);
            info = AllMusic.getMessage().page.choice;
            info = info.replace(ARG.index, "" + (a + 1))
                    .replace(ARG.musicName, item.name)
                    .replace(ARG.musicAuthor, item.author)
                    .replace(ARG.musicAl, item.al);
            AllMusic.side.sendMessageRun(sender, info,
                    AllMusic.getMessage().click.clickRun, "/music select " + (a + 1));
        }
        if (search.haveNextPage()) {
            AllMusic.side.sendMessageRun(sender, "§d[AllMusic3]§2输入/music nextpage下一页",
                    AllMusic.getMessage().page.next, "/music nextpage");
        }
        AllMusic.side.sendMessage(sender, "");
    }
}
