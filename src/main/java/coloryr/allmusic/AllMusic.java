package coloryr.allmusic;

import coloryr.allmusic.api.IMyLogger;
import coloryr.allmusic.api.ISide;
import coloryr.allmusic.hud.DataSql;
import coloryr.allmusic.hud.HudSave;
import coloryr.allmusic.hud.obj.SaveOBJ;
import coloryr.allmusic.message.MessageOBJ;
import coloryr.allmusic.music.api.APIMain;
import coloryr.allmusic.music.play.MusicSearch;
import coloryr.allmusic.music.play.PlayGo;
import coloryr.allmusic.music.play.PlayMusic;
import coloryr.allmusic.music.search.SearchPage;
import coloryr.allmusic.side.bukkit.VaultHook;
import coloryr.allmusic.utils.logs;
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
    public static final String channelBC = "allmusic:channelbc";
    public static final String version = "2.16.2";

    private static final Map<String, SearchPage> searchSave = new HashMap<>();
    private static final List<String> votePlayer = new ArrayList<>();
    private static final List<String> nowPlayPlayer = new ArrayList<>();
    public static final Gson gson = new Gson();

    public static IMyLogger log;
    public static ISide side;
    public static boolean isRun;
    public static VaultHook vault;
    private static APIMain apiMusic;
    private static ConfigOBJ config;
    private static MessageOBJ message;
    public static CookieObj cookie;
    private static File configFile;
    private static File cookieFile;
    private static File messageFile;

    public static void configCheck() {
        if (config == null) {
            config = new ConfigOBJ();
            log.warning("§d[AllMusic]§c配置文件config.json错误，已覆盖");
            save();
        } else if (config.check()) {
            log.warning("§d[AllMusic]§c配置文件config.json错误，已覆盖");
            save();
        }
    }

    private static void messageCheck() {
        if (message == null) {
            message = new MessageOBJ();
            log.warning("§d[AllMusic]§c配置文件message.json错误，已覆盖");
            save();
        } else if (message.check()) {
            log.warning("§d[AllMusic]§c配置文件message.json错误，已覆盖");
            save();
        }
    }

    public static boolean containNowPlay(String player) {
        return nowPlayPlayer.contains(player);
    }

    public static ConfigOBJ getConfig() {
        if (config == null) {
            log.warning("§d[AllMusic]§c配置文件config.json错误，已使用默认配置文件");
            config = new ConfigOBJ();
        }
        return config;
    }

    public static MessageOBJ getMessage() {
        return message;
    }

    public static void addSearch(String player, SearchPage page) {
        searchSave.put(player, page);
    }

    public static SearchPage getSearch(String player) {
        return searchSave.get(player);
    }

    public static void removeSearch(String player) {
        searchSave.remove(player);
    }

    public static void addVote(String player) {
        if (!votePlayer.contains(player))
            votePlayer.add(player);
    }

    public static int getVoteCount() {
        return votePlayer.size();
    }

    public static void clearVote() {
        votePlayer.clear();
    }

    public static boolean containVote(String player) {
        return votePlayer.contains(player);
    }

    public static void addNowPlayPlayer(String player) {
        if (!nowPlayPlayer.contains(player))
            nowPlayPlayer.add(player);
    }

    public static void removeNowPlayPlayer(String player) {
        nowPlayPlayer.remove(player);
    }

    public static void save() {
        try {
            String data = new GsonBuilder().setPrettyPrinting().create().toJson(config);
            FileOutputStream out = new FileOutputStream(configFile);
            OutputStreamWriter write = new OutputStreamWriter(
                    out, StandardCharsets.UTF_8);
            write.write(data);
            write.close();
            data = new GsonBuilder().setPrettyPrinting().create().toJson(message);
            out = new FileOutputStream(messageFile);
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
            String data = new GsonBuilder().setPrettyPrinting().create().toJson(cookie);
            FileOutputStream out = new FileOutputStream(cookieFile);
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
        PlayGo.start();
        MusicSearch.start();
        DataSql.start();
        log.info("§d[AllMusic]§2§e已启动-" + version);
    }

    public static void stop() {
        try {
            clearVote();
            logs.stop();
            side.send("[Stop]", false);
            MusicSearch.stop();
            PlayMusic.stop();
            PlayGo.stop();
            DataSql.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("§d[AllMusic]§2§e已停止，感谢使用");
    }

    public static APIMain getMusicApi() {
        if (apiMusic == null) {
            AllMusic.apiMusic = new APIMain();
        }
        return apiMusic;
    }

    private static void loadConfig() {
        try {
            InputStreamReader reader = new InputStreamReader(
                    Files.newInputStream(configFile.toPath()), StandardCharsets.UTF_8);
            BufferedReader bf = new BufferedReader(reader);
            config = new Gson().fromJson(bf, ConfigOBJ.class);
            bf.close();
            reader.close();
            configCheck();

            reader = new InputStreamReader(Files.newInputStream(AllMusic.messageFile.toPath()), StandardCharsets.UTF_8);
            bf = new BufferedReader(reader);
            message = new Gson().fromJson(bf, MessageOBJ.class);
            bf.close();
            reader.close();
            messageCheck();

            reader = new InputStreamReader(Files.newInputStream(AllMusic.cookieFile.toPath()), StandardCharsets.UTF_8);
            bf = new BufferedReader(reader);
            cookie = new Gson().fromJson(bf, CookieObj.class);
            bf.close();
            reader.close();
            if (cookie == null) {
                cookie = new CookieObj();
                saveCookie();
            }

            log.info("§d[AllMusic]§e当前插件版本为：" + AllMusic.version
                    + "，你的配置文件版本为：" + AllMusic.config.getVersion());

            if (!AllMusic.version.equalsIgnoreCase(AllMusic.config.getVersion())) {
                log.warning("§d[AllMusic]§c请及时更新配置文件");
            }
        } catch (Exception e) {
            log.warning("§d[AllMusic]§c读取配置文件错误");
            e.printStackTrace();
        }
    }

    public static void joinPlay(String name) {
        if (getConfig().getNoMusicPlayer().contains(name))
            return;
        if (PlayMusic.nowPlayMusic != null) {
            AllMusic.side.task(() -> {
                SaveOBJ obj = HudSave.get(name);
                String data = new Gson().toJson(obj);
                AllMusic.side.send(data, name, null);
                AllMusic.side.send("[Play]" + PlayGo.url, name, true);
                if (!PlayMusic.nowPlayMusic.isUrl()) {
                    AllMusic.side.task(() ->
                            AllMusic.side.send("[Img]" + PlayMusic.nowPlayMusic.getPicUrl(), name, true), 15);
                }
                AllMusic.side.task(() ->
                        AllMusic.side.send("[Pos]" + (PlayMusic.musicNowTime + 2000), name, true), 40);
            }, 10);
        }
    }

    public void init(File file) {
        log.info("§d[AllMusic]§2§e正在启动，感谢使用，本插件交流群：571239090");
        try {
            if (!file.exists())
                file.mkdir();
            if (configFile == null)
                configFile = new File(file, "config.json");
            if (messageFile == null)
                messageFile = new File(file, "message.json");
            if (cookieFile == null)
                cookieFile = new File(file, "cookie.json");
            if (logs.file == null)
                logs.file = new File(file, "logs.log");
            if (DataSql.sqlFile == null)
                DataSql.sqlFile = new File(file, "data.db");
            if (!configFile.exists()) {
                Files.copy(this.getClass().getResourceAsStream("/config.json"), configFile.toPath());
            }
            if (!messageFile.exists()) {
                Files.copy(this.getClass().getResourceAsStream("/message.json"), messageFile.toPath());
            }
            if (!cookieFile.exists()) {
                cookieFile.createNewFile();
            }
            if (!logs.file.exists()) {
                logs.file.createNewFile();
            }
            loadConfig();
            isRun = true;
        } catch (IOException e) {
            isRun = false;
            log.warning("§d[AllMusic]§c启动失败");
            e.printStackTrace();
        }
    }
}
