package Color_yr.ALLMusic.Event;

import Color_yr.ALLMusic.Play.PlayMusic;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventBC implements Listener {
    @EventHandler
    public void onPlayerquit(PlayerDisconnectEvent event) {
        PlayMusic.NowPlay.remove(event.getPlayer().getName());
    }
}
