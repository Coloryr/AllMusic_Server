package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.AllMusic;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class AllMusicFabric implements ModInitializer {
    public static MinecraftServer server;
    public static final Logger LOGGER = LoggerFactory.getLogger("AllMusic Server");

    public static final Identifier ID = new Identifier("allmusic", "channel");

    public static final String dir = "allmusic_server/";

    @Override
    public void onInitialize() {
        AllMusic.log = new LogFabric();
        AllMusic.side = new SideFabric();

        new AllMusic().init(new File(dir));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CommandFabric.instance.register(dispatcher));

        Buffer buffer = new Buffer();
        buffer.close();

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
