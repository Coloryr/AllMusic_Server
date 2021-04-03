package Color_yr.AllMusic;

import Color_yr.AllMusic.API.IMusicAPI;
import Color_yr.AllMusic.API.IMyLogger;
import Color_yr.AllMusic.API.ISide;
import Color_yr.AllMusic.Message.*;
import Color_yr.AllMusic.MusicAPI.MusicAPI1.API1;
import Color_yr.AllMusic.MusicAPI.MusicAPI2.API2;
import Color_yr.AllMusic.MusicAPI.SongSearch.SearchPage;
import Color_yr.AllMusic.MusicPlay.PlayMusic;
import Color_yr.AllMusic.MusicPlay.MusicSearch;
import Color_yr.AllMusic.Side.SideBukkit.VaultHook;
import Color_yr.AllMusic.Utils.logs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllMusic {
    public static final String channel = "allmusic:channel";
    public static final String Version = "2.11.0";

    private static final Map<String, SearchPage> SearchSave = new HashMap<>();
    private static final List<String> VotePlayer = new ArrayList<>();
    private static final List<String> NowPlayPlayer = new ArrayList<>();

    public static IMyLogger log;
    public static ISide Side;
    public static boolean isRun;
    public static VaultHook Vault;
    private static IMusicAPI Music;
    private static ConfigOBJ Config;
    private static MessageOBJ Message;
    public static CookieObj Cookie;
    private static File ConfigFile;
    private static File CookieFile;
    private static File MessageFile;

    public static void configCheck() {
        if (Config == null) {
            Config = new ConfigOBJ();
            log.warning("§d[AllMusic]§c配置文件Config.json错误，已覆盖");
            save();
        }
        else if (Config.check()) {
            log.warning("§d[AllMusic]§c配置文件Config.json错误，已覆盖");
            save();
        }
    }
    private static void messageCheck() {
        if (Message == null) {
            Message = new MessageOBJ();
            log.warning("§d[AllMusic]§c配置文件Message.json错误，已覆盖");
            save();
        }
        else if (Message.check()) {
            log.warning("§d[AllMusic]§c配置文件Message.json错误，已覆盖");
            save();
        }
    }

    public static boolean containNowPlay(String player) {
        return NowPlayPlayer.contains(player);
    }

    public static ConfigOBJ getConfig() {
        return Config;
    }

    public static MessageOBJ getMessage() {
        return Message;
    }

    public static void addSearch(String player, SearchPage page) {
        SearchSave.put(player, page);
    }

    public static SearchPage getSearch(String player) {
        return SearchSave.get(player);
    }

    public static void removeSearch(String player) {
        SearchSave.remove(player);
    }

    public static void addVote(String player) {
        if (!VotePlayer.contains(player))
            VotePlayer.add(player);
    }

    public static int getVoteCount() {
        return VotePlayer.size();
    }

    public static void clearVote() {
        VotePlayer.clear();
    }

    public static boolean containVote(String player) {
        return VotePlayer.contains(player);
    }

    public static void addNowPlayPlayer(String player) {
        if (!NowPlayPlayer.contains(player))
            NowPlayPlayer.add(player);
    }

    public static void removeNowPlayPlayer(String player) {
        NowPlayPlayer.remove(player);
    }

    public static void save() {
        try {
            String data = new GsonBuilder().setPrettyPrinting().create().toJson(Config);
            FileOutputStream out = new FileOutputStream(ConfigFile);
            OutputStreamWriter write = new OutputStreamWriter(
                    out, StandardCharsets.UTF_8);
            write.write(data);
            write.close();
            data = new GsonBuilder().setPrettyPrinting().create().toJson(Message);
            out = new FileOutputStream(MessageFile);
            write = new OutputStreamWriter(
                    out, StandardCharsets.UTF_8);
            write.write(data);
            write.close();
        } catch (Exception e) {
            log.warning("§d[AllMusic]§c配置文件保存错误");
            e.printStackTrace();
        }
    }

    public static void saveCookie() {
        try {
            String data = new GsonBuilder().setPrettyPrinting().create().toJson(Cookie);
            FileOutputStream out = new FileOutputStream(CookieFile);
            OutputStreamWriter write = new OutputStreamWriter(
                    out, StandardCharsets.UTF_8);
            write.write(data);
            write.close();
        } catch (Exception e) {
            log.warning("§d[AllMusic]§c配置文件保存错误");
            e.printStackTrace();
        }
    }

    public static void start() {
        PlayMusic.start();
        MusicSearch.start();
        log.info("§d[AllMusic]§2§e已启动-" + Version);
    }

    public static void stop() {
        try {
            clearVote();
            logs.stop();
            Side.Send("[Stop]", false);
            MusicSearch.stop();
            PlayMusic.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("§d[AllMusic]§2§e已停止，感谢使用");
    }

    public static IMusicAPI getMusic() {
        if (Music == null) {
            initAPI();
        }
        return Music;
    }

    private static void initAPI() {
        switch (AllMusic.Config.getMusic_Api()) {
            case 2: {
                AllMusic.Music = new API1();
                break;
            }
            case 1:
            default: {
                AllMusic.Music = new API2();
                break;
            }
        }
    }

    private static void LoadConfig() {
        try {
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(ConfigFile), StandardCharsets.UTF_8);
            BufferedReader bf = new BufferedReader(reader);
            Config = new Gson().fromJson(bf, ConfigOBJ.class);
            bf.close();
            reader.close();
            configCheck();

            reader = new InputStreamReader(new FileInputStream(AllMusic.MessageFile), StandardCharsets.UTF_8);
            bf = new BufferedReader(reader);
            Message = new Gson().fromJson(bf, MessageOBJ.class);
            bf.close();
            reader.close();
            messageCheck();

            reader = new InputStreamReader(new FileInputStream(AllMusic.CookieFile), StandardCharsets.UTF_8);
            bf = new BufferedReader(reader);
            Cookie = new Gson().fromJson(bf, CookieObj.class);
            bf.close();
            reader.close();
            if (Cookie == null) {
                Cookie = new CookieObj();
                saveCookie();
            }

            log.info("§d[AllMusic]§e当前插件版本为：" + AllMusic.Version
                    + "，你的配置文件版本为：" + AllMusic.Config.getVersion());

            if (!AllMusic.Version.equalsIgnoreCase(AllMusic.Config.getVersion())) {
                log.warning("§d[AllMusic]§c请及时更新配置文件");
            }
            initAPI();
        } catch (Exception e) {
            log.warning("§d[AllMusic]§c读取配置文件错误");
            e.printStackTrace();
        }
    }

    public void init(File file) {
        log.info("§d[AllMusic]§2§e正在启动，感谢使用，本插件交流群：571239090");
        try {
            if (ConfigFile == null)
                ConfigFile = new File(file, "config.json");
            if (MessageFile == null)
                MessageFile = new File(file, "Message.json");
            if(CookieFile == null)
                CookieFile = new File(file, "cookie.json");
            if (logs.file == null)
                logs.file = new File(file, "logs.log");
            if (!file.exists())
                file.mkdir();
            if (!ConfigFile.exists()) {
                Files.copy(this.getClass().getResourceAsStream("/config.json"), ConfigFile.toPath());
            }
            if (!MessageFile.exists()) {
                Files.copy(this.getClass().getResourceAsStream("/Message.json"), MessageFile.toPath());
            }
            if (!CookieFile.exists()) {
                CookieFile.createNewFile();
            }
            if (!logs.file.exists()) {
                logs.file.createNewFile();
            }
            LoadConfig();
            isRun = true;
        } catch (IOException e) {
            isRun = false;
            log.warning("§d[AllMusic]§c启动失败");
            e.printStackTrace();
        }
    }
}
