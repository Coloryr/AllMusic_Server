package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.AllMusic;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class AllMusicServer implements DedicatedServerModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("AllMusic Server");
    public static final ResourceLocation ID = new ResourceLocation("allmusic", "channel");
    public static MinecraftServer server;
    public static FabricServerAudiences audiences;

    public static final String dir = "allmusic_server/";

    @Override
    public void onInitializeServer() {

        AllMusic.log = new LogFabric();
        AllMusic.side = new SideFabric();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                CommandFabric.instance.register(dispatcher));

        ServerLifecycleEvents.SERVER_STARTED.register((a) -> {
            server = a;
            audiences = FabricServerAudiences.of(server);
            AllMusic.init(new File(dir));
            AllMusic.start();
            Tasks.init();
        });

        ServerLifecycleEvents.SERVER_STOPPING.register((a) -> {
            AllMusic.stop();
        });
    }
}
