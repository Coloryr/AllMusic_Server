package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.side.forge.CommandForge;
import com.coloryr.allmusic.server.side.forge.LogForge;
import com.coloryr.allmusic.server.side.forge.SideForge;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
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
import net.minecraftforge.network.SimpleChannel;
import org.slf4j.Logger;

import java.io.File;
import java.util.Locale;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AllMusicForge.MODID)
public class AllMusicForge {
    public static MinecraftServer server;
    public static final SimpleChannel channel = ChannelBuilder.named(new ResourceLocation("allmusic", "channel"))
            .networkProtocolVersion(0)
            .optional()
            .clientAcceptedVersions(((status, i) -> true))
            .serverAcceptedVersions(((status, i) -> true))
            .simpleChannel();

    // Define mod id in a common place for everything to reference
    public static final String MODID = "allmusic_server";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace

    public AllMusicForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        channel.messageBuilder(FriendlyByteBuf.class)
                .decoder(this::dec)
                .encoder(this::enc)
                .consumerNetworkThread(this::proc)
                .add();

        String path = String.format(Locale.ROOT, "config/%s/", "AllMusic");

        AllMusic.log = new LogForge();
        AllMusic.side = new SideForge();

        new AllMusic().init(new File(path));
    }

    private void enc(FriendlyByteBuf str, FriendlyByteBuf buffer) {
        buffer.writeBytes(str);
    }

    private FriendlyByteBuf dec(FriendlyByteBuf buffer) {
        return buffer;
    }

    private void proc(FriendlyByteBuf buffer, CustomPayloadEvent.Context ctx) {
        //ctx.enqueueWork(() -> handle(buffer));
        ctx.setPacketHandled(true);
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
