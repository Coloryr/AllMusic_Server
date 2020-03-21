package Color_yr.ALLMusic;

import Color_yr.ALLMusic.Command.CommandBukkit;
import Color_yr.ALLMusic.Event.EventBukkit;
import Color_yr.ALLMusic.MusicPlay.PlayMusic;
import Color_yr.ALLMusic.Side.SideBukkit.SideBukkit;
import Color_yr.ALLMusic.Side.SideBukkit.SideBukkit1_12;
import Color_yr.ALLMusic.Side.SideBukkit.SideBukkit1_14;
import Color_yr.ALLMusic.Side.SideBukkit.SideBukkit1_15;
import Color_yr.ALLMusic.Side.SideBukkit.VV.VVGet;
import Color_yr.ALLMusic.Utils.logs;
import Color_yr.ALLMusic.bStats.MetricsBukkit;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ALLMusicBukkit extends JavaPlugin {
    public static Plugin ALLMusicP;
    public static boolean VVEnable = false;

    public static void setConfig() {
        try {
            ALLMusic.ConfigFile = new File(ALLMusicP.getDataFolder(), "config.json");
            ALLMusic.MessageFile = new File(ALLMusicP.getDataFolder(), "Message.json");
            if (!ALLMusicP.getDataFolder().exists())
                ALLMusicP.getDataFolder().mkdir();
            new logs().Init(ALLMusicP.getDataFolder());
            if (!ALLMusic.ConfigFile.exists()) {
                InputStream in = ALLMusicP.getResource("config_BC.json");
                Files.copy(in, ALLMusic.ConfigFile.toPath());
            }
            if (!ALLMusic.MessageFile.exists()) {
                InputStream in = ALLMusicP.getResource("Message.json");
                Files.copy(in, ALLMusic.MessageFile.toPath());
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

        ALLMusic.log = getLogger();
        ALLMusic.log.info("§e正在启动，感谢使用，本插件交流群：571239090");
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
        if (ALLMusic.Config.isModCheck())
            getServer().getMessenger().registerIncomingPluginChannel(this, ALLMusic.channel,
                    (channel, player, message) -> {
                        if (channel.equalsIgnoreCase(ALLMusic.channel)) {
                            ByteBuf buf = Unpooled.wrappedBuffer(message);
                            String data = buf.toString(StandardCharsets.UTF_8);
                            if (data.contains("666")) {
                                ALLMusic.AddPlayer(player.getName());
                                player.sendMessage(ALLMusic.Message.getCheck().getMOD());
                            }
                        }
                    });
        Bukkit.getPluginCommand("music").setExecutor(new CommandBukkit());
        Bukkit.getPluginCommand("music").setTabCompleter(new CommandBukkit());
        Bukkit.getPluginManager().registerEvents(new EventBukkit(), this);
        if (ALLMusic.Config.isVexView() && Bukkit.getPluginManager().isPluginEnabled("VexView")) {
            ALLMusic.VV = new VVGet();
            VVEnable = true;
        }
        PlayMusic.start();
        new MetricsBukkit(this, 6720);
        ALLMusic.log.info("§e已启动-" + ALLMusic.Version);
    }

    @Override
    public void onDisable() {
        PlayMusic.stop();
        PlayMusic.clear();
        ALLMusic.VotePlayer.clear();
        if (ALLMusic.VV != null) {
            ALLMusic.VV.clear();
        }
        try {
            logs.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ALLMusic.log.info("§e已停止，感谢使用");
    }
}
