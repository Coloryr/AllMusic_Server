package Color_yr.ALLMusic.Event;

import Color_yr.ALLMusic.ALLMusic;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventBukkit implements Listener {
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        ALLMusic.removeNowPlayPlayer(e.getPlayer().getName());
    }
}
