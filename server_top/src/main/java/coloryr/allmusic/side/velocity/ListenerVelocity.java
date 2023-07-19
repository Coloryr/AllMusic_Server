package coloryr.allmusic.side.velocity;

import coloryr.allmusic.core.AllMusic;
import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.ServerConnection;

public class ListenerVelocity {
    @Subscribe
    public void onDisconnectEvent(final DisconnectEvent event) {
        AllMusic.removeNowPlayPlayer(event.getPlayer().getUsername());
    }

    @Subscribe
    public void onPostLoginEvent(final PostLoginEvent event) {
        AllMusic.removeNowPlayPlayer(event.getPlayer().getUsername());
    }

    @Subscribe
    public void onPluginMessageEvent(final PluginMessageEvent event) {
        if (event.getIdentifier().getId().equals(AllMusic.channelBC)) {
            event.setResult(PluginMessageEvent.ForwardResult.handled());
            ByteArrayDataInput data = event.dataAsDataStream();
            int type = data.readInt();
            if (type == 255 && event.getSource() instanceof ServerConnection) {
                ServerConnection server = (ServerConnection) event.getSource();
                SideVelocity.TopServers.add(server);
                SideVelocity.sendAllToServer(server);
                SideVelocity.sendLyricToServer(server);
            } else if (type == 12 || type == 13) {
                String uuid = data.readUTF();
                int res = data.readInt();
                SideVelocity.SendToBackend.put(uuid, res);
            }
        }
    }
}
