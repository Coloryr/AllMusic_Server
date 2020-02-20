package Color_yr.ALLMusic.Play;

import Color_yr.ALLMusic.Lyric.LyricDo;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class PlayMusic {

    public static List<String> PlayList = new ArrayList<>();
    public static List<String> Vote = new ArrayList<String>();
    public static List<String> NowPlay = new ArrayList<>();

    public static int Vote_time = 0;
    public static int MusicAllTime = 0;
    public static int MusicNowTime = 0;
    public static String NowPlayMusic;

    public static LyricDo Lyric;
    public static boolean haveLyric;
    public static String nowLyric = "";

    private static Thread PlayGo = new PlayGo();

    public static void Start() {
        PlayGo.start();
    }
}

