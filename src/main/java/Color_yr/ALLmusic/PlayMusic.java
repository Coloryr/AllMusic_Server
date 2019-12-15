package Color_yr.ALLmusic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PlayMusic {

    public static Map<Integer, String> playlist = new HashMap<Integer, String>();
    public static List<String> Vote = new ArrayList<String>();
    public static Map<String, Boolean> stop = new HashMap<String, Boolean>();

    public static int Vote_time = 0;
    public static int All_music = 0;
    public static int Music_time = 0;
    public static String now_music;

    private static Thread playgo = new playgo();

    static void PlayMusic_Start() {
        playgo.start();
    }

    static void SendToPlayer(String data) {
        Collection<ProxiedPlayer> values = ProxyServer.getInstance().getPlayers();
        try {
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
            buf.writeByte(666);
            buf.writeBytes(bytes);
            for (ProxiedPlayer players : values) {
                if (!ALLmusic_BC.server.contains(players.getServer().getInfo().getName())) {
                    if (stop.containsKey(players.getName()) && !stop.get(players.getName()))
                        players.sendData("allmusic:channel", buf.array());
                }
            }
        } catch (Exception e) {
            logs logs = new logs();
            logs.log_write(e.toString());
            logs.log_write(Arrays.toString(e.getStackTrace()));
        }
    }

    static void SendToOnePlayer(String data, String Player) {
        try {
            ProxiedPlayer players = ProxyServer.getInstance().getPlayer(Player);
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
            buf.writeByte(666);
            buf.writeBytes(bytes);
            players.sendData("allmusic:channel", buf.array());
        } catch (Exception ignored) {
        }
    }

    static int Music_Time(String music) {
        try {
            URL urlfile = new URL(music);
            URLConnection con = urlfile.openConnection();
            int b = con.getContentLength();// 得到音乐文件的总长度
            BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
            Bitstream bt = new Bitstream(bis);
            Header h = bt.readFrame();
            int time = (int) h.total_ms(b);
            return time / 1000;
        } catch (Exception ignored) {
            ignored.getMessage();
        }
        return 0;
    }

    static String realURL(String path) {
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

    private static void music_list() {
        String a;
        if (PlayMusic.playlist.size() == 1) {
            a = PlayMusic.playlist.get(1);
            PlayMusic.playlist.put(0, a);
            PlayMusic.playlist.remove(1);
        } else if (PlayMusic.playlist.size() > 1) {
            for (int i = 0; PlayMusic.playlist.size() - 1 > i; i++) {
                a = PlayMusic.playlist.get(i + 1);
                PlayMusic.playlist.put(i, a);
            }
            PlayMusic.playlist.remove(PlayMusic.playlist.size() - 1);
        }
        PlayMusic.All_music = PlayMusic.playlist.size();
    }

    @Override
    public synchronized void run() {
        while (true) {
            if (PlayMusic.playlist.size() == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.getMessage();
                }
            } else {
                PlayMusic.now_music = PlayMusic.playlist.get(0);
                String song = PlayMusic.realURL(ALLmusic_BC.Music_Api1 + PlayMusic.now_music);
                if (song != null) {
                    PlayMusic.Music_time = PlayMusic.Music_Time(song);
                    ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLmusic]§2" + "正在播放歌曲" + PlayMusic.now_music));
                    PlayMusic.SendToPlayer("[Play]" + song);
                    PlayMusic.playlist.remove(0);
                    music_list();
                    try {
                        while (PlayMusic.Music_time > 0) {
                            Thread.sleep(1000);
                            PlayMusic.Music_time--;
                            if (PlayMusic.Vote_time > 0) {
                                PlayMusic.Vote_time--;
                                if (PlayMusic.Vote_time == 0) {
                                    PlayMusic.Vote.clear();
                                    ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLmusic]§2" + "切歌时间结束"));
                                }
                                else {
                                    int players = ProxyServer.getInstance().getOnlineCount();
                                    if (PlayMusic.Vote.size() >= ALLmusic_BC.min_vote || (players <= ALLmusic_BC.min_vote && players == PlayMusic.Vote.size())) {
                                        ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLmusic]§2" + "已切歌"));
                                        PlayMusic.SendToPlayer("[Stop]");
                                        PlayMusic.Music_time = 1;
                                        if (PlayMusic.All_music == 0) {
                                            ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLmusic]§2" + "队列中无歌曲"));
                                        }
                                        PlayMusic.Vote_time = 0;
                                        PlayMusic.now_music = null;
                                    }
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.getMessage();
                    }
                } else {
                    ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLmusic]§2" + "无效歌曲歌曲" + PlayMusic.now_music));
                    PlayMusic.playlist.remove(0);
                    PlayMusic.now_music = null;
                    music_list();
                }
            }
        }
    }
}
