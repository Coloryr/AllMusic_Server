package Color_yr.AllMusic.side.bukkit;

import Color_yr.AllMusic.api.ISide;
import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.AllMusicBukkit;
import Color_yr.AllMusic.hudsave.HudSave;
import Color_yr.AllMusic.music.play.sendHud.SaveOBJ;
import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;

public class SideBukkit implements ISide {
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
        Bukkit.broadcastMessage(data);
    }

    @Override
    public void bqt(String data) {
        Bukkit.getScheduler().runTask(AllMusicBukkit.plugin, () -> Bukkit.broadcastMessage(data));
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
    public void sendMessaget(Object obj, String Message) {
        Bukkit.getScheduler().runTask(AllMusicBukkit.plugin, () -> ((CommandSender) obj).sendMessage(Message));
    }

    @Override
    public void sendMessage(Object obj, String Message) {
        CommandSender sender = (CommandSender) obj;
        sender.sendMessage(Message);
    }

    @Override
    public void sendMessageRun(Object obj, String Message, String end, String command) {
        if (AllMusicBukkit.spigotSet) {
            SpigotApi.sendMessageRun(obj, Message + end, command);
        } else {
            if (!Message.isEmpty())
                sendMessage(obj, Message);
        }
    }

    @Override
    public void sendMessageSuggest(Object obj, String Message, String end, String command) {
        if (AllMusicBukkit.spigotSet) {
            SpigotApi.sendMessageSuggest(obj, Message + end, command);
        } else {
            if (!Message.isEmpty())
                sendMessage(obj, Message);
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
        if(AllMusic.getConfig().getAdmin().contains(player))
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

    private void send(Player players, String data, Boolean isplay) {
        if (players == null)
            return;
        try {
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
            buf.writeByte(666);
            buf.writeBytes(bytes);
            runTask(() ->
                    players.sendPluginMessage(AllMusicBukkit.plugin, AllMusic.channel, buf.array()));
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
