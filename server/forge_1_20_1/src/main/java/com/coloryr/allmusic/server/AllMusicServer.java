package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.PlayMusic;
import com.mojang.brigadier.CommandDispatcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@Mod(AllMusicServer.MODID)
public class AllMusicServer {
    public static final ResourceLocation channel = new ResourceLocation("allmusic", "channel");
    public static final String MODID = "allmusic_server";
    public static final Logger LOGGER = LoggerFactory.getLogger("AllMusic Server");
    private static final GsonComponentSerializer GSON_SERIALIZER = GsonComponentSerializer.builder()
            .build();
    public static MinecraftServer server;

    public AllMusicServer() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static MutableComponent parse(Component input) {
        String json = GSON_SERIALIZER.serialize(input);
        return MutableComponent.Serializer.fromJson(json);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        AllMusic.log = new LogForge();
        AllMusic.side = new SideForge();
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        CommandForge.instance.register(dispatcher);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        server = event.getServer();
        AllMusic.init(new File(AllMusic.SERVER_DIR));
        AllMusic.start();
        Tasks.init();
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        AllMusic.stop();
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayMusic.removeNowPlayPlayer(event.getEntity().getName().getString());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        AllMusic.joinPlay(event.getEntity().getName().getString());
    }
}
