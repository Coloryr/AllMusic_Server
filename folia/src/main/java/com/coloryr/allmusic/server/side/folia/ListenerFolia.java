package com.coloryr.allmusic.server.side.folia;

import com.coloryr.allmusic.server.core.AllMusic;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ListenerFolia implements Listener {
    @EventHandler
    public void onPlayerQuitEvent(final PlayerQuitEvent e) {
        AllMusic.removeNowPlayPlayer(e.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        AllMusic.joinPlay(event.getPlayer().getName());
    }
}
