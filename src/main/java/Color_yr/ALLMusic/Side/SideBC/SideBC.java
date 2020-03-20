package Color_yr.ALLMusic.Side.SideBC;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.ALLMusicBC;
import Color_yr.ALLMusic.Side.ISide;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class SideBC implements ISide {
    @Override
    public void Send(String data, String player, Boolean isplay) {
        Send(ProxyServer.getInstance().getPlayer(player), data, isplay);
    }

    @Override
    public void Send(String data, Boolean isplay) {
        try {
            Collection<ProxiedPlayer> values = ProxyServer.getInstance().getPlayers();
            for (ProxiedPlayer players : values) {
                if (players == null || players.getServer() == null)
                    continue;
                if (!ALLMusic.haveMOD.contains(players.getName()))
                    continue;
                if (!ALLMusic.Config.getNoMusicServer().contains(players.getServer().getInfo().getName())) {
                    if (!ALLMusic.Config.getNoMusicPlayer().contains(players.getName())) {
                        Send(players, data, isplay);
                    }
                }
            }
        } catch (Exception e) {
            ALLMusic.log.warning("§d[ALLMusic]§c歌曲发送发生错误");
            e.printStackTrace();
        }
    }

    @Override
    public int GetAllPlayer() {
        return ProxyServer.getInstance().getOnlineCount();
    }

    @Override
    public void SendLyric(String data) {
        try {
            Collection<ProxiedPlayer> values = ProxyServer.getInstance().getPlayers();
            for (ProxiedPlayer players : values) {
                if (players == null || players.getServer() == null)
                    continue;
                if (!ALLMusic.haveMOD.contains(players.getName()))
                    continue;
                if (!ALLMusic.Config.getNoMusicServer().contains(players.getServer().getInfo().getName())) {
                    if (!ALLMusic.Config.getNoMusicPlayer().contains(players.getName()))
                        if (ALLMusic.NowPlayPlayer.contains(players.getName()))
                            players.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(data));
                }
            }
        } catch (Exception e) {
            ALLMusic.log.warning("§d[ALLMusic]§c歌词发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void bq(String data) {
        ProxyServer.getInstance().broadcast(new TextComponent(data));
    }

    @Override
    public void bqt(String data) {
        ProxyServer.getInstance().broadcast(new TextComponent(data));
    }

    @Override
    public boolean NeedPlay() {
        int online = GetAllPlayer();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (ALLMusic.Config.getNoMusicPlayer().contains(player.getName())) {
                online--;
            } else {
                if (player.getServer() != null) {
                    ServerInfo server = player.getServer().getInfo();
                    if (server != null && ALLMusic.Config.getNoMusicServer().contains(server.getName())) {
                        online--;
                    }
                }
            }
        }
        return online > 0;
    }

    @Override
    public void SendMessaget(Object obj, String Message) {
        CommandSender sender = (CommandSender) obj;
        sender.sendMessage(new TextComponent(Message));
    }

    @Override
    public void SendMessage(Object obj, String Message) {
        CommandSender sender = (CommandSender) obj;
        sender.sendMessage(new TextComponent(Message));
    }

    @Override
    public void SendMessage(Object obj, String Message, ClickEvent.Action action, String Command) {
        CommandSender sender = (CommandSender) obj;
        TextComponent send = new TextComponent(Message);
        send.setClickEvent(new ClickEvent(action, Command));
        sender.sendMessage(send);
    }

    @Override
    public void RunTask(Runnable run) {
        ProxyServer.getInstance().getScheduler().runAsync(ALLMusicBC.ALLMusicP, run);
    }

    @Override
    public void reload() {
        ALLMusicBC.setConfig();
    }

    @Override
    public boolean checkPermission(String player, String permission) {
        ProxiedPlayer player1 = ProxyServer.getInstance().getPlayer(player);
        if (player1 == null)
            return true;
        return !player1.hasPermission(permission);
    }

    private void Send(ProxiedPlayer players, String data, Boolean isplay) {
        if (players == null)
            return;
        try {
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
            buf.writeByte(666);
            buf.writeBytes(bytes);
            players.sendData(ALLMusic.channel, buf.array());
            if (isplay != null) {
                if (isplay) {
                    ALLMusic.NowPlayPlayer.add(players.getName());
                } else {
                    ALLMusic.NowPlayPlayer.remove(players.getName());
                }
            }
        } catch (Exception e) {
            ALLMusic.log.warning("§d[ALLMusic]§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}
