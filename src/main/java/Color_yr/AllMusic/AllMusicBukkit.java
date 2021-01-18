package Color_yr.AllMusic;

import Color_yr.AllMusic.Command.CommandBukkit;
import Color_yr.AllMusic.Event.EventBukkit;
import Color_yr.AllMusic.Side.SideBukkit.PAPI;
import Color_yr.AllMusic.Side.SideBukkit.SideBukkit;
import Color_yr.AllMusic.Side.SideBukkit.VaultHook;
import Color_yr.AllMusic.bStats.MetricsBukkit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class AllMusicBukkit extends JavaPlugin {
    public static Plugin plugin;
    public static PAPI PAPI;
    public static boolean SpigotSet;

    @Override
    public void onEnable() {
        plugin = this;
        AllMusic.log = getLogger();
        AllMusic.Side = new SideBukkit();

        try {
            SpigotSet = null != Class.forName("net.md_5.bungee.api.chat.BaseComponent");
            AllMusic.log.info("§2Spigot已支持");
        } catch (Exception e) {
            AllMusic.log.info("§2Spigot不支持");
        }

        new AllMusic().init(plugin.getDataFolder());
        if (!AllMusic.isRun) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            PAPI = new PAPI(this);
            if (!PAPI.register()) {
                AllMusic.log.info("§2PAPI支持已启动");
            }
        } else {
            AllMusic.log.info("§2PAPI未挂钩");
        }

        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            try {
                AllMusic.Vault = new VaultHook();
                if (AllMusic.Vault.setupEconomy()) {
                    AllMusic.log.info("§2Vault支持已启动");
                } else {
                    AllMusic.log.info("§2Vault未挂钩");
                    AllMusic.Vault = null;
                }
            } catch (Exception e) {
                AllMusic.log.info("§2Vault未挂钩");
                AllMusic.Vault = null;
            }
        } else {
            AllMusic.log.info("§2Vault未挂钩");
            AllMusic.Vault = null;
        }
        getServer().getMessenger().registerOutgoingPluginChannel(this, AllMusic.channel);
        Bukkit.getPluginCommand("music").setExecutor(new CommandBukkit());
        Bukkit.getPluginCommand("music").setTabCompleter(new CommandBukkit());
        Bukkit.getPluginManager().registerEvents(new EventBukkit(), this);
        new MetricsBukkit(this, 6720);

        AllMusic.start();
    }

    @Override
    public void onDisable() {
        AllMusic.isRun = false;
        AllMusic.stop();
    }
}
