package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.side.fabric.CommandFabric;
import com.coloryr.allmusic.server.side.fabric.LogFabric;
import com.coloryr.allmusic.server.side.fabric.SideFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class AllMusicFabric implements ModInitializer {
    public static MinecraftServer server;
    public static final Logger LOGGER = LoggerFactory.getLogger("AllMusic_Server");

    public static final Identifier ID = Identifier.of("allmusic", "channel");

    @Override
    public void onInitialize() {
        String path = "allmusic3/";

        AllMusic.log = new LogFabric();
        AllMusic.side = new SideFabric();

        new AllMusic().init(new File(path));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment)
                -> CommandFabric.instance.register(dispatcher));

        PayloadTypeRegistry.playS2C().register(PackPayload.ID, PackPayload.CODEC);
        ServerLifecycleEvents.SERVER_STARTED.register((a) -> {
            server = a;

            AllMusic.start();

            Tasks.init();
        });

        ServerLifecycleEvents.SERVER_STOPPING.register((a) -> {
            AllMusic.stop();
        });
    }
}
