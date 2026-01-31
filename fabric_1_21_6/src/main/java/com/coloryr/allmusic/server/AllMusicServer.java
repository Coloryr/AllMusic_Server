package com.coloryr.allmusic.server;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class AllMusicServer implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("AllMusic Server");
    public static final String dir = "allmusic_server/";
    public static MinecraftServer server;

    @Override
    public void onInitializeServer() {
        com.coloryr.allmusic.server.core.AllMusic.log = new LogFabric();
        com.coloryr.allmusic.server.core.AllMusic.side = new SideFabric();

        new com.coloryr.allmusic.server.core.AllMusic().init(new File(dir));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment)
                -> CommandFabric.instance.register(dispatcher));

        ServerLifecycleEvents.SERVER_STARTED.register((a) -> {
            server = a;
            com.coloryr.allmusic.server.core.AllMusic.start();
            Tasks.init();
        });

        ServerLifecycleEvents.SERVER_STOPPING.register((a) -> {
            com.coloryr.allmusic.server.core.AllMusic.stop();
        });
    }
}
