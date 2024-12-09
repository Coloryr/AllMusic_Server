package com.coloryr.allmusic.server.side.folia;

import com.coloryr.allmusic.server.AllMusicFolia;
import com.coloryr.allmusic.server.codec.PacketCodec;
import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.config.SaveObj;
import com.coloryr.allmusic.server.core.objs.enums.ComType;
import com.coloryr.allmusic.server.core.objs.enums.HudType;
import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.side.BaseSide;
import com.coloryr.allmusic.server.core.utils.HudUtils;
import com.coloryr.allmusic.server.side.folia.event.MusicAddEvent;
import com.coloryr.allmusic.server.side.folia.event.MusicPlayEvent;
import com.coloryr.allmusic.server.side.folia.hooks.CitizensNPC;
import com.coloryr.allmusic.server.side.folia.hooks.SpigotApi;
import io.netty.buffer.ByteBuf;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.ServerOperator;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class SideFolia extends BaseSide {
    @Override
    public int getPlayerSize() {
        return Bukkit.getOnlinePlayers().size();
    }

    @Override
    public void sendHudLyric(String data) {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (AllMusic.isSkip(player.getName(), null, true))
                    continue;
                String name = player.getName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.lyric.enable)
                    continue;
                send(player, PacketCodec.pack(ComType.LYRIC, data, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudInfo(String data) {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (AllMusic.isSkip(player.getName(), null, true))
                    continue;
                String name = player.getName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.info.enable)
                    continue;
                send(player, PacketCodec.pack(ComType.INFO, data, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词信息发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudPos(String name) {
        try {
            Player player = Bukkit.getPlayer(name);
            if (player == null)
                return;
            if (AllMusic.isSkip(name, null, false))
                return;
            SaveObj obj = HudUtils.get(name);
            String data = AllMusic.gson.toJson(obj);
            send(player, PacketCodec.pack(ComType.HUD, data, 0));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c界面位置发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHud(String name, HudType pos, String data) {
        try {
            if (pos == HudType.PIC) {
                return;
            }
            Player player = Bukkit.getPlayer(name);
            if (player == null)
                return;

            if (AllMusic.isSkip(name, null, true))
                return;

            switch (pos) {
                case INFO:
                    send(player, PacketCodec.pack(ComType.INFO, data, 0));
                    break;
                case LIST:
                    send(player, PacketCodec.pack(ComType.LIST, data, 0));
                    break;
                case LYRIC:
                    send(player, PacketCodec.pack(ComType.LYRIC, data, 0));
                    break;
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c停止指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudList(String data) {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (AllMusic.isSkip(player.getName(), null, true))
                    continue;
                String name = player.getName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.list.enable)
                    continue;
                send(player, PacketCodec.pack(ComType.LIST, data, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲列表发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudUtilsAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String Name = player.getName();
            try {
                SaveObj obj = HudUtils.get(Name);
                String data = AllMusic.gson.toJson(obj);
                send(player, PacketCodec.pack(ComType.HUD, data, 0));
            } catch (Exception e1) {
                AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void sendBar(String data) {
        if (AllMusicFolia.spigotSet) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                try {
                    if (AllMusic.isSkip(player.getName(), null, true))
                        continue;
                    SpigotApi.sendBar(player, data);
                } catch (Exception e1) {
                    AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
                    e1.printStackTrace();
                }
            }
        } else {
            bq(data);
        }
    }

    @Override
    public void sendMusic(String data) {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (AllMusic.isSkip(player.getName(), null, false))
                    continue;
                send(player, PacketCodec.pack(ComType.PLAY, data, 0));
                AllMusic.addNowPlayPlayer(player.getName());
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    protected void topSendMusic(String player, String data) {
        try {
            Player player1 = Bukkit.getPlayer(player);
            if (player1 == null)
                return;
            if (AllMusic.isSkip(player, null, false))
                return;
            send(player1, PacketCodec.pack(ComType.PLAY, data, 0));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendPic(String data) {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (AllMusic.isSkip(player.getName(), null, true))
                    continue;
                String name = player.getName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.pic.enable)
                    continue;
                send(player, PacketCodec.pack(ComType.IMG, data, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c图片指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendPic(String player, String data) {
        try {
            Player player1 = Bukkit.getPlayer(player);
            if (player1 == null)
                return;
            if (AllMusic.isSkip(player1.getName(), null, true))
                return;
            send(player1, PacketCodec.pack(ComType.IMG, data, 0));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c图片指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendPos(String player, int pos) {
        try {
            Player player1 = Bukkit.getPlayer(player);
            if (player1 == null)
                return;
            if (AllMusic.isSkip(player1.getName(), null, true))
                return;
            send(player1, PacketCodec.pack(ComType.POS, null, pos));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    protected void topSendStop() {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                send(player, PacketCodec.pack(ComType.STOP, null, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c停止指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    protected void topSendStop(String name) {
        try {
            Player player = Bukkit.getPlayer(name);
            if (player == null)
                return;
            send(player, PacketCodec.pack(ComType.STOP, null, 0));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c停止指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void clearHud(String name) {
        try {
            Player player = Bukkit.getPlayer(name);
            if (player == null)
                return;
            send(player, PacketCodec.pack(ComType.CLEAR, null, 0));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void clearHud() {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                send(player, PacketCodec.pack(ComType.CLEAR, null, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void bq(String data) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!AllMusic.isSkip(player.getName(), null, false)) {
                player.sendMessage(data);
            }
        }
    }

    @Override
    public void bqRun(String message, String end, String command) {
        SpigotApi.sendMessageBqRun(message, end, command);
    }

    @Override
    public boolean needPlay() {
        int online = getPlayerSize();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (CitizensNPC.isNPC(player))
                online--;
            else if (AllMusic.isSkip(player.getName(), null, false)) {
                online--;
            }
        }
        return online > 0;
    }

    @Override
    public void sendMessage(Object obj, String message) {
        CommandSender sender = (CommandSender) obj;
        sender.sendMessage(message);
    }

    @Override
    public void sendMessageRun(Object obj, String message, String end, String command) {
        if (AllMusicFolia.spigotSet) {
            SpigotApi.sendMessageRun(obj, message, end, command);
        } else {
            if (!message.isEmpty())
                sendMessage(obj, message);
        }
    }

    @Override
    public void sendMessageSuggest(Object obj, String message, String end, String command) {
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
    public void reload() {
        new AllMusic().init(AllMusicFolia.plugin.getDataFolder());
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
    public void runTask(Runnable run, int delay) {
        Bukkit.getGlobalRegionScheduler().runDelayed(AllMusicFolia.plugin,
                scheduledTask -> run.run(), delay);
    }

    @Override
    public List<String> getPlayerList() {
        return List.of();
    }

    @Override
    public boolean onMusicPlay(SongInfoObj obj) {
        MusicPlayEvent event = new MusicPlayEvent(obj);
        ScheduledTask task = Bukkit.getGlobalRegionScheduler().run(AllMusicFolia.plugin, (scheduledTask) -> {
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancel()) {
                FunCore.addMusic();
            }
        });
        while (task.getExecutionState() == ScheduledTask.ExecutionState.IDLE
                || task.getExecutionState() == ScheduledTask.ExecutionState.RUNNING) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return event.isCancel();
    }

    @Override
    public boolean onMusicAdd(Object obj, MusicObj music) {
        MusicAddEvent event = new MusicAddEvent(music, (CommandSender) obj);
        Bukkit.getPluginManager().callEvent(event);
        final boolean isCancelled = event.isCancelled();
        if (!isCancelled) {
            FunCore.addMusic();
        }
        return isCancelled;
    }

    private void send(Player players, ByteBuf data) {
        if (players == null)
            return;
        try {
            if (AllMusic.isRun) {
                runTask(() ->
                        players.sendPluginMessage(AllMusicFolia.plugin, AllMusic.channel, data.array()));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}
