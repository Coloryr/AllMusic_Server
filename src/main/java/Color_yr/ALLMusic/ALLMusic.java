package Color_yr.ALLMusic;

import Color_yr.ALLMusic.MusicAPI.IMusic;
import Color_yr.ALLMusic.MusicAPI.MusicAPI1.API1;
import Color_yr.ALLMusic.MusicAPI.MusicAPI2.API2;
import Color_yr.ALLMusic.MusicAPI.SongSearch.SearchPage;
import Color_yr.ALLMusic.Side.ISide;
import Color_yr.ALLMusic.Side.SideBukkit.VV.VVGet;
import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ALLMusic {
    public static final String channel = "allmusic:channel";
    public static final String Version = "1.10.0";
    public final static Map<String, SearchPage> SearchSave = new HashMap<>();

    public static ConfigOBJ Config;
    public static Logger log;
    public static File ConfigFile;
    public static ISide Side;
    public static VVGet VV;
    public static IMusic Music;

    public static void save() {
        try {
            String data = new Gson().toJson(ALLMusic.Config);
            if (ALLMusic.ConfigFile.exists()) {
                Writer out = new FileWriter(ALLMusic.ConfigFile);
                out.write(data);
                out.close();
            }
        } catch (Exception e) {
            ALLMusic.log.warning("§c配置文件错误");
            e.printStackTrace();
        }
    }

    public static void LoadConfig() throws FileNotFoundException {

        InputStreamReader reader = new InputStreamReader(new FileInputStream(ALLMusic.ConfigFile), StandardCharsets.UTF_8);
        BufferedReader bf = new BufferedReader(reader);
        ALLMusic.Config = new Gson().fromJson(bf, ConfigOBJ.class);

        if (ALLMusic.Config == null) {
            ALLMusic.log.warning("配置文件错误");
            ALLMusic.Config = new ConfigOBJ();
        }

        ALLMusic.log.info("§e当前插件版本为：" + ALLMusic.Version
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
