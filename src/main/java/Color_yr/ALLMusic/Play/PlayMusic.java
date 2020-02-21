package Color_yr.ALLMusic.Play;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Lyric.LyricDo;
import Color_yr.ALLMusic.Song.GetInfo;
import Color_yr.ALLMusic.Song.Info;
import Color_yr.ALLMusic.Utils.logs;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;
import org.bukkit.Bukkit;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class PlayMusic {

    public static List<Info> PlayList = new ArrayList<>();
    public static List<String> Vote = new ArrayList<>();
    public static List<String> NowPlay = new ArrayList<>();

    public static int Vote_time = 0;
    public static int MusicAllTime = 0;
    public static int MusicNowTime = 0;
    public static Info NowPlayMusic;

    public static LyricDo Lyric;
    public static boolean haveLyric;
    public static String nowLyric = "";

    private static Thread PlayGo = new PlayGo();

    public static void Start() {
        PlayGo.start();
    }

    public static void AddMusic(String ID, String player) {
        ALLMusic.Side.bq("§d[ALLMusic]§2" + player +
                "点歌" + ID);
        logs.log_write("玩家：" + player + " 点歌：" + ID);
        Info info = new Info(null, null, ID, null, player);
        try {
            info = GetInfo.Get(ID, player);
        } catch (Exception e) {
            ALLMusic.log.warning("§d[ALLMusic]§c歌曲信息解析错误");
            e.printStackTrace();
        }
        PlayList.add(info);
    }

    public static String getList() {
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < PlayList.size(); i++) {
            Info info = PlayList.get(i);
            list.append("§2").append(i).append("->").append(info.getInfo()).append("\n");
        }
        return list.toString();
    }
}

