package com.coloryr.allmusic.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Tasks {
    private static final List<TaskItem> taskItems = new CopyOnWriteArrayList<>();

    public static void init() {
        AllMusicFabric.server.addServerGuiTickable(Tasks::tick);
    }

    public static void tick() {
        for (var item : new ArrayList<>(taskItems)) {
            item.tick--;
            if (item.tick <= 0) {
                taskItems.remove(item);
                item.run.run();
            }
        }
    }

    public static void add(TaskItem item) {
        taskItems.add(item);
    }
}
