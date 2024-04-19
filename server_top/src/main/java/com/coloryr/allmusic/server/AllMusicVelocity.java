package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.side.velocity.*;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "allmusic", name = "AllMusic3", version = AllMusic.version,
        url = "https://github.com/Coloryr/AllMusic3_Server", description = "全服点歌插件", authors = {"Color_yr"})
public class AllMusicVelocity {
    public static AllMusicVelocity plugin;
    public static ChannelIdentifier channel;
    public static ChannelIdentifier channelBC;
    public final ProxyServer server;
    public final Path dataDirectory;
    private final Logger logger;
    private final MetricsVelocity.Factory metricsFactory;

    @Inject
    public AllMusicVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory, MetricsVelocity.Factory metricsFactory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.metricsFactory = metricsFactory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        plugin = this;
        AllMusic.log = new LogVelocity(logger);
        new AllMusic().init(dataDirectory.toFile());
        CommandMeta meta = server.getCommandManager().metaBuilder("music")
                .build();
        channel = () -> AllMusic.channel;
        channelBC = MinecraftChannelIdentifier.from(AllMusic.channelBC);
        AllMusic.side = new SideVelocity();
        server.getChannelRegistrar().register(channelBC);
        server.getCommandManager().register(meta, new CommandVelocity());
        server.getEventManager().register(this, new ListenerVelocity());
        metricsFactory.make(this, 6720);

        AllMusic.start();
    }

    @Subscribe
    public void onStop(ProxyShutdownEvent event) {
        AllMusic.stop();
    }
}
