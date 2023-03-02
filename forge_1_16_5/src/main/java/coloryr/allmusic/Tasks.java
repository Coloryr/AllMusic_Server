package coloryr.allmusic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tasks {
    public static List<TaskItem> taskItems = new ArrayList<>();

    public static void init() {
        AllMusicForge.server.addTickable(Tasks::tick);
    }

    public static void tick() {
        Iterator<TaskItem> li = taskItems.iterator();
        while (li.hasNext()) {
            TaskItem item = li.next();
            item.tick--;
            if (item.tick == 0) {
                li.remove();
                item.run.run();
            }
        }
    }
}
