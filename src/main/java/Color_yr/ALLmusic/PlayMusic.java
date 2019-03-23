package Color_yr.ALLmusic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PlayMusic {
    static Map<String, String> playlist = new HashMap<String, String>();
    static Runnable playgo = new playgo();
    public static void PlayMusic_Start()
    {
        playgo.run();
    }
}

class playgo implements Runnable {
    public synchronized void run() {
        while (true) {
            if (PlayMusic.playlist.size() == 0) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.getMessage();
                }
            } else {
                Iterator<String> iterator = PlayMusic.playlist.keySet().iterator();
                String key = iterator.next();
                String playsong = PlayMusic.playlist.get(key);
                ChannelListener.sendToBukkit("play", playsong);
                PlayMusic.playlist.remove(key);
            }
        }
    }
}
