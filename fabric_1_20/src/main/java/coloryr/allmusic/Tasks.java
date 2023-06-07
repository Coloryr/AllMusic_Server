package coloryr.allmusic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Tasks {
    private static final List<TaskItem> taskItems = new CopyOnWriteArrayList<>();

    public static void init() {
        AllMusicFabric.server.addServerGuiTickable(Tasks::tick);
    }

    public static void tick() {
        synchronized (taskItems) {
            for(var item : taskItems){
                item.tick--;
                if (item.tick == 0) {
                    taskItems.remove(item);
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
