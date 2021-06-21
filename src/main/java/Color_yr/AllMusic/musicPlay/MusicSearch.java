package Color_yr.AllMusic.musicPlay;

import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.command.CommandEX;
import Color_yr.AllMusic.musicAPI.songSearch.SearchPage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MusicSearch {
    private static Thread taskT;
    private static boolean isRun;
    private static List<MusicObj> tasks = new CopyOnWriteArrayList<>();

    private static final Runnable Do = () -> {
        while (isRun) {
            try {
                if (!tasks.isEmpty()) {
                    MusicObj obj = tasks.remove(0);
                    SearchPage search = AllMusic.getMusicApi().search(obj.args, obj.isDefault);
                    if (search == null)
                        AllMusic.Side.sendMessaget(obj.sender, AllMusic.getMessage().getSearch()
                                .getCantSearch().replace("%Music%", obj.isDefault ? obj.args[0] : obj.args[1]));
                    else {
                        AllMusic.Side.sendMessaget(obj.sender, AllMusic.getMessage().getSearch().getRes());
                        AllMusic.addSearch(obj.Name, search);
                        AllMusic.Side.runTask(() -> CommandEX.showSearch(obj.sender, search));
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

    public static void addSearch(MusicObj obj) {
        tasks.add(obj);
    }
}
