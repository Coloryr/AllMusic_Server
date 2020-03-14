package Color_yr.ALLMusic.Side;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.ALLMusicBukkit;
import Color_yr.ALLMusic.MusicPlay.PlayMusic;
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
            if (!ALLMusic.Config.getNoMusicPlayer().contains(players.getName())) {
                Send(players, data, isplay);
            }
        }
    }

    @Override
    public int GetAllPlayer() {
        return Bukkit.getOnlinePlayers().size();
    }

    @Override
    public void SendLyric(String data) {

    }

    @Override
    public void bq(String data) {
        Bukkit.broadcastMessage(data);
    }

    @Override
    public void bqt(String data) {
        Bukkit.getScheduler().runTask(ALLMusicBukkit.ALLMusicP, () -> Bukkit.broadcastMessage(data));
    }

    @Override
    public void save() {
        ALLMusicBukkit.save();
    }

    @Override
    public boolean NeedPlay() {
        int online = GetAllPlayer();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (ALLMusic.Config.getNoMusicPlayer().contains(player.getName())) {
                online--;
            }
        }
        return online > 0;
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
        Bukkit.getScheduler().runTask(ALLMusicBukkit.ALLMusicP, run);
    }

    @Override
    public void reload() {
        ALLMusicBukkit.setConfig();
    }

    @Override
    public boolean checkPermission(String player, String permission) {
        Player player1 = Bukkit.getPlayer(player);
        if (player1 == null)
            return false;
        return player1.hasPermission(permission);
    }

    private void Send(Player players, String data, Boolean isplay) {
        try {
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
            buf.writeByte(666);
            buf.writeBytes(bytes);
            players.sendPluginMessage(ALLMusicBukkit.ALLMusicP, ALLMusic.channel, buf.array());
            if (isplay != null) {
                if (isplay) {
                    PlayMusic.NowPlayPlayer.add(players.getName());
                } else {
                    PlayMusic.NowPlayPlayer.remove(players.getName());
                }
            }
        } catch (Exception e) {
            ALLMusic.log.warning("§d[ALLMusic]§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}
