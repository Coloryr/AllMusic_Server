package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.side.forge.CommandForge;
import com.coloryr.allmusic.server.side.forge.LogForge;
import com.coloryr.allmusic.server.side.forge.SideForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod(modid = "allmusic_server", version = AllMusic.version, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.7,)")
public class AllMusicForge {
    public static final Logger LOGGER = LogManager.getLogger("AllMusic_Server");
    public static MinecraftServer server;
    public static FMLEventChannel channel;
    public static Queue<Runnable> queue = new ConcurrentLinkedQueue<>();

    @Mod.EventHandler
    private void commonSetup(final FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("allmusic:channel");

        String path = String.format(Locale.ROOT, "config/%s/", "AllMusic3");

        AllMusic.log = new LogForge();
        AllMusic.side = new SideForge();

        new AllMusic().init(new File(path));
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        AllMusic.start();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        server = event.getServer();

        ServerCommandManager commandManager = (ServerCommandManager) server.getCommandManager();
        commandManager.registerCommand(new CommandForge());
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) {
        AllMusic.stop();
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        AllMusic.removeNowPlayPlayer(event.player.getCommandSenderName());
    }

    @SubscribeEvent
    public void onServerTickEvent(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Tasks.tick();
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        AllMusic.joinPlay(event.player.getCommandSenderName());
    }
}
