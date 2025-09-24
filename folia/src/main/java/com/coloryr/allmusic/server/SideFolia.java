package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.codec.PacketCodec;
import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.enums.ComType;
import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.side.BaseSide;
import com.coloryr.allmusic.server.event.MusicAddEvent;
import com.coloryr.allmusic.server.event.MusicPlayEvent;
import com.coloryr.allmusic.server.hooks.CitizensNPC;
import com.coloryr.allmusic.server.hooks.SpigotApi;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.ServerOperator;

import java.io.File;
import java.util.Collection;

public class SideFolia extends BaseSide {
    @Override
    public Collection<?> getPlayers() {
        return Bukkit.getOnlinePlayers();
    }

    @Override
    public String getPlayerName(Object player) {
        if (player instanceof Player player1) {
            return player1.getName();
        }
        return null;
    }

    @Override
    public String getPlayerServer(Object player) {
        return null;
    }

    @Override
    public void sendBar(Object player, String data) {
        if (player instanceof Player player1) {
            if (AllMusicFolia.spigotSet) {
                SpigotApi.sendBar(player1, data);
            } else {
                sendMessage(player1, data);
            }
        }
    }

    @Override
    public File getFolder() {
        return AllMusicFolia.plugin.getDataFolder();
    }

    @Override
    public void broadcast(String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!AllMusic.isSkip(player.getName(), null, false)) {
                player.sendMessage(message);
            }
        }
    }

    @Override
    public void broadcastWithRun(String message, String end, String command) {
        if (message == null || message.isEmpty()) {
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            SpigotApi.sendMessageBqRun(player, message, end, command);
        }
    }

    @Override
    public boolean needPlay(boolean islist) {
        int online = AllMusic.side.getPlayers().size();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (CitizensNPC.isNPC(player))
                online--;
            else if (AllMusic.isSkip(player.getName(), null, false, islist)) {
                online--;
            }
        }
        return online > 0;
    }

    @Override
    public void sendMessage(Object obj, String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        CommandSender sender = (CommandSender) obj;
        sender.sendMessage(message);
    }

    @Override
    public void sendMessageRun(Object obj, String message, String end, String command) {
        if (message == null || message.isEmpty()) {
            return;
        }
        if (AllMusicFolia.spigotSet) {
            SpigotApi.sendMessageRun(obj, message, end, command);
        } else {
            if (!message.isEmpty())
                sendMessage(obj, message);
        }
    }

    @Override
    public void sendMessageSuggest(Object obj, String message, String end, String command) {
        if (message == null || message.isEmpty()) {
            return;
        }
        if (AllMusicFolia.spigotSet) {
            SpigotApi.sendMessageSuggest(obj, message, end, command);
        } else {
            if (!message.isEmpty())
                sendMessage(obj, message);
        }
    }

    @Override
    public void runTask(Runnable run) {
        Bukkit.getGlobalRegionScheduler().execute(AllMusicFolia.plugin, run);
    }

    @Override
    public boolean checkPermission(Object player, String permission) {
        if (checkPermission(player)) {
            return true;
        }
        if (player instanceof Permissible permissible) {
            return permissible.hasPermission(permission);
        }
        return false;
    }

    @Override
    public boolean checkPermission(Object player) {
        if (player instanceof ConsoleCommandSender) {
            return true;
        }
        if (player instanceof ServerOperator player1) {
            return player1.isOp();
        }
        return false;
    }

    @Override
    public boolean isPlayer(Object source) {
        return source instanceof Player;
    }

    @Override
    public void runTask(Runnable run, int delay) {
        Bukkit.getGlobalRegionScheduler().runDelayed(AllMusicFolia.plugin,
                scheduledTask -> run.run(), delay);
    }

    @Override
    public boolean onMusicPlay(SongInfoObj obj) {
        MusicPlayEvent event = new MusicPlayEvent(obj);
        Bukkit.getPluginManager().callEvent(event);
        final boolean isCancelled = event.isCancelled();
        if (!isCancelled) {
            FunCore.addMusic();
        }
        return isCancelled;
    }

    @Override
    public boolean onMusicAdd(Object obj, MusicObj music) {
        MusicAddEvent event = new MusicAddEvent(music, (CommandSender) obj);
        Bukkit.getPluginManager().callEvent(event);
        return event.isCancelled();
    }

    @Override
    public void send(Object player, ComType type, String data, int data1) {
        if (AllMusic.isRun && player instanceof Player player1) {
            try {
                runTask(() ->
                        player1.sendPluginMessage(AllMusicFolia.plugin, AllMusic.channel, PacketCodec.pack(type, data, data1).array()));
            } catch (Exception e) {
                AllMusic.log.warning("§c数据发送发生错误");
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object getPlayer(String player) {
        return Bukkit.getPlayer(player);
    }
}
