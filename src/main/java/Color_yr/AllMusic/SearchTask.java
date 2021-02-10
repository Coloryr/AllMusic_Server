package Color_yr.AllMusic;

import Color_yr.AllMusic.Command.CommandEX;
import Color_yr.AllMusic.MusicAPI.SongSearch.SearchPage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SearchTask {
    private static Thread taskT;
    private static boolean isRun;
    private static List<TaskObj> tasks = new CopyOnWriteArrayList<>();

    private static final Runnable Do = () -> {
        while (isRun) {
            try {
                if (!tasks.isEmpty()) {
                    TaskObj obj = tasks.remove(0);
                    SearchPage search = AllMusic.getMusic().Search(obj.args, obj.isDefault);
                    if (search == null)
                        AllMusic.Side.SendMessaget(obj.sender, AllMusic.getMessage().getSearch()
                                .getCantSearch().replace("%Music%", obj.isDefault ? obj.args[0] : obj.args[1]));
                    else {
                        AllMusic.Side.SendMessaget(obj.sender, AllMusic.getMessage().getSearch().getRes());
                        AllMusic.addSearch(obj.Name, search);
                        AllMusic.Side.RunTask(() -> CommandEX.ShowSearch(obj.sender, search));
                    }
                }
                Thread.sleep(100);
            } catch (Exception e) {
                AllMusic.log.warning("搜歌出现问题");
                e.printStackTrace();
            }
        }
    };

    public static void start() {
        taskT = new Thread(Do);
        isRun = true;
        taskT.start();
    }

    public static void stop() {
        isRun = false;
    }

    public static void addSearch(TaskObj obj) {
        tasks.add(obj);
    }
}
