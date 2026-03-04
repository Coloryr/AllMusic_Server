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
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

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
        ServerCommandSource source = (ServerCommandSource) player;
        return source.hasPermissionLevel(2);
    }

    @Override
    public boolean isPlayer(Object player) {
        ServerCommandSource source = (ServerCommandSource) player;
        return source.getEntity() instanceof ServerPlayerEntity;
    }

    @Override
    public boolean checkPermission(Object player, String permission) {
        return checkPermission(player);
    }

    @Override
    public Collection<?> getPlayers() {
        return AllMusicServer.server.getPlayerManager().getPlayerList();
    }

    @Override
    public String getPlayerName(Object player) {
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity player1 = (ServerPlayerEntity) player;
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
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity player1 = (ServerPlayerEntity) player;
            send(player1, MusicPacketCodec.pack(type, data, data1));
        }
    }

    @Override
    public Object getPlayer(String player) {
        return null;
    }

    @Override
    public void sendBar(Object player, Component data) {
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity player1 = (ServerPlayerEntity) player;
            AllMusicServer.adventure.audience(player1).sendActionBar(data);
        }
    }

    @Override
    public File getFolder() {
        return new File(AllMusicServer.dir);
    }

    @Override
    public boolean needPlay(boolean islist) {
        for (ServerPlayerEntity player : AllMusicServer.server.getPlayerManager().getPlayerList()) {
            if (!AllMusic.isSkip(player.getName().getString(), null, false, islist)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void broadcast(Component message) {
        for (ServerPlayerEntity player : AllMusicServer.server.getPlayerManager().getPlayerList()) {
            AllMusicServer.adventure.audience(player).sendMessage(message);
        }
    }

    @Override
    public void sendMessage(Object obj, Component message) {
        if (obj instanceof ServerCommandSource) {
            ServerCommandSource sender = (ServerCommandSource) obj;
            AllMusicServer.adventure.audience(sender).sendMessage(message);
        }
    }

    @Override
    public boolean onMusicPlay(SongInfoObj obj) {
        return MusicPlayEvent.EVENT.invoker().interact(obj) != ActionResult.PASS;
    }

    @Override
    public boolean onMusicAdd(Object obj, PlayerAddMusicObj music) {
        ServerCommandSource sender = (ServerCommandSource) obj;
        ServerPlayerEntity entity = null;
        if (sender.getEntity() instanceof ServerPlayerEntity) {
            entity = (ServerPlayerEntity) sender.getEntity();
        }
        return MusicAddEvent.EVENT.invoker().interact(entity, music) != ActionResult.PASS;
    }

    private void send(ServerPlayerEntity players, ByteBuf data) {
        if (players == null)
            return;
        try {
            runTask(() -> ServerPlayNetworking.send(players, AllMusicServer.ID, new PacketByteBuf(data)));
        } catch (Exception e) {
            AllMusic.log.warning("§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}
