package Color_yr.AllMusic.Event;

import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.MusicPlay.SendInfo.SendInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventBC implements Listener {
    @EventHandler
    public void onPlayerquit(final PlayerDisconnectEvent event) {
        AllMusic.removeNowPlayPlayer(event.getPlayer().getName());
    }
}
