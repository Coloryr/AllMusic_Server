package Color_yr.ALLmusic_BC;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class ChannelListener implements Listener {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        if (e.getTag().equalsIgnoreCase("ALLmusic")) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            try {
                String channel = in.readUTF(); // channel we delivered
                if (channel.equals("get")) {
                    ServerInfo server = ProxyServer.getInstance().getPlayer(e.getReceiver().toString()).getServer().getInfo();
                    String input = in.readUTF(); // the inputstring
                    if (input.equals("test")) {

                    }

                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }

    public static void sendToBukkit(String channel, String message) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(channel);
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, ServerInfo> Server = ProxyServer.getInstance().getServers();
        Collection<ServerInfo> values = Server.values();
        Iterator<ServerInfo> iterator2 = values.iterator();
        while (iterator2.hasNext()) {
            ServerInfo server = iterator2.next();
            server.sendData("ALLmusic", stream.toByteArray());
        }
    }
}