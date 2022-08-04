package coloryr.allmusic.side.bukkit;

import coloryr.allmusic.AllMusic;
import coloryr.allmusic.AllMusicBukkit;
import coloryr.allmusic.api.ISide;
import coloryr.allmusic.hud.HudSave;
import coloryr.allmusic.hud.obj.SaveOBJ;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class SideBukkit implements ISide {
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

    private boolean isOK(String player, boolean in) {
        if (AllMusic.getConfig().getNoMusicPlayer().contains(player))
            return false;
        return !in || AllMusic.containNowPlay(player);
    }

    @Override
    public void send(String data, String player, Boolean isplay) {
        send(Bukkit.getPlayer(player), data, isplay);
    }

    @Override
    public void send(String data, Boolean isplay) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!AllMusic.getConfig().getNoMusicPlayer().contains(player.getName())) {
                if (isplay && !isOK(player.getName(), false))
                    continue;
                send(player, data, isplay);
            }
        }
    }

    @Override
    public int getAllPlayer() {
        return Bukkit.getOnlinePlayers().size();
    }

    @Override
    public void sendHudLyric(String data) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String name = player.getName();
            if (!isOK(player.getName(), true))
                continue;
            SaveOBJ obj = HudSave.get(name);
            if (!obj.isEnableLyric())
                continue;
            send(player, "[Lyric]" + data, null);
        }
    }

    @Override
    public void bq(String data) {
        if (AllMusic.getConfig().isMessageLimit()
                && data.length() > AllMusic.getConfig().getMessageLimitSize()) {
            data = data.substring(0, 30) + "...";
        }
        Bukkit.broadcastMessage(data);
    }

    @Override
    public void bqt(String data) {
        if (AllMusic.getConfig().isMessageLimit()
                && data.length() > AllMusic.getConfig().getMessageLimitSize()) {
            data = data.substring(0, 30) + "...";
        }
        String finalData = data;
        Bukkit.getScheduler().runTask(AllMusicBukkit.plugin, () -> Bukkit.broadcastMessage(finalData));
    }

    @Override
    public boolean NeedPlay() {
        int online = getAllPlayer();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (AllMusic.getConfig().getNoMusicPlayer().contains(player.getName())) {
                online--;
            }
        }
        return online > 0;
    }

    @Override
    public void sendHudInfo(String data) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String Name = player.getName();
            if (!isOK(player.getName(), true))
                continue;
            SaveOBJ obj = HudSave.get(Name);
            if (!obj.isEnableInfo())
                continue;
            send(player, "[Info]" + data, null);
        }
    }

    @Override
    public void sendHudList(String data) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String Name = player.getName();
            if (!isOK(player.getName(), true))
                continue;
            SaveOBJ obj = HudSave.get(Name);
            if (!obj.isEnableList())
                continue;
            send(player, "[List]" + data, null);
        }
    }

    @Override
    public void sendHudSaveAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            String Name = player.getName();
            try {
                SaveOBJ obj = HudSave.get(Name);
                String data = new Gson().toJson(obj);
                send(data, Name, null);
            } catch (Exception e1) {
                AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void clearHud(String player) {
        send("[clear]", player, null);
    }

    @Override
    public void clearHudAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            send(player, "[clear]", null);
        }
    }

    @Override
    public void sendMessaget(Object obj, String message) {
        Bukkit.getScheduler().runTask(AllMusicBukkit.plugin, () -> ((CommandSender) obj).sendMessage(message));
    }

    @Override
    public void sendMessage(Object obj, String message) {
        CommandSender sender = (CommandSender) obj;
        sender.sendMessage(message);
    }

    @Override
    public void sendMessageRun(Object obj, String message, String end, String command) {
        if (AllMusicBukkit.spigotSet) {
            SpigotApi.sendMessageRun(obj, message + end, command);
        } else {
            if (!message.isEmpty())
                sendMessage(obj, message);
        }
    }

    @Override
    public void sendMessageSuggest(Object obj, String message, String end, String command) {
        if (AllMusicBukkit.spigotSet) {
            SpigotApi.sendMessageSuggest(obj, message + end, command);
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
        if (AllMusic.getConfig().getAdmin().contains(player))
            return false;
        Player player1 = Bukkit.getPlayer(player);
        if (player1 == null)
            return true;
        return !player1.hasPermission(permission);
    }

    @Override
    public void task(Runnable run, int delay) {
        Bukkit.getScheduler().runTaskLater(AllMusicBukkit.plugin, run, delay);
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

    private void send(Player players, String data, Boolean isplay) {
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
                        players.sendPluginMessage(AllMusicBukkit.plugin, AllMusic.channel, (byte[]) arrayM.invoke(buf));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
            else {
                return;
            }
            if (isplay != null) {
                if (isplay) {
                    AllMusic.addNowPlayPlayer(players.getName());
                } else {
                    AllMusic.removeNowPlayPlayer(players.getName());
                }
            }
        } catch (Exception e) {
            AllMusic.log.warning("§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}
