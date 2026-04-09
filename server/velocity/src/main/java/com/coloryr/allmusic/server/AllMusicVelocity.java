package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.side.velocity.*;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;

import java.nio.file.Path;

public class AllMusicVelocity {
    public static AllMusicVelocity plugin;
    public static ChannelIdentifier channel;
    public static ChannelIdentifier channelBC;
    public final ProxyServer server;
    public final Path dataDirectory;
    private final Logger logger;

    @Inject
    public AllMusicVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        plugin = this;
        AllMusic.log = new LogVelocity();
        SideVelocity side = new SideVelocity();
        AllMusic.side = side;
        AllMusic.economy = side;

        AllMusic.init(dataDirectory.toFile());

        CommandMeta meta = server.getCommandManager().metaBuilder("music")
                .build();
        channel = () -> AllMusic.channel;
        channelBC = MinecraftChannelIdentifier.from(AllMusic.channelBC);

        server.getChannelRegistrar().register(channelBC);
        server.getCommandManager().register(meta, new CommandVelocity());
        server.getEventManager().register(this, new ListenerVelocity());

        AllMusic.start();
    }

    @Subscribe
    public void onStop(ProxyShutdownEvent event) {
        AllMusic.stop();
    }
}
