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
import java.util.logging.Logger;

import static Color_yr.ALLmusic.PlayMusic.PlayMusic_Start;

public class ALLmusic_BC extends Plugin {

    public static String Version = "1.0.0";

    public static Configuration config;
    private static File FileName;

    public static Logger log = ProxyServer.getInstance().getLogger();

    public static void loadconfig() {
        ProxyServer.getInstance().getLogger().info("§d[ALLmusic_BC]§e当前插件版本为：" + Version
                + "，你的配置文件版本为：" + config.getString("Version"));
    }

    public static void reloadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(FileName);
            loadconfig();
        } catch (Exception arg0) {
            log.warning("§d[ALLmusic_BC]§c配置文件读取失败:" + arg0);
        }
    }

    public static Configuration getConfig() {
        return config;
    }

    public void setConfig() {
        FileName = new File(getDataFolder(), "config.yml");
        logs.file = new File(getDataFolder(), "logs.log");
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        if (!FileName.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, FileName.toPath());
            } catch (IOException e) {
                log.warning("§d[ALLmusic_BC]§c配置文件创建失败：" + e);
            }
        }
        try {
            if (!logs.file.exists()) {
                logs.file.createNewFile();
            }
        } catch (IOException e) {
            log.warning("§d[ALLmusic_BC]§c日志文件错误：" + e);
        }
    }

    @Override
    public void onEnable() {
        log.info("§d[ALLmusic_BC]§e正在启动，感谢使用，本插件交流群：571239090");
        setConfig();
        reloadConfig();
        PlayMusic_Start();
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new command());
        log.info("§d[ALLmusic_BC]§e已启动-" + Version);;
    }

    @Override
    public void onDisable() {
        log.info("§d[ALLmusic_BC]§e已停止，感谢使用");
    }
}
