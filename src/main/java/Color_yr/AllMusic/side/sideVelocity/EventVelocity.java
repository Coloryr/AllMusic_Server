package Color_yr.AllMusic.side.sideVelocity;

import Color_yr.AllMusic.AllMusic;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;

public class EventVelocity {
    @Subscribe
    public void onPlayerQuit(final DisconnectEvent event) {
        AllMusic.removeNowPlayPlayer(event.getPlayer().getUsername());
    }
}
