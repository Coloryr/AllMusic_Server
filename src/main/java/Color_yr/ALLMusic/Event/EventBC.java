package Color_yr.ALLMusic.Event;

import Color_yr.ALLMusic.ALLMusic;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.nio.charset.StandardCharsets;

public class EventBC implements Listener {
    @EventHandler
    public void onPlayerquit(final PlayerDisconnectEvent event) {
        ALLMusic.NowPlayPlayer.remove(event.getPlayer().getName());
        ALLMusic.haveMOD.remove(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerLogin(final PostLoginEvent e) {
        ALLMusic.Side.Send(e.getPlayer().getName(), "[Check]", false);
        new Thread(() -> {
            ProxiedPlayer player = e.getPlayer();
            try {
                Thread.sleep(500);
                String data = "[Check]";
                byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
                ByteBuf buf = Unpooled.buffer(bytes.length + 1);
                buf.writeByte(666);
                buf.writeBytes(bytes);
                player.sendData(ALLMusic.channel, buf.array());
                Thread.sleep(2000);
            } catch (Exception ex) {

            }
            if (!ALLMusic.haveMOD.contains(player.getName())) {
                player.sendMessage(new TextComponent(ALLMusic.Message.getCheck().getNoMOD()));
            }
        }).start();
    }

    @EventHandler
    public void onRE(final PluginMessageEvent e) {
        if (e.getTag().equalsIgnoreCase(ALLMusic.channel)) {
            String a = new String(e.getData());
            if (a.contains("666")) {
                if (e.getSender() instanceof ProxiedPlayer) {
                    ProxiedPlayer player = (ProxiedPlayer) e.getSender();
                    ALLMusic.haveMOD.add(player.getName());
                    player.sendMessage(new TextComponent(ALLMusic.Message.getCheck().getMOD()));
                }
            }
        }
    }
}
