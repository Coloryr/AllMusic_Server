package Color_yr.ALLMusic.Side;

import Color_yr.ALLMusic.ALLMusic;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

import static Color_yr.ALLMusic.Play.PlayMusic.NowPlay;

public class SideBukkit1_14 extends SideBukkit {
    @Override
    public void SendLyric(String data)
    {
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
}
