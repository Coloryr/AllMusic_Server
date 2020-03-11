package Color_yr.ALLMusic.Side;

import Color_yr.ALLMusic.ALLMusic;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import static Color_yr.ALLMusic.MusicPlay.PlayMusic.NowPlayPlayer;

public class SideBukkit1_15 extends SideBukkit {
    @Override
    public void SendLyric(String data) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (!ALLMusic.Config.getNoMusicPlayer().contains(players.getName())) {
                if (NowPlayPlayer.contains(players.getName())) {
                    if (ALLMusic.VV == null || !ALLMusic.Config.getVVSave(players.getName()).isEnable()) {
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
}
