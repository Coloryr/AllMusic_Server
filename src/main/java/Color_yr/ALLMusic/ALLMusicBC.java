package Color_yr.ALLMusic;

import Color_yr.ALLMusic.Command.CommandBC;
import Color_yr.ALLMusic.Event.EventBC;
import Color_yr.ALLMusic.Play.PlayMusic;
import Color_yr.ALLMusic.Side.SideBC;
import Color_yr.ALLMusic.Utils.logs;
import com.google.gson.Gson;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ALLMusicBC extends Plugin {
    public static Plugin ALLMusicP;
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

    private static void LoadConfig() throws FileNotFoundException {

        InputStreamReader reader = new InputStreamReader(new FileInputStream(ALLMusic.ConfigFile), StandardCharsets.UTF_8);
        BufferedReader bf = new BufferedReader(reader);
        ALLMusic.Config = new Gson().fromJson(bf, ConfigOBJ.class);

        if (ALLMusic.Config == null) {
            ALLMusic.Config = new ConfigOBJ();
        }

        ALLMusic.log.info("§d[ALLMusic]§e当前插件版本为：" + ALLMusic.Version
                + "，你的配置文件版本为：" + ALLMusic.Config.getVersion());

        for (String item : ALLMusic.Config.getNoMusicPlayer()) {
            ALLMusic.log.info("玩家：" + item + "不参与点歌");
        }

        for (String item : ALLMusic.Config.getNoMusicServer()) {
            ALLMusic.log.info("服务器：" + item + "不参与点歌");
        }
    }

    public static void setConfig() {
        try {
            if (ALLMusic.ConfigFile == null) {
                ALLMusic.ConfigFile = new File(ALLMusicP.getDataFolder(), "config.json");
                logs.file = new File(ALLMusicP.getDataFolder(), "logs.log");
                if (!ALLMusicP.getDataFolder().exists())
                    ALLMusicP.getDataFolder().mkdir();
                if (!ALLMusic.ConfigFile.exists()) {
                    InputStream in = ALLMusicP.getResourceAsStream("config_BC.json");
                    Files.copy(in, ALLMusic.ConfigFile.toPath());
                }
                if (!logs.file.exists()) {
                    logs.file.createNewFile();
                }
            }

            LoadConfig();
        } catch (IOException e) {
            ALLMusic.log.warning("§d[ALLMusic]§c配置文件错误");
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        ALLMusicP = this;
        ALLMusic.log = ProxyServer.getInstance().getLogger();
        ALLMusic.log.info("§d[ALLMusic]§e正在启动，感谢使用，本插件交流群：571239090");
        setConfig();
        PlayMusic.Start();
        ALLMusic.Side = new SideBC();
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CommandBC());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new EventBC());
        ALLMusic.log.info("§d[ALLMusic]§e已启动-" + ALLMusic.Version);
    }

    @Override
    public void onDisable() {
        PlayMusic.stop();
        PlayMusic.PlayList.clear();
        PlayMusic.Vote.clear();
        ALLMusic.Side.Send("[Stop]", false);
        ALLMusic.log.info("§d[ALLMusic]§e已停止，感谢使用");
    }
}