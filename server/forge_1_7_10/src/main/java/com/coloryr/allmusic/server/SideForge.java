package com.coloryr.allmusic.server;

import com.coloryr.allmusic.buffercodec.MusicPacketCodec;
import com.coloryr.allmusic.codec.CommandType;
import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.music.PlayerAddMusicObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.side.BaseSide;
import com.coloryr.allmusic.server.event.MusicAddEvent;
import com.coloryr.allmusic.server.event.MusicPlayEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.kyori.adventure.text.Component;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.util.Collection;

public class SideForge extends BaseSide {
    @Override
    public void runTask(Runnable run) {
        runTask(run, 0);
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
            return ((EntityPlayerMP) player).canCommandSenderUseCommand(2, "music");
        }

        return false;
    }

    @Override
    public boolean isPlayer(Object source) {
        return source instanceof EntityPlayerMP;
    }

    @Override
    public boolean needPlay(boolean islist) {
        for (Object player1 : AllMusicServer.server.getConfigurationManager().playerEntityList) {
            EntityPlayerMP player = (EntityPlayerMP) player1;
            if (!AllMusic.isSkip(player.getCommandSenderName(), null, false, islist)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<?> getPlayers() {
        return AllMusicServer.server.getConfigurationManager().playerEntityList;
    }

    @Override
    public String getPlayerName(Object player) {
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP player1 = (EntityPlayerMP) player;
            return player1.getCommandSenderName();
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
        return AllMusicServer.server.getConfigurationManager().func_152612_a(player);
    }

    @Override
    public void sendBar(Object player, Component data) {
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP player1 = (EntityPlayerMP) player;
            IChatComponent textComponent = AllMusicServer.parse(data);
            player1.addChatMessage(textComponent);
        }
    }

    @Override
    public File getFolder() {
        return new File(AllMusicServer.dir);
    }

    @Override
    public void broadcast(Component message) {
        IChatComponent textComponent = AllMusicServer.parse(message);
        for (Object player1 : AllMusicServer.server.getConfigurationManager().playerEntityList) {
            EntityPlayerMP player = (EntityPlayerMP) player1;
            if (!AllMusic.isSkip(player.getCommandSenderName(), null, false)) {
                player.addChatMessage(textComponent);
            }
        }
    }

    @Override
    public void sendMessage(Object obj, Component message) {
        if(obj instanceof ICommandSender) {
            IChatComponent textComponent = AllMusicServer.parse(message);
            ICommandSender sender = (ICommandSender) obj;
            sender.addChatMessage(textComponent);
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

