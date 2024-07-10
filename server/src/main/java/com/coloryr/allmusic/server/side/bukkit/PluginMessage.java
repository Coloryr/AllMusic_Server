package com.coloryr.allmusic.server.side.bukkit;

import com.coloryr.allmusic.server.AllMusicBukkit;
import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.play.PlayMusic;
import com.coloryr.allmusic.server.core.music.play.TopLyricSave;
import com.coloryr.allmusic.server.core.objs.music.TopSongInfoObj;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PluginMessage implements PluginMessageListener {
    public static int size;
    public static String allList;
    public static boolean update = false;
    private static ScheduledExecutorService service;
    private final TopSongInfoObj info;
    private final TopLyricSave lyric;

    public PluginMessage() {
        info = (TopSongInfoObj) PlayMusic.nowPlayMusic;
        lyric = (TopLyricSave) PlayMusic.lyric;

        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(PluginMessage::clear, 0, 30, TimeUnit.SECONDS);
    }

    private static void clear() {
        update = false;
    }

    private static void sendPack(ByteArrayDataOutput out) {
        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (player == null)
            return;
        player.sendPluginMessage(AllMusicBukkit.plugin, AllMusic.channelBC, out.toByteArray());
    }

    public static void startUpdate() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeInt(255);
        out.writeUTF("allmusic");
        sendPack(out);
    }

    public void stop() {
        service.shutdownNow();
    }

    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player player, byte[] message) {
        if (!channel.equals(AllMusic.channelBC)) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        int type = in.readInt();
        update = true;
        switch (type) {
            case 0:
                info.setName(in.readUTF());
                break;
            case 1:
                info.setAl(in.readUTF());
                break;
            case 2:
                info.setAlia(in.readUTF());
                break;
            case 3:
                info.setAuthor(in.readUTF());
                break;
            case 4:
                info.setCall(in.readUTF());
                break;
            case 5:
                size = in.readInt();
                break;
            case 6:
                allList = in.readUTF();
                break;
            case 7:
                lyric.setLyric(in.readUTF());
                break;
            case 8:
                lyric.setTlyric(in.readUTF());
                break;
            case 9:
                lyric.setHaveT(in.readBoolean());
                break;
            case 10:
                lyric.setKly(in.readUTF());
                break;
            case 11:
                lyric.setHaveK(in.readBoolean());
                break;
            case 12: {
                String uuid = in.readUTF();
                int cost = in.readInt();
                String name = in.readUTF();

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.write(12);
                out.writeUTF(uuid);
                if (AllMusic.economy == null) {
                    out.write(0);
                    sendPack(out);
                } else if (!AllMusic.economy.check(name, cost)) {
                    out.write(1);
                    sendPack(out);
                } else {
                    out.write(2);
                    sendPack(out);
                }
                break;
            }
            case 13: {
                String uuid = in.readUTF();
                int cost = in.readInt();
                String name = in.readUTF();

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.write(13);
                out.writeUTF(uuid);
                if (AllMusic.economy == null) {
                    out.write(0);
                    sendPack(out);
                } else if (!AllMusic.economy.cost(name, cost)) {
                    out.write(1);
                    sendPack(out);
                } else {
                    out.write(2);
                    sendPack(out);
                }
                break;
            }
        }
    }
}
