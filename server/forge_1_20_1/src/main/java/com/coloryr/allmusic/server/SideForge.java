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
import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.PacketDistributor;

import java.io.File;
import java.util.Collection;

public class SideForge extends BaseSide {

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
    public boolean checkPermission(Object player, String permission) {
        return checkPermission(player);
    }

    @Override
    public boolean checkPermission(Object player) {
        CommandSourceStack sender = (CommandSourceStack) player;
        return sender.hasPermission(2);
    }

    @Override
    public boolean isPlayer(Object player) {
        CommandSourceStack sender = (CommandSourceStack) player;
        return sender.getEntity() instanceof ServerPlayer;
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
            var pack = new ClientboundSetActionBarTextPacket(AllMusicServer.parse(data));
            player1.connection.send(pack);
        }
    }

    @Override
    public File getFolder() {
        return new File(AllMusicServer.dir);
    }

    @Override
    public void broadcast(Component message) {
        MutableComponent component = AllMusicServer.parse(message);
        for (ServerPlayer player : AllMusicServer.server.getPlayerList().getPlayers()) {
            if (!AllMusic.isSkip(player.getName().getString(), null, false)) {
                player.sendSystemMessage(component);
            }
        }
    }

    @Override
    public void sendMessage(Object obj, Component message) {
        if (obj instanceof CommandSourceStack stack) {
            stack.sendSystemMessage(AllMusicServer.parse(message));
        }
    }

    @Override
    public boolean onMusicPlay(SongInfoObj obj) {
        MusicPlayEvent event = new MusicPlayEvent(obj);
        return MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    public boolean onMusicAdd(Object obj, PlayerAddMusicObj music) {
        CommandSourceStack sender = (CommandSourceStack) obj;
        ServerPlayer serverPlayer = null;
        if (sender.getEntity() instanceof ServerPlayer player) {
            serverPlayer = player;
        }
        MusicAddEvent event = new MusicAddEvent(music, serverPlayer);
        return MinecraftForge.EVENT_BUS.post(event);
    }

    private void send(ServerPlayer players, ByteBuf data) {
        if (players == null)
            return;
        runTask(() -> PacketDistributor.PLAYER.with(() -> players)
                .send(new ClientboundCustomPayloadPacket(AllMusicServer.channel, new FriendlyByteBuf(data))));
    }
}
