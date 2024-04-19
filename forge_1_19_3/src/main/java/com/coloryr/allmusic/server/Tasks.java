package com.coloryr.allmusic.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

public class Tasks {
    private static final List<TaskItem> taskItems = new CopyOnWriteArrayList<>();

    public static void init() {
        AllMusicForge.server.addTickable(Tasks::tick);
    }

    public static void tick() {
        synchronized (taskItems) {
            for(TaskItem item : taskItems){
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
