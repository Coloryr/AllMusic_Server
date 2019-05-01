package Color_yr.ALLmusic;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.AlgorithmConstraints;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.io.BufferedInputStream;
import java.io.IOException;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Map;

public class PlayMusic {
    static Map<Integer, String> playlist = new HashMap<Integer, String>();
    public static Map<String, String> Vote = new HashMap<String, String>();
    public static Map<String, Integer> stop = new HashMap<String, Integer>();

    public static int Vote_time = 0;
    public static int All_music = 0;
    public static int Music_time = 0;

    static Thread playgo = new playgo();

    public static void PlayMusic_Start() {
        playgo.start();
    }

    public static void SendToPlayer(String data)
    {
        Collection<ProxiedPlayer> values = ProxyServer.getInstance().getPlayers();
        Iterator<ProxiedPlayer> iterator = values.iterator();
        while (iterator.hasNext()) {
            ProxiedPlayer players = iterator.next();
            if(stop.get(players.getDisplayName())!=2)
            players.sendData("AudioBuffer", data.getBytes());
        }
    }
    public static void SendToOnePlayer(String data, String Player)
    {
        ProxiedPlayer players = ProxyServer.getInstance().getPlayer(Player);
        players.sendData("AudioBuffer", data.getBytes());
    }
    public static int Music_Time(String music) {
        try {
            URL urlfile = new URL(music);
            URLConnection con = urlfile.openConnection();
            int b = con.getContentLength();// 得到音乐文件的总长度
            BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
            Bitstream bt = new Bitstream(bis);
            Header h = bt.readFrame();
            int time = (int) h.total_ms(b);
            return time / 1000;
        } catch (Exception e) {
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
            }
        } catch (NullPointerException e) {
            e.getMessage();
            return null;
        }
        return null;
    }
}

class playgo extends Thread {
    public static void music_list() {
        if (PlayMusic.playlist.size() == 1) {
            String a = PlayMusic.playlist.get(1);
            PlayMusic.playlist.put(0, a);
            PlayMusic.playlist.remove(1);
        } else if (PlayMusic.playlist.size() > 1) {
            int i = 0;
            for (; PlayMusic.playlist.size() - 1 > i; i++) {
                String a = PlayMusic.playlist.get(String.valueOf(i + 1));
                PlayMusic.playlist.put(i, a);
            }
            PlayMusic.playlist.remove(String.valueOf(PlayMusic.All_music - 1));
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
                String playsong = PlayMusic.playlist.get(0);
                String song = PlayMusic.realURL(ALLmusic_BC.Music_Api1 + playsong);
                if (song != null) {
                    PlayMusic.Music_time = PlayMusic.Music_Time(song);
                    ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLmusic]§2" + "正在播放歌曲" + playsong));
                    PlayMusic.SendToPlayer("[Net]" + song);
                    PlayMusic.playlist.remove(0);
                    music_list();
                    try {
                        while (PlayMusic.Music_time > 0) {
                            Thread.sleep(1000);
                            PlayMusic.Music_time--;
                            if (PlayMusic.Vote_time > 0) {
                                PlayMusic.Vote_time--;
                                int players = ProxyServer.getInstance().getOnlineCount();
                                if (PlayMusic.Vote.size() >= ALLmusic_BC.min_vote || (players < ALLmusic_BC.min_vote && players == PlayMusic.Vote.size())) {
                                    ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLmusic]§2" + "已切歌"));
                                    PlayMusic.SendToPlayer("[Stop]");
                                    PlayMusic.Music_time = 0;
                                    if (PlayMusic.All_music == 0) {
                                        ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLmusic]§2" + "队列中无歌曲"));
                                    }
                                    PlayMusic.Vote_time = 0;
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.getMessage();
                    }
                } else {
                    ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLmusic]§2" + "无效歌曲歌曲" + playsong));
                    PlayMusic.playlist.remove(0);
                    music_list();
                }
            }
        }
    }
}
