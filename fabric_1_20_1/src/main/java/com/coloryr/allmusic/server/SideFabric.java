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
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
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
        return source.isExecutedByPlayer();
    }

    @Override
    public boolean checkPermission(Object player, String permission) {
        return checkPermission(player);
    }

    @Override
    public boolean needPlay(boolean islist) {
        for (var player : AllMusicServer.server.getPlayerManager().getPlayerList()) {
            if (!AllMusic.isSkip(player.getName().getString(), null, false, islist)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<?> getPlayers() {
        return AllMusicServer.server.getPlayerManager().getPlayerList();
    }

    @Override
    public String getPlayerName(Object player) {
        if (player instanceof ServerPlayerEntity player1) {
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
        if (player instanceof ServerPlayerEntity player1) {
            send(player1, MusicPacketCodec.pack(type, data, data1));
        }
    }

    @Override
    public Object getPlayer(String player) {
        return AllMusicServer.server.getPlayerManager().getPlayer(player);
    }

    @Override
    public void sendBar(Object player, String data) {
        if (player instanceof ServerPlayerEntity player1) {
            FabricApi.sendBar(player1, data);
        }
    }

    @Override
    public File getFolder() {
        return new File(AllMusicServer.dir);
    }

    @Override
    public void broadcast(String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        for (var player : AllMusicServer.server.getPlayerManager().getPlayerList()) {
            if (!AllMusic.isSkip(player.getName().getString(), null, false)) {
                player.sendMessage(Text.of(message), false);
            }
        }
    }

    @Override
    public void broadcastWithRun(String message, String end, String command) {
        if (message == null || message.isEmpty()) {
            return;
        }
        FabricApi.sendMessageBqRun(message, end, command);
    }

    @Override
    public void sendMessage(Object obj, String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        ServerCommandSource source = (ServerCommandSource) obj;
        source.sendMessage(Text.of(message));
    }

    @Override
    public void sendMessageRun(Object obj, String message, String end, String command) {
        if (message == null || message.isEmpty()) {
            return;
        }
        FabricApi.sendMessageRun((ServerCommandSource) obj, message, end, command);
    }

    @Override
    public void sendMessageSuggest(Object obj, String message, String end, String command) {
        if (message == null || message.isEmpty()) {
            return;
        }
        FabricApi.sendMessageSuggest((ServerCommandSource) obj, message, end, command);
    }

    @Override
    public boolean onMusicPlay(SongInfoObj obj) {
        return MusicPlayEvent.EVENT.invoker().interact(obj) != ActionResult.PASS;
    }

    @Override
    public boolean onMusicAdd(Object obj, PlayerAddMusicObj music) {
        ServerCommandSource source = (ServerCommandSource) obj;
        return MusicAddEvent.EVENT.invoker().interact(source.getPlayer(), music) != ActionResult.PASS;
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
