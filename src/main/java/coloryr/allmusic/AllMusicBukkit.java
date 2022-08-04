package coloryr.allmusic;

import coloryr.allmusic.music.api.SongInfo;
import coloryr.allmusic.music.api.TopSongInfo;
import coloryr.allmusic.music.lyric.TopLyricItem;
import coloryr.allmusic.music.play.PlayMusic;
import coloryr.allmusic.side.bukkit.*;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class AllMusicBukkit extends JavaPlugin {
    public static Plugin plugin;
    public static AllMusicPAPI PAPI;
    public static boolean spigotSet;
    private static PluginMessage pluginMessage;

    @Override
    public void onEnable() {
        plugin = this;
        AllMusic.log = new BukkitLog(getLogger());
        AllMusic.side = new SideBukkit();

        try {
            Class.forName("net.md_5.bungee.api.chat.BaseComponent");
            Class test = Class.forName("org.bukkit.command.CommandSender");
            test.getMethod("spigot");
            AllMusic.log.info("§2Spigot已支持");
            spigotSet = true;
        } catch (Exception e) {
            AllMusic.log.info("§2Spigot不支持");
        }

        new AllMusic().init(plugin.getDataFolder());
        if (!AllMusic.isRun) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            PAPI = new AllMusicPAPI(this);
            if (!PAPI.register()) {
                AllMusic.log.info("§2PAPI支持已启动");
            }
        } else {
            AllMusic.log.info("§2PAPI未挂钩");
        }

        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            try {
                AllMusic.vault = new VaultHook();
                if (AllMusic.vault.setupEconomy()) {
                    AllMusic.log.info("§2Vault支持已启动");
                } else {
                    AllMusic.log.info("§2Vault未挂钩");
                    AllMusic.vault = null;
                }
            } catch (Exception e) {
                AllMusic.log.info("§2Vault未挂钩");
                AllMusic.vault = null;
            }
        } else {
            AllMusic.log.info("§2Vault未挂钩");
            AllMusic.vault = null;
        }

        if (AllMusic.getConfig().isTopPAPI()) {
            PlayMusic.nowPlayMusic = new TopSongInfo();
            PlayMusic.lyricItem = new TopLyricItem();
            pluginMessage = new PluginMessage();
            getServer().getMessenger().registerOutgoingPluginChannel(this, AllMusic.channelBC);
            getServer().getMessenger().registerIncomingPluginChannel(this, AllMusic.channelBC, pluginMessage);
            AllMusic.log.info("§2设置为顶层模式");
        } else {
            CommandBukkit command = new CommandBukkit();
            getServer().getMessenger().registerOutgoingPluginChannel(this, AllMusic.channel);
            PluginCommand command1 = Bukkit.getPluginCommand("music");
            command1.setExecutor(command);
            command1.setTabCompleter(command);
            Bukkit.getPluginManager().registerEvents(new EventBukkit(), this);
            AllMusic.start();
        }

        new MetricsBukkit(this, 6720);
    }

    @Override
    public void onDisable() {
        AllMusic.isRun = false;
        if (AllMusic.getConfig().isTopPAPI())
            pluginMessage.stop();
        else
            AllMusic.stop();
    }
}
