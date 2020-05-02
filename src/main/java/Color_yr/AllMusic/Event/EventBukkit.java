package Color_yr.AllMusic.Event;

import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.MusicPlay.SendInfo.SendInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.nio.charset.StandardCharsets;

public class EventBukkit implements Listener {
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        AllMusic.removeNowPlayPlayer(e.getPlayer().getName());
    }

    @EventHandler
    public void onPlayJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String Name = player.getName();
        SendInfo.SendSave(Name);
    }
}
