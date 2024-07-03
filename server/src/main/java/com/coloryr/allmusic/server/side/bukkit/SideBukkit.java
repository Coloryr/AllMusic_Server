package com.coloryr.allmusic.server.side.bukkit;

import com.coloryr.allmusic.server.AllMusicBukkit;
import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.config.SaveObj;
import com.coloryr.allmusic.server.core.objs.enums.ComType;
import com.coloryr.allmusic.server.core.objs.enums.HudType;
import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.side.BaseSide;
import com.coloryr.allmusic.server.core.utils.HudUtils;
import com.coloryr.allmusic.server.side.bukkit.event.MusicAddEvent;
import com.coloryr.allmusic.server.side.bukkit.event.MusicPlayEvent;
import com.coloryr.allmusic.server.side.bukkit.hooks.CitizensNPC;
import com.coloryr.allmusic.server.side.bukkit.hooks.SpigotApi;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class SideBukkit extends BaseSide {
    private static Class ByteBufC;
    private static Class UnpooledC;
    private static Method bufferM;
    private static Method writeByteM;
    private static Method writeIntM;
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
                writeIntM = ByteBufC.getMethod("writeInt", int.class);
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
    public int getPlayerSize() {
        return Bukkit.getOnlinePlayers().size();
    }

    @Override
    public void sendHudLyric(String data) {
        try {
            Object obj1 = pack(ComType.LYRIC, data, 0);
            byte[] temp = (byte[]) arrayM.invoke(obj1);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (AllMusic.isSkip(player.getName(), null, true))
                    continue;
                String name = player.getName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.lyric.enable)
                    continue;
                send(player, temp);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudInfo(String data) {
        try {
            Object obj1 = pack(ComType.INFO, data, 0);
            byte[] temp = (byte[]) arrayM.invoke(obj1);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (AllMusic.isSkip(player.getName(), null, true))
                    continue;
                String name = player.getName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.info.enable)
                    continue;
                send(player, temp);
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
            Object obj1 = pack(ComType.HUD, data, 0);
            byte[] temp = (byte[]) arrayM.invoke(obj1);
            send(player, temp);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲指令发送出错");
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
            Object obj1 = null;
            switch (pos) {
                case INFO:
                    obj1 = pack(ComType.INFO, data, 0);
                    break;
                case LIST:
                    obj1 = pack(ComType.LIST, data, 0);
                    break;
                case LYRIC:
                    obj1 = pack(ComType.LYRIC, data, 0);
                    break;
            }
            if (obj1 == null) {
                return;
            }

            byte[] temp = (byte[]) arrayM.invoke(obj1);
            send(player, temp);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c停止指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudList(String data) {
        try {
            Object obj1 = pack(ComType.LIST, data, 0);
            byte[] temp = (byte[]) arrayM.invoke(obj1);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (AllMusic.isSkip(player.getName(), null, true))
                    continue;
                String name = player.getName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.list.enable)
                    continue;
                send(player, temp);
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
                Object obj1 = pack(ComType.HUD, data, 0);
                byte[] temp = (byte[]) arrayM.invoke(obj1);
                send(player, temp);
            } catch (Exception e1) {
                AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void sendBar(String data) {
        if (AllMusicBukkit.spigotSet) {
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
            Object obj1 = pack(ComType.PLAY, data, 0);
            byte[] temp = (byte[]) arrayM.invoke(obj1);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (AllMusic.isSkip(player.getName(), null, false)) {
                    continue;
                }
                send(player, temp);
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
            if (AllMusic.isSkip(player, null, false)) {
                return;
            }
            Object obj1 = pack(ComType.PLAY, data, 0);
            byte[] temp = (byte[]) arrayM.invoke(obj1);
            send(player1, temp);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendPic(String data) {
        try {
            Object obj1 = pack(ComType.IMG, data, 0);
            byte[] temp = (byte[]) arrayM.invoke(obj1);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (AllMusic.isSkip(player.getName(), null, true))
                    continue;
                String name = player.getName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.pic.enable)
                    continue;
                send(player, temp);
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
            Object obj1 = pack(ComType.IMG, data, 0);
            byte[] temp = (byte[]) arrayM.invoke(obj1);
            send(player1, temp);
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
            Object obj1 = pack(ComType.POS, null, pos);
            byte[] temp = (byte[]) arrayM.invoke(obj1);
            send(player1, temp);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    protected void topSendStop() {
        try {
            Object obj1 = pack(ComType.STOP, null, 0);
            byte[] temp = (byte[]) arrayM.invoke(obj1);
            for (Player player : Bukkit.getOnlinePlayers()) {
                send(player, temp);
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
            Object obj1 = pack(ComType.STOP, null, 0);
            byte[] temp = (byte[]) arrayM.invoke(obj1);
            send(player, temp);
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
            Object obj1 = pack(ComType.CLEAR, null, 0);
            byte[] temp = (byte[]) arrayM.invoke(obj1);
            send(player, temp);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void clearHud() {
        try {
            Object obj1 = pack(ComType.CLEAR, null, 0);
            byte[] temp = (byte[]) arrayM.invoke(obj1);
            for (Player player : Bukkit.getOnlinePlayers()) {
                send(player, temp);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void topBq(String data) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!AllMusic.isSkip(player.getName(), null, true)) {
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
        if (AllMusicBukkit.spigotSet) {
            SpigotApi.sendMessageRun(obj, message, end, command);
        } else {
            if (!message.isEmpty())
                sendMessage(obj, message);
        }
    }

    @Override
    public void sendMessageSuggest(Object obj, String message, String end, String command) {
        if (AllMusicBukkit.spigotSet) {
            SpigotApi.sendMessageSuggest(obj, message, end, command);
        } else {
            if (!message.isEmpty())
                sendMessage(obj, message);
        }
    }

    @Override
    public void runTask(Runnable run) {
        Bukkit.getScheduler().runTask(AllMusicBukkit.plugin, run);
    }

    @Override
    public void reload() {
        new AllMusic().init(AllMusicBukkit.plugin.getDataFolder());
    }

    @Override
    public boolean checkPermission(String player, String permission) {
        for (String item : AllMusic.getConfig().adminList) {
            if (item.equalsIgnoreCase(player)) {
                return true;
            }
        }
        Player player1 = Bukkit.getPlayer(player);
        if (player1 == null)
            return false;
        return player1.hasPermission(permission);
    }

    @Override
    public boolean checkPermission(String player) {
        for (String item : AllMusic.getConfig().adminList) {
            if (item.equalsIgnoreCase(player)) {
                return true;
            }
        }
        Player player1 = Bukkit.getPlayer(player);
        if (player1 == null)
            return false;
        return player1.isOp();
    }

    @Override
    public void runTask(Runnable run, int delay) {
        Bukkit.getScheduler().runTaskLater(AllMusicBukkit.plugin, run, delay);
    }

    @Override
    public List<String> getPlayerList() {
        return Collections.emptyList();
    }

    @Override
    public boolean onMusicPlay(SongInfoObj obj) {
        MusicPlayEvent event = new MusicPlayEvent(obj);
        Bukkit.getScheduler().callSyncMethod(AllMusicBukkit.plugin, () -> {
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancel()) {
                FunCore.addMusic();
            }
            return event;
        });
        return event.isCancel();
    }

    @Override
    public boolean onMusicAdd(Object obj, MusicObj music) {
        MusicAddEvent event = new MusicAddEvent(music, (CommandSender) obj);
        Bukkit.getScheduler().callSyncMethod(AllMusicBukkit.plugin, () -> {
            Bukkit.getPluginManager().callEvent(event);
            return event;
        });
        return event.isCancel();
    }

    private void send(Player players, byte[] data) {
        if (players == null)
            return;

        if (AllMusic.isRun) {
            runTask(() -> players.sendPluginMessage(AllMusicBukkit.plugin, AllMusic.channel, data));
        }
    }

    private void writeString(Object buf, String data) throws InvocationTargetException, IllegalAccessException {
        byte[] temp = data.getBytes(StandardCharsets.UTF_8);
        writeIntM.invoke(buf, temp.length);
        writeBytesM.invoke(buf, temp);
    }

    private Object pack(ComType type, String data, int data1) throws InvocationTargetException, IllegalAccessException {
        Object buf = bufferM.invoke(null, 0);
        writeByteM.invoke(buf, type.ordinal());
        switch (type) {
            case PLAY:
            case IMG:
            case INFO:
            case LIST:
            case LYRIC:
            case HUD:
                writeString(buf, data);
                break;
            case POS:
                writeIntM.invoke(buf, data1);
                break;
        }
        return buf;
    }
}
