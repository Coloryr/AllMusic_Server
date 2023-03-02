package coloryr.allmusic.core.music.play;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.command.CommandEX;
import coloryr.allmusic.core.objs.music.MusicObj;
import coloryr.allmusic.core.objs.music.SearchPageObj;

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
                        AllMusic.side.sendMessaget(obj.sender, AllMusic.getMessage().Search
                                .CantSearch.replace("%Music%", obj.isDefault ? obj.args[0] : obj.args[1]));
                    else {
                        AllMusic.side.sendMessaget(obj.sender, AllMusic.getMessage().Search.Res);
                        AllMusic.addSearch(obj.name, search);
                        AllMusic.side.runTask(() -> CommandEX.showSearch(obj.sender, search));
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
}
