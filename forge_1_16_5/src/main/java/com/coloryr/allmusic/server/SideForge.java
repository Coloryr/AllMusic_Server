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
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SCustomPayloadPlayPacket;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.PacketDistributor;

import java.io.File;
import java.util.*;

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
        CommandSource source = (CommandSource) player;
        return source.hasPermission(2);
    }

    @Override
    public boolean isPlayer(Object player) {
        CommandSource source = (CommandSource) player;
        return source.getEntity() instanceof PlayerEntity;
    }

    @Override
    public boolean needPlay(boolean islist) {
        for (ServerPlayerEntity player : AllMusicServer.server.getPlayerList().getPlayers()) {
            if (!AllMusic.isSkip(player.getName().getString(), null,false, islist)) {
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
        return AllMusicServer.server.getPlayerList().getPlayerByName(player);
    }

    @Override
    public void sendBar(Object player, Component data) {
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity player1 = (ServerPlayerEntity) player;
            ITextComponent textComponent = AllMusicServer.parse(data);
            STitlePacket pack = new STitlePacket(STitlePacket.Type.ACTIONBAR, textComponent);
            player1.connection.send(pack);
        }
    }

    @Override
    public File getFolder() {
        return new File(AllMusicServer.dir);
    }

    @Override
    public void broadcast(Component message) {
        ITextComponent textComponent = AllMusicServer.parse(message);
        for (ServerPlayerEntity player : AllMusicServer.server.getPlayerList().getPlayers()) {
            if (!AllMusic.isSkip(player.getName().getString(), null, false)) {
                player.sendMessage(textComponent, UUID.randomUUID());
            }
        }
    }

    @Override
    public void sendMessage(Object obj, Component message) {
        if (obj instanceof CommandSource) {
            CommandSource sender = (CommandSource) obj;
            ITextComponent textComponent = AllMusicServer.parse(message);
            sender.sendSuccess(textComponent, false);
        }
    }


    @Override
    public boolean onMusicPlay(SongInfoObj obj) {
        MusicPlayEvent event = new MusicPlayEvent(obj);
        return MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    public boolean onMusicAdd(Object obj, PlayerAddMusicObj music) {
        MusicAddEvent event = new MusicAddEvent(music, (CommandSource) obj);
        return MinecraftForge.EVENT_BUS.post(event);
    }

    private void send(ServerPlayerEntity players, ByteBuf data) {
        if (players == null)
            return;
        runTask(() -> PacketDistributor.PLAYER.with(() -> players)
                .send(new SCustomPayloadPlayPacket(AllMusicServer.channel, new PacketBuffer(data))));
    }
}
