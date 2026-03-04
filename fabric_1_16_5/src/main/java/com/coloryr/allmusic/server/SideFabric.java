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
        CommandSourceStack source = (CommandSourceStack) player;
        return source.hasPermission(2);
    }

    @Override
    public boolean isPlayer(Object player) {
        CommandSourceStack source = (CommandSourceStack) player;
        return source.getEntity() instanceof ServerPlayer;
    }

    @Override
    public boolean checkPermission(Object player, String permission) {
        return checkPermission(player);
    }

    @Override
    public Collection<?> getPlayers() {
        return AllMusicServer.server.getPlayerList().getPlayers();
    }

    @Override
    public String getPlayerName(Object player) {
        if (player instanceof ServerPlayer) {
            ServerPlayer player1 = (ServerPlayer) player;
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
        if (player instanceof ServerPlayer) {
            ServerPlayer player1 = (ServerPlayer) player;
            send(player1, MusicPacketCodec.pack(type, data, data1));
        }
    }

    @Override
    public Object getPlayer(String player) {
        return null;
    }

    @Override
    public void sendBar(Object player, Component data) {
        if (player instanceof ServerPlayer) {
            ServerPlayer player1 = (ServerPlayer) player;
            AllMusicServer.audiences.audience(player1).sendActionBar(data);
        }
    }

    @Override
    public File getFolder() {
        return new File(AllMusicServer.dir);
    }

    @Override
    public boolean needPlay(boolean islist) {
        for (ServerPlayer player : AllMusicServer.server.getPlayerList().getPlayers()) {
            if (!AllMusic.isSkip(player.getName().getString(), null, false, islist)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void broadcast(Component message) {
        for (ServerPlayer player : AllMusicServer.server.getPlayerList().getPlayers()) {
            AllMusicServer.audiences.audience(player).sendMessage(message);
        }
    }

    @Override
    public void sendMessage(Object obj, Component message1) {
        if (obj instanceof CommandSourceStack) {
            CommandSourceStack sender = (CommandSourceStack) obj;
            AllMusicServer.audiences.audience(sender).sendMessage(message1);
        }
    }

    @Override
    public boolean onMusicPlay(SongInfoObj obj) {
        return MusicPlayEvent.EVENT.invoker().interact(obj);
    }

    @Override
    public boolean onMusicAdd(Object obj, PlayerAddMusicObj music) {
        CommandSourceStack sender = (CommandSourceStack) obj;
        ServerPlayer entity = null;
        if (sender.getEntity() instanceof ServerPlayer) {
            entity = (ServerPlayer) sender.getEntity();
        }
        return MusicAddEvent.EVENT.invoker().interact(entity, music);
    }

    private void send(ServerPlayer players, ByteBuf data) {
        if (players == null)
            return;
        runTask(() -> ServerPlayNetworking.send(players, AllMusicServer.ID, new FriendlyByteBuf(data)));
    }
}
