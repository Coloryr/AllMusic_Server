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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static Color_yr.ALLmusic.PlayMusic.PlayMusic_Start;

public class ALLmusic_BC extends Plugin {

    private static final String Version = "1.1.0";

    public static int Maxlist = 0;
    public static int min_vote = 0;
    public static String Music_Api1 = null;

    public static Configuration config;
    public static File FileName;

    public static Configuration Banconfig;
    public static File BanFileName;

    public static List<String> Admin = new ArrayList<String>();
    public static List<String> server = new ArrayList<String>();

    public static Logger log = ProxyServer.getInstance().getLogger();

    private void loadconfig() {
        ProxyServer.getInstance().getLogger().info("§d[ALLmusic]§e当前插件版本为：" + Version
                + "，你的配置文件版本为：" + config.getString("Version"));

        Maxlist = config.getInt("Maxlist", 10);
        min_vote = config.getInt("min_vote", 3);
        Music_Api1 = config.getString("Music_Api1", "http://music.163.com/song/media/outer/url?id=");

        Admin = config.getStringList("Admin");
        server = config.getStringList("nomusic_server");

        Configuration a = config.getSection("nomusic");
        Collection<String> b = a.getKeys();
        for (String c : b) {
            boolean d = a.getBoolean(c, false);
            PlayMusic.stop.put(c, String.valueOf(d));
            log.info("玩家：" + c + "的点歌状态：" + !d);
        }
    }

    void setConfig() {
        try {
            if(FileName == null) {
                FileName = new File(getDataFolder(), "config.yml");
                BanFileName = new File(getDataFolder(), "banlist.yml");
                logs.file = new File(getDataFolder(), "logs.log");
                if (!getDataFolder().exists())
                    getDataFolder().mkdir();
                if (!FileName.exists()) {
                    InputStream in = getResourceAsStream("config.yml");
                    Files.copy(in, FileName.toPath());
                    in = getResourceAsStream("banlist.yml");
                    Files.copy(in, BanFileName.toPath());
                }
                if (!logs.file.exists()) {
                    logs.file.createNewFile();
                }
            }

            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(FileName);
            Banconfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(BanFileName);
            loadconfig();
        } catch (IOException e) {
            log.warning("§d[ALLmusic]§c日志文件错误：" + e);
        }
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
