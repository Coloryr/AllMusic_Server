package com.coloryr.allmusic.server;

import com.coloryr.allmusic.buffercodec.MusicPacketCodec;
import com.coloryr.allmusic.codec.CommandType;
import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import com.coloryr.allmusic.server.core.objs.music.PlayerAddMusicObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.side.BaseSide;
import com.coloryr.allmusic.server.event.MusicAddEvent;
import com.coloryr.allmusic.server.event.MusicPlayEvent;
import io.netty.buffer.ByteBuf;
import net.kyori.adventure.text.Component;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.Collection;

public class SideForge extends BaseSide {
    @Override
    public void runTask(Runnable run) {
        AllMusicServer.server.addScheduledTask(run);
    }

    @Override
    public void runTask(Runnable run1, int delay) {
        Tasks.add(new TaskItem() {{
            tick = delay;
            run = run1;
        }});
    }

    @Override
    public boolean checkPermission(Object player, String permission) {
        return checkPermission(player);
    }

    @Override
    public boolean checkPermission(Object player) {
        if (player instanceof MinecraftServer) {
            return true;
        }
        if (player instanceof EntityPlayerMP) {
            return ((EntityPlayerMP) player).canUseCommand(2, "music");
        }

        return false;
    }

    @Override
    public boolean isPlayer(Object source) {
        return source instanceof EntityPlayerMP;
    }

    @Override
    public boolean needPlay(boolean islist) {
        for (EntityPlayerMP player : AllMusicServer.server.getPlayerList().getPlayers()) {
            if (!AllMusic.isSkip(player.getName(), null, false, islist)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<?> getPlayers() {
        return AllMusicServer.server.getPlayerList().getPlayers();
    }

    @Override
    public String getPlayerName(Object player) {
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP player1 = (EntityPlayerMP) player;
            return player1.getName();
        }

        return null;
    }

    @Override
    public String getPlayerServer(Object player) {
        return null;
    }

    @Override
    public void send(Object player, CommandType type, String data, int data1) {
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP player1 = (EntityPlayerMP) player;
            send(player1, MusicPacketCodec.pack(type, data, data1));
        }
    }

    @Override
    public Object getPlayer(String player) {
        return AllMusicServer.server.getPlayerList().getPlayerByUsername(player);
    }

    @Override
    public void sendBar(Object player, Component data) {
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP player1 = (EntityPlayerMP) player;
            ITextComponent textComponent = AllMusicServer.parse(data);
            SPacketTitle pack = new SPacketTitle(SPacketTitle.Type.ACTIONBAR, textComponent);
            player1.connection.sendPacket(pack);
        }
    }

    @Override
    public File getFolder() {
        return new File(AllMusicServer.dir);
    }

    @Override
    public void broadcast(Component message) {
        ITextComponent textComponent = AllMusicServer.parse(message);
        for (EntityPlayerMP player : AllMusicServer.server.getPlayerList().getPlayers()) {
            if (!AllMusic.isSkip(player.getName(), null, false)) {
                player.sendMessage(textComponent);
            }
        }
    }

    @Override
    public void sendMessage(Object obj, Component message) {
        if (obj instanceof ICommandSender) {
            ICommandSender sender = (ICommandSender) obj;
            ITextComponent textComponent = AllMusicServer.parse(message);
            sender.sendMessage(textComponent);
        }
    }

    @Override
    public boolean onMusicPlay(SongInfoObj obj) {
        MusicPlayEvent event = new MusicPlayEvent(obj);
        return MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    public boolean onMusicAdd(Object obj, PlayerAddMusicObj music) {
        MusicAddEvent event = new MusicAddEvent(music, (ICommandSender) obj);
        return MinecraftForge.EVENT_BUS.post(event);
    }

    private void send(EntityPlayerMP players, ByteBuf data) {
        if (players == null)
            return;
        FMLProxyPacket packet = new FMLProxyPacket(new PacketBuffer(data), "allmusic:channel");
        packet.setTarget(Side.CLIENT);
        runTask(() -> AllMusicServer.channel.sendTo(packet, players));
    }
}

