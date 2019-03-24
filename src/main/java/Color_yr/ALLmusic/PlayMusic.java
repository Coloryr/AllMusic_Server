package Color_yr.ALLmusic;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.io.BufferedInputStream;
import java.io.IOException;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Map;

public class PlayMusic {
    static Map<String, String> playlist = new HashMap<String, String>();
    static Thread playgo = new playgo();
    private static Integer warningTimes;
    public static int Mode = 0;
    public static int All_music = 0;
    public static String Music_name;

    public static void PlayMusic_Start() {
        playgo.start();
    }

    public static int Music_Time(String music){
        try {
            URL urlfile = new URL(music);
            URLConnection con = urlfile.openConnection();
            int b = con.getContentLength();// 得到音乐文件的总长度
            BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
            Bitstream bt = new Bitstream(bis);
            Header h = bt.readFrame();
            int time = (int) h.total_ms(b);
            return time / 1000;
        }catch (Exception e)
        {
            e.getMessage();
        }
        return 0;
    }

    public static String realURL(String path) {
        HttpURLConnection connection = null;

        try {
            URL realUrl = new URL(path);
            connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            connection.getContent();
            return connection.getURL().toString();
        } catch (IOException var5) {
            if (connection == null) {
                var5.printStackTrace();
                return null;
            } else {
                if (warningTimes > 0) {
                    Integer var3 = warningTimes;
                    warningTimes = warningTimes - 1;
                    ALLmusic_BC.log.warning("> 警告: 网页请求反馈出错.");
                }

                return connection.getURL().toString();
            }
        }
    }
}

class playgo extends Thread {
    public static void music_list() {
        if (PlayMusic.playlist.size() != 0) {
            int i = 0;
            for (; PlayMusic.playlist.size() - 1 > i; i++) {
                String a = PlayMusic.playlist.get("" + (i + 1));
                ALLmusic_BC.log.info("处理队列" + "[" + (i + 1) + "]" + a);
                PlayMusic.playlist.put("" + i, a);
                ALLmusic_BC.log.info("到队列" + "[" + i + "]" + a);
            }
            PlayMusic.playlist.remove("" + (PlayMusic.playlist.size()));
            ALLmusic_BC.log.info("删除队列" + (PlayMusic.playlist.size()));
        }
        PlayMusic.All_music = PlayMusic.playlist.size();
    }
    public synchronized void run() {
        while (true) {
            if (PlayMusic.playlist.size() == 0) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.getMessage();
                }
            } else {
                String playsong = PlayMusic.playlist.get("0");
                String song = PlayMusic.realURL("http://music.163.com/song/media/outer/url?id=" + playsong);
                int Music_time = PlayMusic.Music_Time(song);
                ALLmusic_BC.log.info("音乐时长"+Music_time);
                ChannelListener.sendToBukkit("play", playsong);
                ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLmusic_BC]§2"+"正在播放歌曲"+playsong));
                PlayMusic.playlist.remove("0");
                ALLmusic_BC.log.info("删除队列" + "[0]");
                music_list();
                try {
                    while(Music_time>0)
                    {
                        Thread.sleep(1000);
                        Music_time -- ;
                        if(PlayMusic.Mode==1)
                        {
                            ChannelListener.sendToBukkit("stopALL", "");
                            music_list();
                            Music_time=0;
                            if(PlayMusic.All_music==0)
                            {
                                ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLmusic_BC]§2"+"队列中无歌曲"));
                            }
                            PlayMusic.Mode=0;
                        }
                    }
                } catch (InterruptedException e) {
                    e.getMessage();
                }
            }
        }
    }
}
