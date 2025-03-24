package com.coloryr.allmusic.server.side.forge;

import com.coloryr.allmusic.server.AllMusicForge;
import com.coloryr.allmusic.server.TaskItem;
import com.coloryr.allmusic.server.Tasks;
import com.coloryr.allmusic.server.codec.PacketCodec;
import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.config.SaveObj;
import com.coloryr.allmusic.server.core.objs.enums.ComType;
import com.coloryr.allmusic.server.core.objs.enums.HudType;
import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.side.BaseSide;
import com.coloryr.allmusic.server.core.utils.HudUtils;
import com.coloryr.allmusic.server.side.forge.event.MusicAddEvent;
import com.coloryr.allmusic.server.side.forge.event.MusicPlayEvent;
import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SCustomPayloadPlayPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.PacketDistributor;

import java.io.File;
import java.util.*;

public class SideForge extends BaseSide {

    @Override
    public void runTask(Runnable run) {
        AllMusicForge.server.execute(run);
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
        for (ServerPlayerEntity player : AllMusicForge.server.getPlayerList().getPlayers()) {
            if (!AllMusic.isSkip(player.getName().getString(), null,false, islist)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<Object> getPlayers() {
        return Collections.singleton(AllMusicForge.server.getPlayerList().getPlayers());
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
    public void send(Object player, ComType type, String data, int data1) {
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity player1 = (ServerPlayerEntity) player;
            send(player1, PacketCodec.pack(type, data, data1));
        }
    }

    @Override
    public Object getPlayer(String player) {
        return AllMusicForge.server.getPlayerList().getPlayerByName(player);
    }

    @Override
    public void sendBar(Object player, String data) {
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity player1 = (ServerPlayerEntity) player;
            ForgeApi.sendBar(player1, data);
        }
    }

    @Override
    public File getFolder() {
        return new File(String.format(Locale.ROOT, "config/%s/", "allmusic"));
    }

    @Override
    public void broadcast(String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        for (ServerPlayerEntity player : AllMusicForge.server.getPlayerList().getPlayers()) {
            if (!AllMusic.isSkip(player.getName().getString(), null, false)) {
                player.sendMessage(new StringTextComponent(message), UUID.randomUUID());
            }
        }
    }

    @Override
    public void broadcastWithRun(String message, String end, String command) {
        if (message == null || message.isEmpty()) {
            return;
        }
        ForgeApi.sendMessageBqRun(message, end, command);
    }

    @Override
    public void sendMessage(Object obj, String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        CommandSource sender = (CommandSource) obj;
        sender.sendSuccess(new StringTextComponent(message), false);
    }

    @Override
    public void sendMessageRun(Object obj, String message, String end, String command) {
        if (message == null || message.isEmpty()) {
            return;
        }
        ForgeApi.sendMessageRun(obj, message, end, command);
    }

    @Override
    public void sendMessageSuggest(Object obj, String message, String end, String command) {
        if (message == null || message.isEmpty()) {
            return;
        }
        ForgeApi.sendMessageSuggest(obj, message, end, command);
    }

    @Override
    public boolean onMusicPlay(SongInfoObj obj) {
        MusicPlayEvent event = new MusicPlayEvent(obj);
        return MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    public boolean onMusicAdd(Object obj, MusicObj music) {
        MusicAddEvent event = new MusicAddEvent(music, (CommandSource) obj);
        return MinecraftForge.EVENT_BUS.post(event);
    }

    private void send(ServerPlayerEntity players, ByteBuf data) {
        if (players == null)
            return;
        try {
            runTask(() -> PacketDistributor.PLAYER.with(() -> players)
                    .send(new SCustomPayloadPlayPacket(AllMusicForge.channel, new PacketBuffer(data))));
        } catch (Exception e) {
            AllMusic.log.warning("§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}
