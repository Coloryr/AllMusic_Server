package Color_yr.ALLMusic.Event;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.ALLMusicBukkit;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.nio.charset.StandardCharsets;

public class EventBukkit implements Listener {
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        ALLMusic.NowPlayPlayer.remove(e.getPlayer().getName());
        ALLMusic.RemovePlayer(e.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerLogin(final PlayerJoinEvent event) {
        if (!ALLMusic.Config.isModCheck())
            return;
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Player player = event.getPlayer();
                try {
                    String data = "[Check]";
                    byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
                    ByteBuf buf = Unpooled.buffer(bytes.length + 1);
                    buf.writeByte(666);
                    buf.writeBytes(bytes);
                    player.sendPluginMessage(ALLMusicBukkit.ALLMusicP, ALLMusic.channel, buf.array());
                    ALLMusic.NowPlayPlayer.remove(player.getName());
                } catch (Exception e) {
                    ALLMusic.log.warning("§c数据发送发生错误");
                    e.printStackTrace();
                }
                Thread.sleep(5000);
                if (!ALLMusic.havelPlayer(player.getName())) {
                    player.sendMessage(ALLMusic.Message.getCheck().getNoMOD());
                }
            } catch (Exception e) {

            }
        }).start();
    }
}
