package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.AllMusic;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = "allmusic_server", version = AllMusic.version, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.12,)")
public class AllMusicServer {
    public static final Logger LOGGER = LogManager.getLogger("AllMusic Server");
    public static MinecraftServer server;
    public static FMLEventChannel channel;

    public static final String dir = "config/allmusic_server/";

    @Mod.EventHandler
    private void commonSetup(final FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("allmusic:channel");

        AllMusic.log = new LogForge();
        AllMusic.side = new SideForge();

        new AllMusic().init(new File(dir));
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        AllMusic.start();
        Tasks.init();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        server = event.getServer();

        ServerCommandManager commandManager = (ServerCommandManager) server.commandManager;
        commandManager.registerCommand(new CommandForge());
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        AllMusic.stop();
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        AllMusic.removeNowPlayPlayer(event.player.getName());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        AllMusic.joinPlay(event.player.getName());
    }
}
