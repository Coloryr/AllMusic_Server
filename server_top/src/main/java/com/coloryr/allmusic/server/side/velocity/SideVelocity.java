package com.coloryr.allmusic.server.side.velocity;

import com.coloryr.allmusic.server.AllMusicVelocity;
import com.coloryr.allmusic.server.codec.PacketCodec;
import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.play.PlayMusic;
import com.coloryr.allmusic.server.core.objs.enums.ComType;
import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.side.BaseSide;
import com.coloryr.allmusic.server.core.sql.IEconomy;
import com.coloryr.allmusic.server.side.velocity.event.MusicAddEvent;
import com.coloryr.allmusic.server.side.velocity.event.MusicPlayEvent;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import io.netty.buffer.ByteBuf;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

public class SideVelocity extends BaseSide implements IEconomy {
    public static final Set<ServerConnection> TopServers = new CopyOnWriteArraySet<>();

    public static final Map<String, Integer> SendToBackend = new ConcurrentHashMap<>();

    public static void sendAllToServer(ServerConnection server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeInt(0);
        if (PlayMusic.nowPlayMusic == null)
            out.writeUTF(AllMusic.getMessage().papi.emptyMusic);
        else {
            out.writeUTF(PlayMusic.nowPlayMusic.getName());
        }
        server.sendPluginMessage(AllMusicVelocity.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(1);
        if (PlayMusic.nowPlayMusic == null)
            out.writeUTF("");
        else
            out.writeUTF(PlayMusic.nowPlayMusic.getAl());
        server.sendPluginMessage(AllMusicVelocity.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(2);
        if (PlayMusic.nowPlayMusic == null)
            out.writeUTF("");
        else
            out.writeUTF(PlayMusic.nowPlayMusic.getAlia());
        server.sendPluginMessage(AllMusicVelocity.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(3);
        if (PlayMusic.nowPlayMusic == null)
            out.writeUTF("");
        else
            out.writeUTF(PlayMusic.nowPlayMusic.getAuthor());
        server.sendPluginMessage(AllMusicVelocity.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(4);
        if (PlayMusic.nowPlayMusic == null)
            out.writeUTF("");
        else
            out.writeUTF(PlayMusic.nowPlayMusic.getCall());
        server.sendPluginMessage(AllMusicVelocity.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(5);
        out.writeInt(PlayMusic.getListSize());
        server.sendPluginMessage(AllMusicVelocity.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(6);
        out.writeUTF(PlayMusic.getAllList());
        server.sendPluginMessage(AllMusicVelocity.channelBC, out.toByteArray());
    }

    public static void sendLyricToServer(ServerConnection server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeInt(7);
        if (PlayMusic.lyric == null)
            out.writeUTF("");
        else
            out.writeUTF(PlayMusic.lyric.getLyric());
        server.sendPluginMessage(AllMusicVelocity.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(8);
        if (PlayMusic.lyric == null || PlayMusic.lyric.getTlyric() == null)
            out.writeUTF("");
        else
            out.writeUTF(PlayMusic.lyric.getTlyric());
        server.sendPluginMessage(AllMusicVelocity.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(9);
        out.writeBoolean(PlayMusic.lyric != null && PlayMusic.lyric.getTlyric() != null);
        server.sendPluginMessage(AllMusicVelocity.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(10);
        if (PlayMusic.lyric == null || PlayMusic.lyric.getKly() == null)
            out.writeUTF("");
        else
            out.writeUTF(PlayMusic.lyric.getTlyric());
        server.sendPluginMessage(AllMusicVelocity.channelBC, out.toByteArray());

        out = ByteStreams.newDataOutput();
        out.writeInt(11);
        out.writeBoolean(PlayMusic.lyric != null && PlayMusic.lyric.getKly() != null);
        server.sendPluginMessage(AllMusicVelocity.channelBC, out.toByteArray());
    }

    private static void writeString(ByteBuf buf, String text) {
        byte[] temp = text.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(temp.length);
        buf.writeBytes(temp);
    }

    @Override
    public void broadcast(String data) {
        if (data == null || data.isEmpty())
            return;
        Component message = Component.text(data);
        for (Player player : AllMusicVelocity.plugin.server.getAllPlayers()) {
            if (skip(player)) {
                continue;
            }

            player.sendMessage(message);
        }
    }

    @Override
    public void broadcastWithRun(String message, String end, String command) {
        if (message == null || message.isEmpty())
            return;
        TextComponent endtext = Component.text(end)
                .clickEvent(ClickEvent.runCommand(command));
        TextComponent send = Component.text(message).append(endtext);
        for (Player player : AllMusicVelocity.plugin.server.getAllPlayers()) {
            if (skip(player)) {
                continue;
            }

            player.sendMessage(send);
        }
    }

    @Override
    public boolean needPlay(boolean islist) {
        for (Player player : AllMusicVelocity.plugin.server.getAllPlayers()) {
            String server = null;
            if (player.getCurrentServer().isPresent()) {
                server = player.getCurrentServer().get().getServerInfo().getName();
            }

            if (!AllMusic.isSkip(player.getUsername(), server, false, islist)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Collection<?> getPlayers() {
        return AllMusicVelocity.plugin.server.getAllPlayers();
    }

    @Override
    public String getPlayerName(Object player) {
        if (player instanceof Player) {
            Player player1 = (Player) player;
            return player1.getUsername();
        }
        return null;
    }

    @Override
    public String getPlayerServer(Object player) {
        if (player instanceof Player) {
            Player player1 = (Player) player;
            Optional<ServerConnection> serverConnection = player1.getCurrentServer();
            if (serverConnection.isPresent()) {
                return serverConnection.get().getServer().getServerInfo().getName();
            }
        }

        return null;
    }

    @Override
    public void send(Object player, ComType type, String data, int data1) {
        if (player instanceof Player) {
            Player player1 = (Player) player;
            send(player1, PacketCodec.pack(type, data, data1));
        }
    }

    @Override
    public Object getPlayer(String player) {
        return AllMusicVelocity.plugin.server.getPlayer(player);
    }

    @Override
    public void sendBar(Object player, String data) {
        if (player instanceof Player) {
            Player player1 = (Player) player;
            Component message = Component.text(data);
            player1.sendActionBar(message);
        }
    }

    @Override
    public File getFolder() {
        return AllMusicVelocity.plugin.dataDirectory.toFile();
    }

    @Override
    public void sendMessage(Object obj, String message) {
        if (message == null || message.isEmpty())
            return;
        CommandSource sender = (CommandSource) obj;
        sender.sendMessage(Component.text(message));
    }

    @Override
    public void sendMessageRun(Object obj, String message, String end, String command) {
        if (message == null || message.isEmpty())
            return;
        CommandSource sender = (CommandSource) obj;
        TextComponent endtext = Component.text(end)
                .clickEvent(ClickEvent.runCommand(command));
        TextComponent send = Component.text(message).append(endtext);
        sender.sendMessage(send);
    }

    @Override
    public void sendMessageSuggest(Object obj, String message, String end, String command) {
        if (message == null || message.isEmpty())
            return;
        CommandSource sender = (CommandSource) obj;
        TextComponent endtext = Component.text(end)
                .clickEvent(ClickEvent.suggestCommand(command));
        TextComponent send = Component.text(message).append(endtext);
        sender.sendMessage(send);
    }

    @Override
    public void runTask(Runnable run) {
        AllMusicVelocity.plugin.server.getScheduler().buildTask(AllMusicVelocity.plugin, run).schedule();
    }

    @Override
    public boolean checkPermission(Object player, String permission) {
        if (checkPermission(player)) {
            return true;
        }
        if (player instanceof PermissionSubject) {
            return ((PermissionSubject) player).hasPermission(permission);
        }
        return false;
    }

    @Override
    public boolean checkPermission(Object player) {
        return player instanceof ConsoleCommandSource;
    }

    @Override
    public boolean isPlayer(Object source) {
        return source instanceof Player;
    }

    @Override
    public void runTask(Runnable run, int delay) {
        AllMusicVelocity.plugin.server.getScheduler().buildTask(AllMusicVelocity.plugin, run)
                .delay(delay, TimeUnit.MICROSECONDS).schedule();
    }

    @Override
    public void ping() {
        for (ServerConnection server : new HashSet<>(TopServers)) {
            try {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeInt(200);
                server.sendPluginMessage(AllMusicVelocity.channelBC, out.toByteArray());
            } catch (Exception e) {
                TopServers.remove(server);
            }
        }
    }

    @Override
    public boolean onMusicPlay(SongInfoObj obj) {
        MusicPlayEvent event = new MusicPlayEvent(obj);
        AllMusicVelocity.plugin.server.getEventManager().fire(event).join();
        return event.isCancel();
    }

    @Override
    public boolean onMusicAdd(Object obj, MusicObj music) {
        MusicAddEvent event = new MusicAddEvent(music, (CommandSource) obj);
        AllMusicVelocity.plugin.server.getEventManager().fire(event).join();
        return event.isCancel();
    }

    @Override
    public void updateInfo() {
        for (ServerConnection server : TopServers) {
            try {
                sendAllToServer(server);
            } catch (Exception e) {
                TopServers.remove(server);
            }
        }
    }

    @Override
    public void updateLyric() {
        for (ServerConnection server : TopServers) {
            try {
                sendLyricToServer(server);
            } catch (Exception e) {
                TopServers.remove(server);
            }
        }
    }

    private void send(Player players, ByteBuf data) {
        if (players == null)
            return;
        try {
            runTask(() -> players.sendPluginMessage(AllMusicVelocity.channel, data.array()));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
            e.printStackTrace();
        }
    }

    private boolean skip(Player player) {
        String server = null;
        if (player.getCurrentServer().isPresent()) {
            server = player.getCurrentServer().get().getServerInfo().getName();
        }

        return AllMusic.isSkip(player.getUsername(), server, true);
    }

    @Override
    public boolean check(String name, int cost) {
        return topEconomy(name, cost, 12);
    }

    @Override
    public boolean cost(String name, int cost) {
        return topEconomy(name, cost, 13);
    }

    private boolean topEconomy(String name, int cost, int type) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeInt(type);
        String uuid;
        do {
            uuid = UUID.randomUUID().toString();
        } while (SendToBackend.containsKey(uuid));

        SendToBackend.put(uuid, -1);
        String server = AllMusic.getConfig().economy.backend;
        ServerConnection toServer = null;
        for (ServerConnection connection : TopServers) {
            if (connection.getServerInfo().getName().equalsIgnoreCase(server)) {
                toServer = connection;
            }
        }
        if (toServer == null) {
            AllMusic.log.warning("§d[AllMusic]§c没有找到目标服务器");
            return false;
        }

        out.writeUTF(uuid);
        out.write(cost);
        out.writeUTF(name);

        toServer.sendPluginMessage(AllMusicVelocity.channelBC, out.toByteArray());

        Integer res;

        int count = 0;

        do {
            try {
                res = SendToBackend.get(uuid);
                if (res == null)
                    return false;
                else if (res == -1) {
                    Thread.sleep(1);
                    count++;
                } else if (res == 0) {
                    AllMusic.log.warning("§d[AllMusic]§c后端经济插件错误");
                    SendToBackend.remove(uuid);
                    return false;
                } else if (res == 1) {
                    SendToBackend.remove(uuid);
                    return false;
                } else if (res == 2) {
                    SendToBackend.remove(uuid);
                    return true;
                }
            } catch (Exception e) {
                AllMusic.log.warning("§d[AllMusic]§c经济数据发送错误");
                e.printStackTrace();
            }
        } while (count < 100);

        AllMusic.log.warning("§d[AllMusic]§c经济数据请求超时");

        return false;
    }
}
