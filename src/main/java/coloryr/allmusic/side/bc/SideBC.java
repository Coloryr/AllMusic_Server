package coloryr.allmusic.side.bc;

import coloryr.allmusic.AllMusic;
import coloryr.allmusic.AllMusicBC;
import coloryr.allmusic.hud.HudUtils;
import coloryr.allmusic.objs.hud.SaveOBJ;
import coloryr.allmusic.objs.music.SongInfoObj;
import coloryr.allmusic.objs.music.MusicObj;
import coloryr.allmusic.music.play.PlayMusic;
import coloryr.allmusic.side.ComType;
import coloryr.allmusic.side.ISide;
import coloryr.allmusic.side.bc.event.MusicAddEvent;
import coloryr.allmusic.side.bc.event.MusicPlayEvent;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

public class SideBC implements ISide {
    public static final Set<Server> TopServers = new CopyOnWriteArraySet<>();

    @Override
    public void send(String data, String player, Boolean isplay) {
        send(ProxyServer.getInstance().getPlayer(player), data, isplay);
    }

    @Override
    public int getAllPlayer() {
        return ProxyServer.getInstance().getOnlineCount();
    }

    @Override
    public void sendHudLyric(String data) {
        try {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (isOK(player))
                    continue;
                SaveOBJ obj = HudUtils.get(player.getName());
                if (!obj.EnableLyric)
                    continue;
                send(player, ComType.lyric + data, null);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudInfo(String data) {
        try {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (isOK(player))
                    continue;
                SaveOBJ obj = HudUtils.get(player.getName());
                if (!obj.EnableInfo)
                    continue;
                send(player, ComType.info + data, null);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词信息发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudList(String data) {
        try {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (isOK(player))
                    continue;
                String name = player.getName();
                SaveOBJ obj = HudUtils.get(name);
                if (!obj.EnableList)
                    continue;
                send(player, ComType.list + data, null);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲列表发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudUtilsAll() {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            try {
                SaveOBJ obj = HudUtils.get(player.getName());
                String data = AllMusic.gson.toJson(obj);
                send(player, data, null);
            } catch (Exception e1) {
                AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void sendBar(String data) {
        TextComponent message = new TextComponent(data);
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            try {
                player.sendMessage(ChatMessageType.ACTION_BAR, message);
            } catch (Exception e1) {
                AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void sendMusic(String url) {
        try {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (isOK(player))
                    continue;
                send(player, ComType.play + url, true);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c图片数据发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendPic(String url) {
        try {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (isOK(player))
                    continue;
                String name = player.getName();
                SaveOBJ obj = HudUtils.get(name);
                if (!obj.EnablePic)
                    continue;
                send(player, ComType.img + url, null);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c图片数据发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendStop() {
        try {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                send(player, ComType.stop, false);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c停止指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendStop(String name) {
        try {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);
            if (player == null)
                return;
            send(player, ComType.stop, false);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c停止指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void clearHud(String player) {
        send(ComType.clear, player, null);
    }

    @Override
    public void clearHud() {
        try {
            Collection<ProxiedPlayer> values = ProxyServer.getInstance().getPlayers();
            for (ProxiedPlayer players : values) {
                send(players, ComType.clear, null);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void bq(String data) {
        if (AllMusic.getConfig().MessageLimit
                && data.length() > AllMusic.getConfig().MessageLimitSize) {
            data = data.substring(0, AllMusic.getConfig().MessageLimitSize - 1) + "...";
        }
        TextComponent message = new TextComponent(data);
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (isOK(player))
                continue;
            player.sendMessage(message);
        }
    }

    @Override
    public void bqt(String data) {
        this.bq(data);
    }

    @Override
    public boolean needPlay() {
        int online = 0;
        for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
            if (AllMusic.getConfig().NoMusicServer.contains(server.getName()))
                continue;
            for (ProxiedPlayer player : server.getPlayers())
                if (!AllMusic.getConfig().NoMusicPlayer.contains(player.getName()))
                    online++;
        }
        return online > 0;
    }


    @Override
    public void sendMessaget(Object obj, String message) {
        CommandSender sender = (CommandSender) obj;
        sender.sendMessage(new TextComponent(message));
    }

    @Override
    public void sendMessage(Object obj, String message) {
        CommandSender sender = (CommandSender) obj;
        sender.sendMessage(new TextComponent(message));
    }

    @Override
    public void sendMessageRun(Object obj, String message, String end, String command) {
        CommandSender sender = (CommandSender) obj;
        TextComponent send = new TextComponent(message + end);
        send.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        sender.sendMessage(send);
    }

    @Override
    public void sendMessageSuggest(Object obj, String message, String end, String command) {
        CommandSender sender = (CommandSender) obj;
        TextComponent send = new TextComponent(message + end);
        send.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        sender.sendMessage(send);
    }

    @Override
    public void runTask(Runnable run) {
        ProxyServer.getInstance().getScheduler().runAsync(AllMusicBC.plugin, run);
    }

    @Override
    public void reload() {
        new AllMusic().init(AllMusicBC.plugin.getDataFolder());
    }

    @Override
    public boolean checkPermission(String player, String permission) {
        if (AllMusic.getConfig().Admin.contains(player))
            return false;
        ProxiedPlayer player1 = ProxyServer.getInstance().getPlayer(player);
        if (player1 == null)
            return true;
        return !player1.hasPermission(permission);
    }

    @Override
    public void runTask(Runnable run, int delay) {
        ProxyServer.getInstance().getScheduler()
                .schedule(AllMusicBC.plugin, run, delay, TimeUnit.MICROSECONDS);
    }

    public static void sendAllToServer(Server server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeInt(0);
        if (PlayMusic.nowPlayMusic == null)
            out.writeUTF(AllMusic.getMessage().PAPI.NoMusic);
        else {
            if (AllMusic.getConfig().MessageLimit
                    && PlayMusic.nowPlayMusic.getName().length() > AllMusic.getConfig().MessageLimitSize) {
                out.writeUTF(PlayMusic.nowPlayMusic.getName()
                        .substring(0, AllMusic.getConfig().MessageLimitSize));
            } else {
                out.writeUTF(PlayMusic.nowPlayMusic.getName());
            }
        }

        server.sendData(AllMusic.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(1);
        if (PlayMusic.nowPlayMusic == null)
            out.writeUTF("");
        else
            out.writeUTF(PlayMusic.nowPlayMusic.getAl());
        server.sendData(AllMusic.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(2);
        if (PlayMusic.nowPlayMusic == null)
            out.writeUTF("");
        else
            out.writeUTF(PlayMusic.nowPlayMusic.getAlia());
        server.sendData(AllMusic.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(3);
        if (PlayMusic.nowPlayMusic == null)
            out.writeUTF("");
        else
            out.writeUTF(PlayMusic.nowPlayMusic.getAuthor());
        server.sendData(AllMusic.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(4);
        if (PlayMusic.nowPlayMusic == null)
            out.writeUTF("");
        else
            out.writeUTF(PlayMusic.nowPlayMusic.getCall());
        server.sendData(AllMusic.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(5);
        out.writeInt(PlayMusic.getSize());
        server.sendData(AllMusic.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(6);
        out.writeUTF(PlayMusic.getAllList());
        server.sendData(AllMusic.channelBC, out.toByteArray());
    }

    public static void sendLyricToServer(Server server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeInt(7);
        if (PlayMusic.lyric == null)
            out.writeUTF("");
        else
            out.writeUTF(PlayMusic.lyric.getLyric());
        server.sendData(AllMusic.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(8);
        if (PlayMusic.lyric == null || PlayMusic.lyric.getTlyric() == null)
            out.writeUTF("");
        else
            out.writeUTF(PlayMusic.lyric.getTlyric());
        server.sendData(AllMusic.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(9);
        out.writeBoolean(PlayMusic.lyric != null && PlayMusic.lyric.getTlyric() != null);
        server.sendData(AllMusic.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(10);
        if (PlayMusic.lyric == null || PlayMusic.lyric.getKly() == null)
            out.writeUTF("");
        else
            out.writeUTF(PlayMusic.lyric.getTlyric());
        server.sendData(AllMusic.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(11);
        out.writeBoolean(PlayMusic.lyric != null && PlayMusic.lyric.getKly() != null);
        server.sendData(AllMusic.channelBC, out.toByteArray());
    }

    @Override
    public void updateInfo() {
        for (Server server : TopServers) {
            if (server.isConnected()) {
                sendAllToServer(server);
            } else {
                TopServers.remove(server);
            }
        }
    }

    @Override
    public void updateLyric() {
        for (Server server : TopServers) {
            if (server.isConnected()) {
                sendLyricToServer(server);
            } else {
                TopServers.remove(server);
            }
        }
    }

    @Override
    public void ping() {
        Iterator<Server> iterator = TopServers.iterator();
        while (iterator.hasNext()) {
            Server server = iterator.next();
            if (server.isConnected()) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeInt(200);
                server.sendData(AllMusic.channelBC, out.toByteArray());
            } else {
                iterator.remove();
            }
        }
    }

    @Override
    public boolean onMusicPlay(SongInfoObj obj) {
        MusicPlayEvent event = new MusicPlayEvent(obj);
        ProxyServer.getInstance().getPluginManager().callEvent(event);
        return event.isCancel();
    }

    @Override
    public boolean onMusicAdd(Object obj, MusicObj music) {
        MusicAddEvent event = new MusicAddEvent(music, (CommandSender) obj);
        ProxyServer.getInstance().getPluginManager().callEvent(event);
        return event.isCancel();
    }

    private void send(ProxiedPlayer players, String data, Boolean isplay) {
        if (players == null)
            return;
        try {
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
            buf.writeByte(666);
            buf.writeBytes(bytes);
            runTask(() -> players.sendData(AllMusic.channel, buf.array()));
            if (isplay != null) {
                if (isplay) {
                    AllMusic.addNowPlayPlayer(players.getName());
                } else {
                    AllMusic.removeNowPlayPlayer(players.getName());
                }
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
            e.printStackTrace();
        }
    }

    private boolean isOK(ProxiedPlayer player) {
        if (player == null || player.getServer() == null)
            return true;
        if (AllMusic.getConfig().NoMusicServer
                .contains(player.getServer().getInfo().getName()))
            return true;
        String name = player.getName();
        if (AllMusic.getConfig().NoMusicPlayer.contains(player.getName()))
            return true;
        return AllMusic.containNowPlay(name);
    }
}
