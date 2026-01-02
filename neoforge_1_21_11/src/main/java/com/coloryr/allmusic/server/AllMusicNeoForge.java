package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.AllMusic;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AllMusicNeoForge.MODID)
public class AllMusicNeoForge {
    public static final Identifier channel = Identifier.fromNamespaceAndPath("allmusic", "channel");
    // Define mod id in a common place for everything to reference
    public static final String MODID = "allmusic_server";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LoggerFactory.getLogger("AllMusic Server");
    public static MinecraftServer server;
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace

    public static final String dir = "config/allmusic_server/";

    public AllMusicNeoForge(IEventBus modEventBus) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::register);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        AllMusic.log = new LogNeoForge();
        AllMusic.side = new SideNeoForge();

        new AllMusic().init(new File(dir));
    }

    public void register(final RegisterPayloadHandlersEvent event) {
        LOGGER.info("注册插件信道");
        final PayloadRegistrar registrar = event.registrar("1.0");
        registrar.optional().playToClient(PackData.TYPE, PackData.CODEC, new HandelPack());
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        LOGGER.info("注册指令");
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        CommandNeoForge.instance.register(dispatcher);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartedEvent event) {
        server = event.getServer();

        AllMusic.start();
        Tasks.init();
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
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

    private static class HandelPack implements IPayloadHandler<PackData> {
        @Override
        public void handle(@NotNull PackData payload, IPayloadContext context) {
            //context.handle(payload);
        }
    }
}
