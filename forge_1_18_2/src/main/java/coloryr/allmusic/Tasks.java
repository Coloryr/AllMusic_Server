package coloryr.allmusic;

import java.util.ArrayList;
import java.util.List;

public class Tasks {
    public static List<TaskItem> taskItems = new ArrayList<>();

    public static void init() {
        AllMusicForge.server.addTickable(Tasks::tick);
    }

    public static void tick() {
        var li = taskItems.iterator();
        while (li.hasNext()) {
            var item = li.next();
            item.tick--;
            if (item.tick == 0) {
                li.remove();
                item.run.run();
            }
        }
    }
}
