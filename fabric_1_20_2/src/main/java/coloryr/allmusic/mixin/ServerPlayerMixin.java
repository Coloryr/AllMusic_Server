package coloryr.allmusic.mixin;

import coloryr.allmusic.AllMusicFabric;
import coloryr.allmusic.core.AllMusic;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class ServerPlayerMixin {
	@Inject(at = @At("HEAD"), method = "remove")
	private void remove(ServerPlayerEntity entity, CallbackInfo info) {
		AllMusic.removeNowPlayPlayer(entity.getName().getString());
	}

	@Inject(at = @At("TAIL"), method = "onPlayerConnect")
	private void add(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci) {
		AllMusic.joinPlay(player.getName().getString());
	}
}
