package Color_yr.AllMusic.Side.SideBukkit;

import Color_yr.AllMusic.API.ISide;
import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.AllMusicBukkit;
import Color_yr.AllMusic.MusicPlay.SendInfo.SaveOBJ;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;

public class SideBukkit implements ISide {

    @Override
    public void Send(String data, String player, Boolean isplay) {
        Send(Bukkit.getPlayer(player), data, isplay);
    }

    @Override
    public void Send(String data, Boolean isplay) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (!AllMusic.getConfig().getNoMusicPlayer().contains(players.getName())) {
                Send(players, data, isplay);
            }
        }
    }

    @Override
    public int GetAllPlayer() {
        return Bukkit.getOnlinePlayers().size();
    }

    @Override
    public boolean SendLyric(String data) {
        boolean Save = false;
        for (Player players : Bukkit.getOnlinePlayers()) {
            String name = players.getName();
            if (!AllMusic.getConfig().getNoMusicPlayer().contains(players.getName())) {
                if (!AllMusic.containNowPlay(name))
                    continue;
                if (AllMusic.getConfig().getNoMusicPlayer().contains(name))
                    continue;
                SaveOBJ obj = AllMusic.getConfig().getInfoSave(name);
                if (obj == null) {
                    obj = new SaveOBJ();
                    AllMusic.getConfig().setInfoSave(obj, name);
                    Save = true;
                }
                if (!obj.isEnableLyric())
                    continue;
                Send(players, "[Lyric]" + data, null);
            }
        }
        return Save;
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
        int online = GetAllPlayer();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (AllMusic.getConfig().getNoMusicPlayer().contains(player.getName())) {
                online--;
            }
        }
        return online > 0;
    }

    @Override
    public boolean SendInfo(String data) {
        boolean Save = false;
        for (Player player : Bukkit.getOnlinePlayers()) {
            String Name = player.getName();
            if (!AllMusic.containNowPlay(Name))
                continue;
            if (AllMusic.getConfig().getNoMusicPlayer().contains(Name))
                continue;
            SaveOBJ obj = AllMusic.getConfig().getInfoSave(Name);
            if (obj == null) {
                obj = new SaveOBJ();
                AllMusic.getConfig().setInfoSave(obj, Name);
                Save = true;
            }
            if (!obj.isEnableInfo())
                continue;
            Send(player, "[Info]" + data, null);
        }
        return Save;
    }

    @Override
    public boolean SendList(String data) {
        boolean Save = false;
        for (Player player : Bukkit.getOnlinePlayers()) {
            String Name = player.getName();
            if (!AllMusic.containNowPlay(Name))
                continue;
            if (AllMusic.getConfig().getNoMusicPlayer().contains(Name))
                continue;
            SaveOBJ obj = AllMusic.getConfig().getInfoSave(Name);
            if (obj == null) {
                obj = new SaveOBJ();
                AllMusic.getConfig().setInfoSave(obj, Name);
                Save = true;
            }
            if (!obj.isEnableList())
                continue;
            Send(player, "[List]" + data, null);
        }
        return Save;
    }

    @Override
    public void Clear(String player) {
        Send("[clear]", player, null);
    }

    @Override
    public void ClearAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Send(player, "[clear]", null);
        }
    }

    @Override
    public void SendMessaget(Object obj, String Message) {
        Bukkit.getScheduler().runTask(AllMusicBukkit.plugin, () -> ((CommandSender) obj).sendMessage(Message));
    }

    @Override
    public void SendMessage(Object obj, String Message) {
        CommandSender sender = (CommandSender) obj;
        sender.sendMessage(Message);
    }

    @Override
    public void SendMessage(Object obj, String Message, ClickEvent.Action action, String Command) {
        CommandSender sender = (CommandSender) obj;
        TextComponent send = new TextComponent(Message);
        send.setClickEvent(new ClickEvent(action, Command));
        sender.spigot().sendMessage(send);
    }

    @Override
    public void RunTask(Runnable run) {
        Bukkit.getScheduler().runTask(AllMusicBukkit.plugin, run);
    }

    @Override
    public void reload() {
        new AllMusic().init(AllMusicBukkit.plugin.getDataFolder());
    }

    @Override
    public boolean checkPermission(String player, String permission) {
        Player player1 = Bukkit.getPlayer(player);
        if (player1 == null)
            return true;
        return !player1.hasPermission(permission);
    }

    private void Send(Player players, String data, Boolean isplay) {
        if (players == null)
            return;
        try {
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
            buf.writeByte(666);
            buf.writeBytes(bytes);
            players.sendPluginMessage(AllMusicBukkit.plugin, AllMusic.channel, buf.array());
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
