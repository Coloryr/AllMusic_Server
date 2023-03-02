package coloryr.allmusic;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.side.fabric.CommandFabric;
import coloryr.allmusic.side.fabric.LogFabric;
import coloryr.allmusic.side.fabric.SideFabric;
import coloryr.allmusic.side.fabric.event.MusicAddEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Locale;

public class AllMusicFabric implements ModInitializer {
	public static MinecraftServer server;
	public static final Logger LOGGER = LoggerFactory.getLogger("allmusic");

	public static final Identifier ID = new Identifier("allmusic", "channel");

	@Override
	public void onInitialize() {
		String path = "allmusic/";

		AllMusic.log = new LogFabric();
		AllMusic.side = new SideFabric();

		new AllMusic().init(new File(path));

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CommandFabric.instance.register(dispatcher));

		Buffer buffer = new Buffer();
		buffer.close();

		ServerLifecycleEvents.SERVER_STARTED.register((a)->{
			server = a;

			AllMusic.start();

			Tasks.init();
		});

		ServerLifecycleEvents.SERVER_STOPPING.register((a)->{
			AllMusic.stop();
		});
	}
}
