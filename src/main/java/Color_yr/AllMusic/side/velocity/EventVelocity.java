package Color_yr.AllMusic.side.velocity;

import Color_yr.AllMusic.AllMusic;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;

public class EventVelocity {
    @Subscribe
    public void onPlayerQuit(final DisconnectEvent event) {
        AllMusic.removeNowPlayPlayer(event.getPlayer().getUsername());
    }

    @Subscribe
    public void onPostLoginEvent(final PostLoginEvent event) {
        AllMusic.removeNowPlayPlayer(event.getPlayer().getUsername());
    }
}
