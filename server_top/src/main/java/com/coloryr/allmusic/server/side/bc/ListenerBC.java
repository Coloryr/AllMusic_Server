package com.coloryr.allmusic.server.side.bc;

import com.coloryr.allmusic.server.core.AllMusic;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ListenerBC implements Listener {
    @EventHandler
    public void onPlayerDisconnectEvent(final PlayerDisconnectEvent event) {
        AllMusic.removeNowPlayPlayer(event.getPlayer().getName());
    }

    @EventHandler
    public void onServerConnectedEvent(ServerConnectedEvent event) {
        AllMusic.side.runTask(() -> {
            AllMusic.joinPlay(event.getPlayer().getName());
        }, 500);
    }

    @EventHandler
    public void onPluginMessageEvent(PluginMessageEvent event) {
        if (event.getTag().equals(AllMusic.channelBC)) {
            event.setCancelled(true);
            ByteArrayDataInput data = ByteStreams.newDataInput(event.getData());
            int type = data.readInt();
            if (type == 255 && event.getSender() instanceof Server) {
                Server server = (Server) event.getSender();
                SideBC.TopServers.add(server);
                SideBC.sendAllToServer(server);
                SideBC.sendLyricToServer(server);
            } else if (type == 12 || type == 13) {
                String uuid = data.readUTF();
                int res = data.readInt();
                SideBC.SendToBackend.put(uuid, res);
            }
        }
    }
}
