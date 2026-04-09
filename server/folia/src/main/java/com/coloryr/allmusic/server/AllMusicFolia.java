package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.PlayMusic;
import com.coloryr.allmusic.server.core.music.TopLyricSave;
import com.coloryr.allmusic.server.core.objs.music.TopSongInfoObj;
import com.coloryr.allmusic.server.hooks.AllMusicPAPI;
import com.coloryr.allmusic.server.hooks.VaultHook;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class AllMusicFolia extends JavaPlugin {
    public static Plugin plugin;
    public static AllMusicPAPI PAPI;
    private static PluginMessage pluginMessage;

    @Override
    public void onEnable() {
        plugin = this;
        AllMusic.log = new LogFolia();
        AllMusic.side = new SideFolia();

        AllMusic.init(plugin.getDataFolder());
        if (!AllMusic.isRun) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (Bukkit.getPluginManager().getPlugin("Vault") != null
                && AllMusic.getConfig().economy.vault) {
            try {
                VaultHook vault = new VaultHook();
                AllMusic.economy = vault;
                if (vault.setupEconomy()) {
                    AllMusic.log.data("<light_purple>[AllMusic3]<yellow>Vault支持已启动");
                } else {
                    AllMusic.log.data("<light_purple>[AllMusic3]<yellow>Vault未挂钩");
                    AllMusic.economy = null;
                }
            } catch (Exception e) {
                AllMusic.log.data("<light_purple>[AllMusic3]<yellow>Vault未挂钩");
                AllMusic.economy = null;
            }
        } else {
            AllMusic.log.data("<light_purple>[AllMusic3]<yellow>Vault未挂钩");
            AllMusic.economy = null;
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            PAPI = new AllMusicPAPI(this);
            if (!PAPI.register()) {
                AllMusic.log.data("<light_purple>[AllMusic3]<yellow>PAPI支持已启动");
            }
        } else {
            AllMusic.log.data("<light_purple>[AllMusic3]<yellow>PAPI未挂钩");
        }

        if (AllMusic.getConfig().topAPI) {
            PlayMusic.nowPlayMusic = new TopSongInfoObj();
            PlayMusic.lyric = new TopLyricSave();
            pluginMessage = new PluginMessage();
            getServer().getMessenger().registerOutgoingPluginChannel(this, AllMusic.channelBC);
            getServer().getMessenger().registerIncomingPluginChannel(this, AllMusic.channelBC, pluginMessage);
            AllMusic.log.data("<light_purple>[AllMusic3]<yellow>设置为顶层模式");
        } else {
            CommandFolia command = new CommandFolia();
            getServer().getMessenger().registerOutgoingPluginChannel(this, AllMusic.channel);
            PluginCommand command1 = Bukkit.getPluginCommand("music");
            command1.setExecutor(command);
            command1.setTabCompleter(command);
            Bukkit.getPluginManager().registerEvents(new ListenerFolia(), this);
            AllMusic.start();
        }
    }

    @Override
    public void onDisable() {
        AllMusic.isRun = false;
        if (AllMusic.getConfig().topAPI)
            pluginMessage.stop();
        else
            AllMusic.stop();
    }
}
