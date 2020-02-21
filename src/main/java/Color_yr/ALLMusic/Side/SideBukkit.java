package Color_yr.ALLMusic.Side;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.ALLMusicBukkit;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static Color_yr.ALLMusic.ALLMusicBukkit.ALLMusicP;
import static Color_yr.ALLMusic.Play.PlayMusic.NowPlay;

public class SideBukkit implements ISide {

    @Override
    public void Send(String data, String player, Boolean isplay) {
        Send(Bukkit.getPlayer(player), data, isplay);
    }

    @Override
    public void Send(String data, Boolean isplay) {
        Collection<Player> values = (Collection<Player>) Bukkit.getOnlinePlayers();
        for (Player players : values) {
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
        Collection<Player> values = (Collection<Player>) Bukkit.getOnlinePlayers();
        for (Player players : values) {
            if (!ALLMusic.Config.getNoMusicPlayer().contains(players.getName())) {
                if (NowPlay.contains(players.getName())) {
                    ChatComponentText chat = new ChatComponentText(data);
                    PacketPlayOutChat pack = new PacketPlayOutChat(chat, ChatMessageType.GAME_INFO);
                    CraftPlayer player = (CraftPlayer) players;
                    EntityPlayer entityPlayer = player.getHandle();
                    PlayerConnection playerConnection = entityPlayer.playerConnection;
                    playerConnection.sendPacket(pack);
                }
            }
        }
    }

    @Override
    public void bq(String data) {
        Bukkit.broadcastMessage(data);
    }

    @Override
    public void save() {
        ALLMusicBukkit.save();
    }

    private void Send(Player players, String data, Boolean isplay) {
        try {
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
            buf.writeByte(666);
            buf.writeBytes(bytes);
            players.sendPluginMessage(ALLMusicP, ALLMusic.channel, buf.array());
            if (isplay != null) {
                if (isplay) {
                    NowPlay.add(players.getName());
                } else {
                    NowPlay.remove(players.getName());
                }
            }
        } catch (Exception e) {
            ALLMusic.log.warning("§d[ALLMusic]§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}
