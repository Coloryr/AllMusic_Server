package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.side.forge.CommandForge;
import com.coloryr.allmusic.server.side.forge.LogForge;
import com.coloryr.allmusic.server.side.forge.SideForge;
import com.mojang.brigadier.CommandDispatcher;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.CommandSource;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Locale;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AllMusicForge.MODID)
public class AllMusicForge {
    public static MinecraftServer server;
    public static final ResourceLocation channel = new ResourceLocation("allmusic", "channel");

    // Define mod id in a common place for everything to reference
    public static final String MODID = "allmusic_server";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogManager.getLogger("AllMusic_Server");
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace

    public AllMusicForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        String path = String.format(Locale.ROOT, "config/%s/", "AllMusic3");

        AllMusic.log = new LogForge();
        AllMusic.side = new SideForge();

        new AllMusic().init(new File(path));
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        CommandForge.instance.register(dispatcher);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartedEvent event) {
        server = event.getServer();

        AllMusic.start();
        Tasks.init();
    }

    @SubscribeEvent
    public void onServerStopping(FMLServerStoppingEvent event) {
        AllMusic.stop();
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        AllMusic.removeNowPlayPlayer(event.getEntity().getName().getString());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        AllMusic.joinPlay(event.getEntity().getName().getString());
    }
}
