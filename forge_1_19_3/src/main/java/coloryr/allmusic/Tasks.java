package coloryr.allmusic;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

public class Tasks {
    private static final List<TaskItem> taskItems = new ArrayList<>();

    public static void init() {
        AllMusicForge.server.addTickable(Tasks::tick);
    }

    public static void tick() {
        synchronized (taskItems) {
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

    public static void add(TaskItem item) {
        synchronized (taskItems) {
            taskItems.add(item);
        }
    }
}
