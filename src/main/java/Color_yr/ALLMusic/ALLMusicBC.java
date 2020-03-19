package Color_yr.ALLMusic;

import Color_yr.ALLMusic.Command.CommandBC;
import Color_yr.ALLMusic.Event.EventBC;
import Color_yr.ALLMusic.MusicPlay.PlayMusic;
import Color_yr.ALLMusic.Side.SideBC.SideBC;
import Color_yr.ALLMusic.Utils.logs;
import Color_yr.ALLMusic.bStats.MetricsBC;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;
import java.nio.file.Files;

public class ALLMusicBC extends Plugin {
    public static Plugin ALLMusicP;

    public static void setConfig() {
        try {
            ALLMusic.ConfigFile = new File(ALLMusicP.getDataFolder(), "config.json");
            if (!ALLMusicP.getDataFolder().exists())
                ALLMusicP.getDataFolder().mkdir();
            new logs().Init(ALLMusicP.getDataFolder());
            if (!ALLMusic.ConfigFile.exists()) {
                InputStream in = ALLMusicP.getResourceAsStream("config_BC.json");
                Files.copy(in, ALLMusic.ConfigFile.toPath());
            }
            if (!logs.file.exists()) {
                logs.file.createNewFile();
            }
            ALLMusic.LoadConfig();
        } catch (IOException e) {
            ALLMusic.log.warning("§c配置文件错误");
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        ALLMusicP = this;
        ALLMusic.log = ProxyServer.getInstance().getLogger();
        ALLMusic.log.info("§e正在启动，感谢使用，本插件交流群：571239090");
        setConfig();
        ALLMusic.Side = new SideBC();
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CommandBC());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new EventBC());
        PlayMusic.start();
        new MetricsBC(this, 6720);
        ALLMusic.log.info("§e已启动-" + ALLMusic.Version);
    }

    @Override
    public void onDisable() {
        PlayMusic.stop();
        PlayMusic.clear();
        PlayMusic.VotePlayer.clear();
        ALLMusic.Side.Send("[Stop]", false);
        try {
            logs.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ALLMusic.log.info("§e已停止，感谢使用");
    }
}