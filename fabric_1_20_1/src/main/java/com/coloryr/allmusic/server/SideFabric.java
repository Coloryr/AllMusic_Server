package com.coloryr.allmusic.server;

import com.coloryr.allmusic.buffercodec.MusicPacketCodec;
import com.coloryr.allmusic.codec.CommandType;
import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.music.PlayerAddMusicObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.side.BaseSide;
import com.coloryr.allmusic.server.event.MusicAddEvent;
import com.coloryr.allmusic.server.event.MusicPlayEvent;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.io.File;
import java.util.Collection;

public class SideFabric extends BaseSide {
    @Override
    public void runTask(Runnable run) {
        AllMusicServer.server.execute(run);
    }

    @Override
    public void runTask(Runnable run1, int delay) {
        Tasks.add(new TaskItem() {{
            tick = delay;
            run = run1;
        }});
    }

    @Override
    public boolean checkPermission(Object player) {
        ServerPlayer source = (ServerPlayer) player;
        return source.hasPermissions(2);
    }

    @Override
    public boolean isPlayer(Object player) {
        return player instanceof ServerPlayer;
    }

    @Override
    public boolean checkPermission(Object player, String permission) {
        return checkPermission(player);
    }

    @Override
    public boolean needPlay(boolean islist) {
        for (var player : AllMusicServer.server.getPlayerList().getPlayers()) {
            if (!AllMusic.isSkip(player.getName().getString(), null, false, islist)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<?> getPlayers() {
        return AllMusicServer.server.getPlayerList().getPlayers();
    }

    @Override
    public String getPlayerName(Object player) {
        if (player instanceof ServerPlayer player1) {
            return player1.getName().getString();
        }

        return null;
    }

    @Override
    public String getPlayerServer(Object player) {
        return null;
    }

    @Override
    public void send(Object player, CommandType type, String data, int data1) {
        if (player instanceof ServerPlayer player1) {
            send(player1, MusicPacketCodec.pack(type, data, data1));
        }
    }

    @Override
    public Object getPlayer(String player) {
        return AllMusicServer.server.getPlayerList().getPlayerByName(player);
    }

    @Override
    public void sendBar(Object player, Component data) {
        if (player instanceof ServerPlayer player1) {
            player1.sendActionBar(data);
        }
    }

    @Override
    public File getFolder() {
        return new File(AllMusicServer.dir);
    }

    @Override
    public void broadcast(Component message) {
        for (var player : AllMusicServer.server.getPlayerList().getPlayers()) {
            if (!AllMusic.isSkip(player.getName().getString(), null, false)) {
                player.sendMessage(message);
            }
        }
    }

    @Override
    public void sendMessage(Object obj, Component message) {
        if (obj instanceof CommandSourceStack source) {
            source.sendMessage(message);
        }
    }

    @Override
    public boolean onMusicPlay(SongInfoObj obj) {
        return !MusicPlayEvent.EVENT.invoker().interact(obj);
    }

    @Override
    public boolean onMusicAdd(Object obj, PlayerAddMusicObj music) {
        CommandSourceStack source = (CommandSourceStack) obj;
        return !MusicAddEvent.EVENT.invoker().interact(source.getPlayer(), music);
    }

    private void send(ServerPlayer players, ByteBuf data) {
        if (players == null)
            return;
        runTask(() -> ServerPlayNetworking.send(players, AllMusicServer.ID, new FriendlyByteBuf(data)));
    }
}
