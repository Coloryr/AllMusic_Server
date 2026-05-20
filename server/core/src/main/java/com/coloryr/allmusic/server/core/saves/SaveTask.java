package com.coloryr.allmusic.server.core.saves;

import com.coloryr.allmusic.server.core.AllMusic;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

public class SaveTask {
    private static final Queue<Runnable> tasks = new LinkedBlockingDeque<>();
    private static final Semaphore semaphore = new Semaphore(0);

    public static void start() {
        new Thread(SaveTask::run).start();
    }

    public static void task(Runnable runnable) {
        tasks.add(runnable);
        semaphore.release();
    }

    /**
     * 停止
     */
    public static void stop() {
        semaphore.release();
    }

    private static void run() {
        AllMusic.log.data("数据库线程启动");
        Runnable runnable;
        while (AllMusic.isRun) {
            try {
                semaphore.acquire();
                if (!AllMusic.isRun)
                    break;
                do {
                    runnable = tasks.poll();
                    if (runnable != null) {
                        runnable.run();
                    }
                }
                while (runnable != null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        AllMusic.log.data("数据库线程关闭");
    }
}
