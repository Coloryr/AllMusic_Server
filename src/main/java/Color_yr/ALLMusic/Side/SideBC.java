package Color_yr.ALLMusic.Side;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.ALLMusicBC;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static Color_yr.ALLMusic.Play.PlayMusic.NowPlay;
import static Color_yr.ALLMusic.Play.PlayMusic.PlayList;

public class SideBC implements ISide {
    @Override
    public void Send(String data, String player, Boolean isplay) {
        Send(ProxyServer.getInstance().getPlayer(player), data, isplay);
    }
    @Override
    public void Send(String data, Boolean isplay) {
        Collection<ProxiedPlayer> values = ProxyServer.getInstance().getPlayers();
        for (ProxiedPlayer players : values) {
            if (!ALLMusic.Config.getNoMusicServer().contains(players.getServer().getInfo().getName())) {
                if (!ALLMusic.Config.getNoMusicPlayer().contains(players.getName())) {
                    Send(players, data, isplay);
                }
            }
        }
    }

    @Override
    public int GetAllPlayer() {
        return ProxyServer.getInstance().getOnlineCount();
    }

    @Override
    public void SendLyric(String data) {
        Collection<ProxiedPlayer> values = ProxyServer.getInstance().getPlayers();
        for (ProxiedPlayer players : values) {
            if (!ALLMusic.Config.getNoMusicServer().contains(players.getServer().getInfo().getName())) {
                if (!ALLMusic.Config.getNoMusicPlayer().contains(players.getName()))
                    if (NowPlay.contains(players.getName()))
                        players.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(data));
            }
        }
    }

    @Override
    public void bq(String data) {
        ProxyServer.getInstance().broadcast(new TextComponent(data));
    }

    @Override
    public void save() {
        ALLMusicBC.save();
    }

    private void Send(ProxiedPlayer players, String data,Boolean isplay) {
        try {
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
            buf.writeByte(666);
            buf.writeBytes(bytes);
            players.sendData(ALLMusic.channel, buf.array());
            if (isplay != null) {
                if (isplay) {
                    NowPlay.add(players.getName());
                } else {
                    NowPlay.remove(players.getName());
                }
            }
        } catch (Exception e) {
            ALLMusic.log.warning("§d[ALLMusic]§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}
