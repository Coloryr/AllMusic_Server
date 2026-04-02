package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.AllMusic;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.chat.MutableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class AllMusicServer implements DedicatedServerModInitializer {
    public static MinecraftServer server;

    public static final ResourceLocation ID = new ResourceLocation("allmusic", "channel");

    public static final String dir = "allmusic_server/";

    private static final GsonComponentSerializer GSON_SERIALIZER = GsonComponentSerializer.builder().build();

    public static MutableComponent parse(Component input) {
        String json = GSON_SERIALIZER.serialize(input);
        return MutableComponent.Serializer.fromJson(json);
    }

    @Override
    public void onInitializeServer() {
        AllMusic.log = new LogFabric();
        AllMusic.side = new SideFabric();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CommandFabric.instance.register(dispatcher));

        ServerLifecycleEvents.SERVER_STARTED.register((a) -> {
            server = a;
            AllMusic.init(new File(dir));
            AllMusic.start();
            Tasks.init();
        });

        ServerLifecycleEvents.SERVER_STOPPING.register((a) -> {
            AllMusic.stop();
        });
    }
}
