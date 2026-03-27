package com.coloryr.allmusic.server.core.music;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.IMusicApi;
import com.coloryr.allmusic.server.core.objs.SearchMusicObj;
import com.coloryr.allmusic.server.core.objs.message.ARG;
import com.coloryr.allmusic.server.core.objs.music.PlayerAddMusicObj;
import com.coloryr.allmusic.server.core.objs.music.SearchPageObj;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MusicSearch {
    private static final Queue<PlayerAddMusicObj> tasks = new ConcurrentLinkedQueue<>();

    private static void task() {
        AllMusic.log.data("歌曲搜索线程启动");
        while (AllMusic.isRun) {
            try {
                PlayerAddMusicObj obj = tasks.poll();
                if (obj != null) {
                    IMusicApi api = AllMusic.MUSIC_APIS.get(obj.api);
                    if (api == null) {
                        AllMusic.side.sendMessageTask(obj.sender, AllMusic.getMessage().musicPlay.error2);
                        return;
                    }
                    SearchPageObj search = api.search(obj.args, obj.isDefault);
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
                AllMusic.log.data("搜歌出现问题");
                e.printStackTrace();
            }
        }
        AllMusic.log.data("歌曲搜索线程停止");
    }

    public static void start() {
        new Thread(MusicSearch::task, "allmusic_search").start();
    }

    public static void addSearch(PlayerAddMusicObj obj) {
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
            AllMusic.side.sendMessage(sender, AllMusic.miniMessage(AllMusic.getMessage().search.lastPage)
                    .append(AllMusic.miniMessageRun(AllMusic.getMessage().page.last, "/music lastpage")));
        }
        for (int a = 0; a < index; a++) {
            item = search.getRes(a + search.getPage() * 10);
            info = AllMusic.getMessage().page.choice;
            info = info.replace(ARG.index, "" + (a + 1))
                    .replace(ARG.musicName, item.name)
                    .replace(ARG.musicAuthor, item.author)
                    .replace(ARG.musicAl, item.al);
            AllMusic.side.sendMessage(sender, AllMusic.miniMessage(info)
                    .append(AllMusic.miniMessageRun(AllMusic.getMessage().click.clickRun, "/music select " + (a + 1))));
        }
        if (search.haveNextPage()) {
            AllMusic.side.sendMessage(sender, AllMusic.miniMessage(AllMusic.getMessage().search.nextPage)
                    .append(AllMusic.miniMessageRun(AllMusic.getMessage().page.next, "/music nextpage")));
        }
        AllMusic.side.sendMessage(sender, "");
    }
}
