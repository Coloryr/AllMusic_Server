package coloryr.allmusic.side.folia;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.AllMusicFolia;
import coloryr.allmusic.core.hud.HudUtils;
import coloryr.allmusic.core.objs.config.SaveObj;
import coloryr.allmusic.core.objs.music.MusicObj;
import coloryr.allmusic.core.objs.music.SongInfoObj;
import coloryr.allmusic.core.side.ComType;
import coloryr.allmusic.core.side.ISide;
import coloryr.allmusic.side.folia.event.MusicAddEvent;
import coloryr.allmusic.side.folia.event.MusicPlayEvent;
import coloryr.allmusic.side.folia.hooks.CitizensNPC;
import coloryr.allmusic.side.folia.hooks.SpigotApi;
import com.google.gson.Gson;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class SideFolia extends ISide {
    private static Class ByteBufC;
    private static Class UnpooledC;
    private static Method bufferM;
    private static Method writeByteM;
    private static Method writeBytesM;
    private static Method arrayM;

    static {
        try {
            ByteBufC = Class.forName("net.minecraft.util.io.netty.buffer.ByteBuf");
        } catch (Exception e) {
            try {
                ByteBufC = Class.forName("io.netty.buffer.ByteBuf");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        if (ByteBufC != null) {
            try {
                arrayM = ByteBufC.getMethod("array");
                writeByteM = ByteBufC.getMethod("writeByte", int.class);
                writeBytesM = ByteBufC.getMethod("writeBytes", byte[].class);
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }
        }
        try {
            UnpooledC = Class.forName("net.minecraft.util.io.netty.buffer.Unpooled");
        } catch (Exception e) {
            try {
                UnpooledC = Class.forName("io.netty.buffer.Unpooled");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        if (UnpooledC != null) {
            try {
                bufferM = UnpooledC.getMethod("buffer", int.class);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    @Override
    public void send(String data, String player) {
        send(Bukkit.getPlayer(player), data);
    }

    @Override
    public int getAllPlayer() {
        return Bukkit.getOnlinePlayers().size();
    }

    @Override
    public void sendHudLyric(String data) {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (AllMusic.isOK(player.getName(), null, true))
                    continue;
                String name = player.getName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.EnableLyric)
                    continue;
                send(player, ComType.lyric + data);
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
                if (AllMusic.isOK(player.getName(), null, true))
                    continue;
                String name = player.getName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.EnableInfo)
                    continue;
                send(player, ComType.info + data);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词信息发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudList(String data) {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (AllMusic.isOK(player.getName(), null, true))
                    continue;
                String name = player.getName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.EnableList)
                    continue;
                send(player, ComType.list + data);
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
                String data = new Gson().toJson(obj);
                send(player, data);
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
                    if (AllMusic.isOK(player.getName(), null, true))
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
    public void sendMusic(String url) {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (AllMusic.isOK(player.getName(), null, false))
                    continue;
                send(player, ComType.play + url);
                AllMusic.addNowPlayPlayer(player.getName());
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    protected void topSendMusic(String player, String url) {
        try {
            Player player1 = Bukkit.getPlayer(player);
            if (player1 == null)
                return;
            if (AllMusic.isOK(player, null, false))
                return;
            send(ComType.play + url, player);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendPic(String url) {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (AllMusic.isOK(player.getName(), null, true))
                    continue;
                String name = player.getName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.EnablePic)
                    continue;
                send(player, ComType.img + url);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c图片指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendPic(String player, String url) {
        try {
            Player player1 = Bukkit.getPlayer(player);
            if (player1 == null)
                return;
            if (AllMusic.isOK(player1.getName(), null, true))
                return;
            send(ComType.img + url, player);
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
            if (AllMusic.isOK(player1.getName(), null, true))
                return;
            send(ComType.pos + pos, player);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    protected void topSendStop() {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                send(player, ComType.stop);
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
            send(player, ComType.stop);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c停止指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void clearHud(String player) {
        try {
            send(ComType.clear, player);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void clearHud() {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                send(player, ComType.clear);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void bq(String data) {
        if (AllMusic.getConfig().MessageLimit
                && data.length() > AllMusic.getConfig().MessageLimitSize) {
            data = data.substring(0, AllMusic.getConfig().MessageLimitSize - 1) + "...";
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!AllMusic.getConfig().NoMusicPlayer.contains(player.getName())) {
                player.sendMessage(data);
            }
        }
    }

    @Override
    public void bqt(String data) {
        if (AllMusic.getConfig().MessageLimit
                && data.length() > AllMusic.getConfig().MessageLimitSize) {
            data = data.substring(0, AllMusic.getConfig().MessageLimitSize - 1) + "...";
        }
        String finalData = data;
        Bukkit.getGlobalRegionScheduler().execute(AllMusicFolia.plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!AllMusic.getConfig().NoMusicPlayer.contains(player.getName())) {
                    player.sendMessage(finalData);
                }
            }
        });
    }

    @Override
    public boolean needPlay() {
        int online = getAllPlayer();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (CitizensNPC.isNPC(player))
                online--;
            else if (AllMusic.getConfig().NoMusicPlayer.contains(player.getName())) {
                online--;
            }
        }
        return online > 0;
    }

    @Override
    public void sendMessaget(Object obj, String message) {
        Bukkit.getGlobalRegionScheduler().execute(AllMusicFolia.plugin, () -> ((CommandSender) obj).sendMessage(message));
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
    public boolean checkPermission(String player, String permission) {
        if (AllMusic.getConfig().Admin.contains(player))
            return false;
        Player player1 = Bukkit.getPlayer(player);
        if (player1 == null)
            return true;
        return !player1.hasPermission(permission);
    }

    @Override
    public void runTask(Runnable run, int delay) {
        Bukkit.getGlobalRegionScheduler().runDelayed(AllMusicFolia.plugin,
                scheduledTask -> run.run(), delay);
    }

    @Override
    public void updateInfo() {

    }

    @Override
    public void updateLyric() {

    }

    @Override
    public void ping() {

    }

    @Override
    public boolean onMusicPlay(SongInfoObj obj) {
        MusicPlayEvent event = new MusicPlayEvent(obj);
        var task = Bukkit.getGlobalRegionScheduler().run(AllMusicFolia.plugin, (scheduledTask) -> {
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancel()) {
                FunCore.addMusic();
            }
        });
        while (task.getExecutionState() == ScheduledTask.ExecutionState.IDLE || task.getExecutionState() == ScheduledTask.ExecutionState.RUNNING) {
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
        var task = Bukkit.getGlobalRegionScheduler().run(AllMusicFolia.plugin, (scheduledTask) -> {
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancel()) {
                FunCore.addMusic();
            }
        });
        while (task.getExecutionState() == ScheduledTask.ExecutionState.IDLE || task.getExecutionState() == ScheduledTask.ExecutionState.RUNNING) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return event.isCancel();
    }

    private void send(Player players, String data) {
        if (players == null)
            return;
        try {
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            Object buf = bufferM.invoke(null, bytes.length + 1);
            writeByteM.invoke(buf, 666);
            writeBytesM.invoke(buf, bytes);

            if (AllMusic.isRun)
                runTask(() ->
                {
                    try {
                        players.sendPluginMessage(AllMusicFolia.plugin, AllMusic.channel, (byte[]) arrayM.invoke(buf));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
            else {
            }
        } catch (Exception e) {
            AllMusic.log.warning("§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}
