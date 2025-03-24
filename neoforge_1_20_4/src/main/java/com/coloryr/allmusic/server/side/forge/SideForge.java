package com.coloryr.allmusic.server.side.forge;

import com.coloryr.allmusic.server.AllMusicForge;
import com.coloryr.allmusic.server.TaskItem;
import com.coloryr.allmusic.server.Tasks;
import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.enums.ComType;
import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.side.BaseSide;
import com.coloryr.allmusic.server.side.forge.event.MusicAddEvent;
import com.coloryr.allmusic.server.side.forge.event.MusicPlayEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

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
        CommandSourceStack source = (CommandSourceStack) player;
        return source.hasPermission(2);
    }

    @Override
    public boolean isPlayer(Object player) {
        CommandSourceStack source = (CommandSourceStack) player;
        return source.isPlayer();
    }

    @Override
    public boolean needPlay(boolean islist) {
        for (ServerPlayer player : AllMusicForge.server.getPlayerList().getPlayers()) {
            if (!AllMusic.isSkip(player.getName().getString(), null, false, islist)) {
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
    public void send(Object player, ComType type, String data, int data1) {
        if (player instanceof ServerPlayer player1) {
            send(player1, new PackData(type, data, data1));
        }
    }

    @Override
    public Object getPlayer(String player) {
        return AllMusicForge.server.getPlayerList().getPlayerByName(player);
    }

    @Override
    public void sendBar(Object player, String data) {
        if (player instanceof ServerPlayer player1) {
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
        for (ServerPlayer player : AllMusicForge.server.getPlayerList().getPlayers()) {
            if (!AllMusic.isSkip(player.getName().getString(), null, false)) {
                player.sendSystemMessage(Component.literal(message));
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
        CommandSourceStack source = (CommandSourceStack) obj;
        source.sendSystemMessage(Component.literal(message));
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
        NeoForge.EVENT_BUS.post(event);
        return event.isCancel();
    }

    @Override
    public boolean onMusicAdd(Object obj, MusicObj music) {
        CommandSourceStack source = (CommandSourceStack) obj;
        MusicAddEvent event = new MusicAddEvent(music, source.getPlayer());
        NeoForge.EVENT_BUS.post(event);
        return event.isCancel();
    }

    private void send(ServerPlayer players, PackData data) {
        if (players == null)
            return;
        try {
            runTask(() -> PacketDistributor.PLAYER.with(players).send(data));
        } catch (Exception e) {
            AllMusic.log.warning("§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}
