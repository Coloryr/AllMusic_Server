package com.coloryr.allmusic.server.side.bukkit;

import com.coloryr.allmusic.server.AllMusicBukkit;
import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.side.BaseSide;
import com.coloryr.allmusic.server.side.bukkit.event.MusicAddEvent;
import com.coloryr.allmusic.server.side.bukkit.event.MusicPlayEvent;
import com.coloryr.allmusic.server.side.bukkit.hooks.CitizensNPC;
import com.coloryr.allmusic.server.side.bukkit.hooks.SpigotApi;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.ServerOperator;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

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
    public void broadcast(String data) {
        if (data == null || data.isEmpty())
            return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!AllMusic.isSkip(player.getName(), null, false)) {
                player.sendMessage(data);
            }
        }
    }

    @Override
    public void broadcastWithRun(String message, String end, String command) {
        if (message == null || message.isEmpty())
            return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (AllMusic.isSkip(player.getName(), null, true))
                continue;
            SpigotApi.sendMessageBqRun(player, message, end, command);
        }
    }

    @Override
    public boolean needPlay(boolean islist) {
        int online = Bukkit.getOnlinePlayers().size();
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
    public Collection<?> getPlayers() {
        return Bukkit.getOnlinePlayers();
    }

    @Override
    public String getPlayerName(Object player) {
        if (player instanceof Player) {
            Player player1 = (Player) player;
            return player1.getName();
        }

        return null;
    }

    @Override
    public String getPlayerServer(Object player) {
        return null;
    }

    @Override
    public void send(Object player, ComType type, String data, int data1) {
        if (player instanceof Player) {
            Player player1 = (Player) player;
            try {
                Object obj1 = pack(type, data, data1);
                byte[] temp = (byte[]) arrayM.invoke(obj1);
                send(player1, temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object getPlayer(String player) {
        return Bukkit.getPlayer(player);
    }

    @Override
    public void sendBar(Object player, String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        if (player instanceof Player) {
            Player player1 = (Player) player;
            if (AllMusicBukkit.spigotSet) {
                SpigotApi.sendBar(player1, message);
            } else {
                player1.sendMessage(message);
            }
        }
    }

    @Override
    public File getFolder() {
        return AllMusicBukkit.plugin.getDataFolder();
    }

    @Override
    public void sendMessage(Object obj, String message) {
        if (message == null || message.isEmpty())
            return;
        CommandSender sender = (CommandSender) obj;
        sender.sendMessage(message);
    }

    @Override
    public void sendMessageRun(Object obj, String message, String end, String command) {
        if (message == null || message.isEmpty())
            return;
        if (AllMusicBukkit.spigotSet) {
            SpigotApi.sendMessageRun(obj, message, end, command);
        } else {
            sendMessage(obj, message);
        }
    }

    @Override
    public void sendMessageSuggest(Object obj, String message, String end, String command) {
        if (message == null || message.isEmpty())
            return;
        if (AllMusicBukkit.spigotSet) {
            SpigotApi.sendMessageSuggest(obj, message, end, command);
        } else {
            sendMessage(obj, message);
        }
    }

    @Override
    public void runTask(Runnable run) {
        Bukkit.getScheduler().runTask(AllMusicBukkit.plugin, run);
    }

    @Override
    public boolean checkPermission(Object player, String permission) {
        if (checkPermission(player)) {
            return true;
        }
        if (player instanceof Permissible) {
            Permissible player1 = (Permissible) player;
            return player1.hasPermission(permission);
        }

        return false;
    }

    @Override
    public boolean checkPermission(Object player) {
        if (player instanceof ConsoleCommandSender) {
            return true;
        }
        if (player instanceof ServerOperator) {
            ServerOperator sender = (ServerOperator) player;
            return sender.isOp();
        }

        return false;
    }

    @Override
    public boolean isPlayer(Object source) {
        return source instanceof Player;
    }

    @Override
    public void runTask(Runnable run, int delay) {
        Bukkit.getScheduler().runTaskLater(AllMusicBukkit.plugin, run, delay);
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
