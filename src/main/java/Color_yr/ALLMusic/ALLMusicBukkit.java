package Color_yr.ALLMusic;

import Color_yr.ALLMusic.Command.CommandBukkit;
import Color_yr.ALLMusic.Event.EventBukkit;
import Color_yr.ALLMusic.MusicPlay.PlayMusic;
import Color_yr.ALLMusic.Side.SideBukkit.SideBukkit;
import Color_yr.ALLMusic.Side.SideBukkit.SideBukkit1_12;
import Color_yr.ALLMusic.Side.SideBukkit.SideBukkit1_14;
import Color_yr.ALLMusic.Side.SideBukkit.SideBukkit1_15;
import Color_yr.ALLMusic.Utils.logs;
import Color_yr.ALLMusic.Side.SideBukkit.VV.VVGet;
import Color_yr.ALLMusic.bStats.MetricsBukkit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;

public class ALLMusicBukkit extends JavaPlugin {
    public static Plugin ALLMusicP;
    public static boolean VVEnable = false;

    public static void setConfig() {
        try {
            ALLMusic.ConfigFile = new File(ALLMusicP.getDataFolder(), "config.json");
            if (!ALLMusicP.getDataFolder().exists())
                ALLMusicP.getDataFolder().mkdir();
            new logs().Init(ALLMusicP.getDataFolder());
            if (!ALLMusic.ConfigFile.exists()) {
                InputStream in = ALLMusicP.getResource("config_BC.json");
                Files.copy(in, ALLMusic.ConfigFile.toPath());
            }
            ALLMusic.LoadConfig();
        } catch (IOException e) {
            ALLMusic.log.warning("§d[ALLMusic]§c配置文件错误");
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        ALLMusicP = this;

        ALLMusic.log = getLogger();
        ALLMusic.log.info("§d[ALLMusic]§e正在启动，感谢使用，本插件交流群：571239090");
        setConfig();
        String Version = Bukkit.getBukkitVersion();
        if (Version.startsWith("1.12"))
            ALLMusic.Side = new SideBukkit1_12();
        else if (Version.startsWith("1.14"))
            ALLMusic.Side = new SideBukkit1_14();
        else if (Version.startsWith("1.15"))
            ALLMusic.Side = new SideBukkit1_15();
        else
            ALLMusic.Side = new SideBukkit();
        getServer().getMessenger().registerOutgoingPluginChannel(this, ALLMusic.channel);
        Bukkit.getPluginCommand("music").setExecutor(new CommandBukkit());
        Bukkit.getPluginCommand("music").setTabCompleter(new CommandBukkit());
        Bukkit.getPluginManager().registerEvents(new EventBukkit(), this);
        if (ALLMusic.Config.isVexView() && Bukkit.getPluginManager().isPluginEnabled("VexView")) {
            ALLMusic.VV = new VVGet();
            VVEnable = true;
        }
        PlayMusic.start();
        new MetricsBukkit(this, 6720);
        ALLMusic.log.info("§d[ALLMusic]§e已启动-" + ALLMusic.Version);
    }

    @Override
    public void onDisable() {
        PlayMusic.stop();
        PlayMusic.clear();
        PlayMusic.VotePlayer.clear();
        if (ALLMusic.VV != null) {
            ALLMusic.VV.clear();
        }
        ALLMusic.Side.Send("[Stop]", false);
        ALLMusic.log.info("§d[ALLMusic]§e已停止，感谢使用");
    }

}
