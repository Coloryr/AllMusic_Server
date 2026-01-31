package com.coloryr.allmusic.server;

import com.coloryr.allmusic.comm.AllMusicInit;
import com.coloryr.allmusic.server.core.AllMusic;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@EventBusSubscriber(modid = AllMusicInit.MODID, value = Dist.DEDICATED_SERVER)
public class AllMusicServer {

    public static final Logger LOGGER = LoggerFactory.getLogger("AllMusic Server");
    public static MinecraftServer server;

    public static final String dir = "config/allmusic_server/";

    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
        AllMusic.log = new LogNeoForge();
        AllMusic.side = new SideNeoForge();

        new AllMusic().init(new File(dir));
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        LOGGER.info("注册指令");
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        CommandNeoForge.instance.register(dispatcher);
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartedEvent event) {
        server = event.getServer();

        AllMusic.start();
        Tasks.init();
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        AllMusic.stop();
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        AllMusic.removeNowPlayPlayer(event.getEntity().getName().getString());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        AllMusic.joinPlay(event.getEntity().getName().getString());
    }
}
