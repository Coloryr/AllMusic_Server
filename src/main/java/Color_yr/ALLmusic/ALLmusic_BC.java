package Color_yr.ALLmusic;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import static Color_yr.ALLmusic.PlayMusic.PlayMusic_Start;

public class ALLmusic_BC extends Plugin {

    public static String Version = "1.0.0";

    public static int Maxlist = 0;
    public static int min_vote = 0;
    public static String Music_Api1 = null;

    public static Configuration config;
    public static File FileName;

    public static Configuration Banconfig;
    public static File BanFileName;

    public static Logger log = ProxyServer.getInstance().getLogger();

    public void loadconfig() {
        ProxyServer.getInstance().getLogger().info("§d[ALLmusic]§e当前插件版本为：" + Version
                + "，你的配置文件版本为：" + config.getString("Version"));

        Maxlist = config.getInt("Maxlist", 10);
        min_vote = config.getInt("min_vote", 3);
        Music_Api1 = config.getString("Music_Api1", "http://music.163.com/song/media/outer/url?id=");

        Configuration a = config.getSection("nomusic");
        Collection<String> b = a.getKeys();
        for (String c : b) {
            boolean d = a.getBoolean(c, false);
            PlayMusic.stop.put(c, String.valueOf(d));
            log.info("玩家：" + c + "的点歌状态：" + !d);
        }
    }

    private void reloadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(FileName);
            Banconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(BanFileName);
            loadconfig();
        } catch (Exception arg0) {
            log.warning("§d[ALLmusic_BC]§c配置文件读取失败:" + arg0);
        }
    }

    public void setConfig() {
        FileName = new File(getDataFolder(), "config.yml");
        BanFileName = new File(getDataFolder(), "banlist.yml");
        logs.file = new File(getDataFolder(), "logs.log");
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        if (!FileName.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, FileName.toPath());
            } catch (IOException e) {
                log.warning("§d[ALLmusic]§c配置文件创建失败：" + e);
            }
            try (InputStream in = getResourceAsStream("banlist.yml")) {
                Files.copy(in, BanFileName.toPath());
            } catch (IOException e) {
                log.warning("§d[ALLmusic]§c禁用列表创建失败：" + e);
            }
        }
        try {
            if (!logs.file.exists()) {
                logs.file.createNewFile();
            }
        } catch (IOException e) {
            log.warning("§d[ALLmusic]§c日志文件错误：" + e);
        }
        reloadConfig();
    }

    @Override
    public void onEnable() {
        log.info("§d[ALLmusic]§e正在启动，感谢使用，本插件交流群：571239090");
        setConfig();
        PlayMusic_Start();
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new command());
        log.info("§d[ALLmusic]§e已启动-" + Version);
    }

    @Override
    public void onDisable() {
        PlayMusic.playlist.clear();
        PlayMusic.Vote.clear();
        PlayMusic.SendToPlayer("[Stop]");
        log.info("§d[ALLmusic]§e已停止，感谢使用");
    }
}
