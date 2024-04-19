package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.side.bc.*;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class AllMusicBC extends Plugin {

    public static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        AllMusic.log = new LogBC(ProxyServer.getInstance().getLogger());
        AllMusic.side = new SideBC();

        new AllMusic().init(plugin.getDataFolder());

        if (!AllMusic.isRun)
            return;
        ProxyServer.getInstance().registerChannel(AllMusic.channelBC);
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CommandBC());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ListenerBC());
        new MetricsBC(this, 6720);

        AllMusic.start();
    }

    @Override
    public void onDisable() {
        AllMusic.stop();
    }
}