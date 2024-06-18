package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.side.forge.CommandForge;
import com.coloryr.allmusic.server.side.forge.LogForge;
import com.coloryr.allmusic.server.side.forge.MusicPacketPayload;
import com.coloryr.allmusic.server.side.forge.SideForge;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.SimpleChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Locale;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AllMusicForge.MODID)
public class AllMusicForge {
    public static final ResourceLocation channel = ResourceLocation.fromNamespaceAndPath("allmusic", "channel");
    // Define mod id in a common place for everything to reference
    public static final String MODID = "allmusic_server";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LoggerFactory.getLogger("AllMusic_Server");
    public static MinecraftServer server;
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace

    public static SimpleChannel channel1;

    public AllMusicForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        String path = String.format(Locale.ROOT, "config/%s/", "AllMusic3");

        channel1 = ChannelBuilder
                .named(channel)
                .optional()
                .networkProtocolVersion(0)
                .simpleChannel()
                .play()
                .serverbound()
                .add(MusicPacketPayload.class, MusicPacketPayload.STREAM_CODEC, this::handel)
                .build();

        AllMusic.log = new LogForge();
        AllMusic.side = new SideForge();

        new AllMusic().init(new File(path));
    }

    private void handel(MusicPacketPayload payload, CustomPayloadEvent.Context context) {

    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        CommandForge.instance.register(dispatcher);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
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
}
