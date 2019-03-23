package Color_yr.ALLmusic;

import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.io.BufferedInputStream;
import java.io.IOException;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import java.util.Map;

public class PlayMusic {
    static Map<String, String> playlist = new HashMap<String, String>();
    static Runnable playgo = new playgo();
    public static void PlayMusic_Start()
    {
        playgo.run();
    }
    public static int Music_Time(String music) {
        try {
            URL urlfile = new URL(music);
            URLConnection con = null;

            con = urlfile.openConnection();

            int b = con.getContentLength();//
            BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
            Bitstream bt = new Bitstream(bis);
            Header h = bt.readFrame();
            int time = (int) h.total_ms(b);
            System.out.println(time / 1000);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BitstreamException e)
        {
            e.printStackTrace();
        }
        return 0;
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
                ALLmusic_BC.log.info("音乐时长"+PlayMusic.Music_Time(playsong));
            }
        }
    }
}
