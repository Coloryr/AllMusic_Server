package Color_yr.ALLMusic;

import Color_yr.ALLMusic.Message.MessageOBJ;
import Color_yr.ALLMusic.MusicAPI.IMusic;
import Color_yr.ALLMusic.MusicAPI.MusicAPI1.API1;
import Color_yr.ALLMusic.MusicAPI.MusicAPI2.API2;
import Color_yr.ALLMusic.MusicAPI.SongInfo.SongInfo;
import Color_yr.ALLMusic.MusicAPI.SongSearch.SearchPage;
import Color_yr.ALLMusic.Side.ISide;
import Color_yr.ALLMusic.Side.SideBukkit.VVGet;
import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ALLMusic {
    public static final String channel = "allmusic:channel";
    public static final String Version = "1.11.0";

    public final static Map<String, SearchPage> SearchSave = new HashMap<>();
    private static final List<String> haveMOD = new ArrayList<>();
    public static final List<String> VotePlayer = new ArrayList<>();
    public static final List<String> NowPlayPlayer = new ArrayList<>();
    public static final List<SongInfo> PlayList = new ArrayList<>();

    public static Logger log;

    public static ConfigOBJ Config;
    public static MessageOBJ Message;

    public static File ConfigFile;
    public static File MessageFile;

    public static ISide Side;
    public static VVGet VV;
    public static IMusic Music;

    public static void AddPlayer(String player) {
        if (ALLMusic.Config.isModCheck())
            if (!haveMOD.contains(player))
                haveMOD.add(player);
    }

    public static void RemovePlayer(String player) {
        if (ALLMusic.Config.isModCheck())
            haveMOD.remove(player);
    }

    public static boolean havelPlayer(String player) {
        if (ALLMusic.Config.isModCheck())
            return haveMOD.contains(player);
        return true;
    }

    public static void save() {
        try {
            String data = new Gson().toJson(ALLMusic.Config);
            if (ALLMusic.ConfigFile.exists()) {
                Writer out = new FileWriter(ALLMusic.ConfigFile);
                out.write(data);
                out.close();
            }
        } catch (Exception e) {
            ALLMusic.log.warning("§d[ALLMusic]§c配置文件错误");
            e.printStackTrace();
        }
    }

    public static void LoadConfig() throws FileNotFoundException {

        InputStreamReader reader = new InputStreamReader(new FileInputStream(ALLMusic.ConfigFile), StandardCharsets.UTF_8);
        BufferedReader bf = new BufferedReader(reader);
        ALLMusic.Config = new Gson().fromJson(bf, ConfigOBJ.class);

        if (ALLMusic.Config == null) {
            ALLMusic.log.warning("§d[ALLMusic]§c配置文件错误");
            ALLMusic.Config = new ConfigOBJ();
        }

        reader = new InputStreamReader(new FileInputStream(ALLMusic.MessageFile), StandardCharsets.UTF_8);
        bf = new BufferedReader(reader);
        ALLMusic.Message = new Gson().fromJson(bf, MessageOBJ.class);

        if (ALLMusic.Config == null) {
            ALLMusic.log.warning("§d[ALLMusic]§c语言文件错误");
            ALLMusic.Message = new MessageOBJ();
        }

        ALLMusic.log.info("§d[ALLMusic]§e当前插件版本为：" + ALLMusic.Version
                + "，你的配置文件版本为：" + ALLMusic.Config.getVersion());

        for (String item : ALLMusic.Config.getNoMusicPlayer()) {
            ALLMusic.log.info("玩家：" + item + "不参与点歌");
        }

        for (String item : ALLMusic.Config.getNoMusicServer()) {
            ALLMusic.log.info("服务器：" + item + "不参与点歌");
        }
        initAPI();
    }

    private static void initAPI() {
        switch (ALLMusic.Config.getMusic_Api()) {
            case 2: {
                ALLMusic.Music = new API2();
                break;
            }
            case 1:
            default: {
                ALLMusic.Music = new API1();
                break;
            }
        }
    }
}
