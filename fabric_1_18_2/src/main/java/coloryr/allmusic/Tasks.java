package coloryr.allmusic;

import coloryr.allmusic.core.AllMusic;

import java.util.ArrayList;
import java.util.List;

public class Tasks {
    public static List<TaskItem> taskItems = new ArrayList<>();

    public static void init() {
        AllMusicFabric.server.addServerGuiTickable(Tasks::tick);
    }

    public static void tick() {
        for (int index = 0; index < taskItems.size(); index++) {
            var item = taskItems.get(index);
            item.tick --;
            if(item.tick == 0){
                taskItems.remove(item);
                item.run.run();
            }
        }
    }
}
