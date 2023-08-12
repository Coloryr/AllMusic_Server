package coloryr.allmusic;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.side.forge.*;
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
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Locale;

@Mod(modid = "allmusic", version = AllMusic.version, acceptableRemoteVersions = "*" ,acceptedMinecraftVersions = "[1.12,)", serverSideOnly = true)
public class AllMusicForge {
    public static MinecraftServer server;

    public static SimpleNetworkWrapper channel;

    public static final Logger LOGGER = LogManager.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace

    @Mod.EventHandler
    private void commonSetup(final FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        channel = NetworkRegistry.INSTANCE.newSimpleChannel("allmusic:channel");
        channel.registerMessage(PacketMessageHandler.class, PacketMessage.class, 0, Side.CLIENT);

        String path = String.format(Locale.ROOT, "config/%s/", "AllMusic");

        AllMusic.log = new LogForge();
        AllMusic.side = new SideForge();

        new AllMusic().init(new File(path));
    }

    @Mod.EventHandler
    public void onRegisterCommands(FMLServerStartedEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        server = event.getServer();

        ServerCommandManager commandManager = (ServerCommandManager)server.commandManager;
        commandManager.registerCommand(new CommandForge());

        AllMusic.start();
        Tasks.init();
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
