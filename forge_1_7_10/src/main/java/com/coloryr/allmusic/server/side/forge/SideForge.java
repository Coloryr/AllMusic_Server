package com.coloryr.allmusic.server.side.forge;

import com.coloryr.allmusic.server.AllMusicForge;
import com.coloryr.allmusic.server.TaskItem;
import com.coloryr.allmusic.server.Tasks;
import com.coloryr.allmusic.server.codec.PacketCodec;
import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.config.SaveObj;
import com.coloryr.allmusic.server.core.objs.enums.ComType;
import com.coloryr.allmusic.server.core.objs.enums.HudType;
import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.side.BaseSide;
import com.coloryr.allmusic.server.core.utils.HudUtils;
import com.coloryr.allmusic.server.side.forge.event.MusicAddEvent;
import com.coloryr.allmusic.server.side.forge.event.MusicPlayEvent;
import com.google.gson.Gson;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SideForge extends BaseSide {

    @Override
    public void reload() {
        String path = String.format(Locale.ROOT, "config/%s/", "AllMusic");
        new AllMusic().init(new File(path));
    }

    @Override
    public int getPlayerSize() {
        return AllMusicForge.server.getCurrentPlayerCount();
    }

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
    public boolean needPlay() {
        for (Object player1 : AllMusicForge.server.getConfigurationManager().playerEntityList) {
            EntityPlayerMP player = (EntityPlayerMP) player1;
            if (!AllMusic.isSkip(player.getCommandSenderName(), null, false)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void sideSendStop() {
        try {
            for (Object player1 : AllMusicForge.server.getConfigurationManager().playerEntityList) {
                EntityPlayerMP player = (EntityPlayerMP) player1;
                send(player, PacketCodec.pack(ComType.STOP, null, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c停止指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    protected void sideSendStop(String name) {
        try {
            EntityPlayerMP player = AllMusicForge.server.getConfigurationManager().func_152612_a(name);
            if (player == null)
                return;
            send(player, PacketCodec.pack(ComType.STOP, null, 0));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c停止指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendMusic(String data) {
        try {
            for (Object player1 : AllMusicForge.server.getConfigurationManager().playerEntityList) {
                EntityPlayerMP player = (EntityPlayerMP) player1;
                if (AllMusic.isSkip(player.getCommandSenderName(), null, false))
                    continue;
                send(player, PacketCodec.pack(ComType.PLAY, data, 0));
                AllMusic.addNowPlayPlayer(player.getCommandSenderName());
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    protected void sideSendMusic(String player, String data) {
        try {
            EntityPlayerMP player1 = AllMusicForge.server.getConfigurationManager().func_152612_a(player);
            if (player1 == null)
                return;
            if (AllMusic.isSkip(player, null, false))
                return;
            send(player1, PacketCodec.pack(ComType.PLAY, data, 0));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendPic(String data) {
        try {
            for (Object player1 : AllMusicForge.server.getConfigurationManager().playerEntityList) {
                EntityPlayerMP player = (EntityPlayerMP) player1;
                if (AllMusic.isSkip(player.getCommandSenderName(), null, true))
                    continue;
                String name = player.getCommandSenderName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.pic.enable)
                    continue;
                send(player, PacketCodec.pack(ComType.IMG, data, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c图片指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendPic(String player, String data) {
        try {
            EntityPlayerMP player1 = AllMusicForge.server.getConfigurationManager().func_152612_a(player);
            if (player1 == null)
                return;
            if (AllMusic.isSkip(player1.getCommandSenderName(), null, true))
                return;
            send(player1, PacketCodec.pack(ComType.IMG, data, 0));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c图片指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendPos(String player, int pos) {
        try {
            EntityPlayerMP player1 = AllMusicForge.server.getConfigurationManager().func_152612_a(player);
            if (player1 == null)
                return;
            if (AllMusic.isSkip(player1.getCommandSenderName(), null, true))
                return;
            send(player1, PacketCodec.pack(ComType.POS, null, pos));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudLyric(String data) {
        try {
            for (Object player1 : AllMusicForge.server.getConfigurationManager().playerEntityList) {
                EntityPlayerMP player = (EntityPlayerMP) player1;
                if (AllMusic.isSkip(player.getCommandSenderName(), null, true))
                    continue;
                String name = player.getCommandSenderName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.lyric.enable)
                    continue;
                send(player, PacketCodec.pack(ComType.LYRIC, data, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudInfo(String data) {
        try {
            for (Object player1 : AllMusicForge.server.getConfigurationManager().playerEntityList) {
                EntityPlayerMP player = (EntityPlayerMP) player1;
                if (AllMusic.isSkip(player.getCommandSenderName(), null, true))
                    continue;
                String name = player.getCommandSenderName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.info.enable)
                    continue;
                send(player, PacketCodec.pack(ComType.INFO, data, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词信息发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudPos(String name) {
        try {
            EntityPlayerMP player = AllMusicForge.server.getConfigurationManager().func_152612_a(name);
            if (player == null)
                return;
            SaveObj obj = HudUtils.get(name);
            String data = AllMusic.gson.toJson(obj);
            send(player, PacketCodec.pack(ComType.HUD, data, 0));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c界面位置发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHud(String name, HudType pos, String data) {
        try {
            if (pos == HudType.PIC) {
                return;
            }
            EntityPlayerMP player = AllMusicForge.server.getConfigurationManager().func_152612_a(name);
            if (player == null)
                return;
            if (AllMusic.isSkip(name, null, true))
                return;
            switch (pos) {
                case INFO:
                    send(player, PacketCodec.pack(ComType.INFO, data, 0));
                    break;
                case LIST:
                    send(player, PacketCodec.pack(ComType.LIST, data, 0));
                    break;
                case LYRIC:
                    send(player, PacketCodec.pack(ComType.LYRIC, data, 0));
                    break;
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c停止指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudList(String data) {
        try {
            for (Object player1 : AllMusicForge.server.getConfigurationManager().playerEntityList) {
                EntityPlayerMP player = (EntityPlayerMP) player1;
                if (AllMusic.isSkip(player.getCommandSenderName(), null, true))
                    continue;
                String name = player.getCommandSenderName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.list.enable)
                    continue;
                send(player, PacketCodec.pack(ComType.LIST, data, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲列表发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudUtilsAll() {
        for (Object player1 : AllMusicForge.server.getConfigurationManager().playerEntityList) {
            EntityPlayerMP player = (EntityPlayerMP) player1;
            String Name = player.getCommandSenderName();
            try {
                SaveObj obj = HudUtils.get(Name);
                String data = new Gson().toJson(obj);
                send(player, PacketCodec.pack(ComType.HUD, data, 0));
            } catch (Exception e1) {
                AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void sendBar(String data) {
        for (Object player1 : AllMusicForge.server.getConfigurationManager().playerEntityList) {
            EntityPlayerMP player = (EntityPlayerMP) player1;
            try {
                if (AllMusic.isSkip(player.getCommandSenderName(), null, true))
                    continue;
                ForgeApi.sendBar(player, data);
            } catch (Exception e1) {
                AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void clearHud(String name) {
        try {
            EntityPlayerMP player = AllMusicForge.server.getConfigurationManager().func_152612_a(name);
            if (player == null)
                return;
            send(player, PacketCodec.pack(ComType.CLEAR, null, 0));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void clearHud() {
        try {
            for (Object player1 : AllMusicForge.server.getConfigurationManager().playerEntityList) {
                EntityPlayerMP player = (EntityPlayerMP) player1;
                send(player, PacketCodec.pack(ComType.CLEAR, null, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void broadcast(String data) {
        for (Object player1 : AllMusicForge.server.getConfigurationManager().playerEntityList) {
            EntityPlayerMP player = (EntityPlayerMP) player1;
            if (!AllMusic.isSkip(player.getCommandSenderName(), null, false)) {
                player.addChatMessage(new ChatComponentText(data));
            }
        }
    }

    @Override
    public void broadcastWithRun(String message, String end, String command) {
        ForgeApi.sendMessageBqRun(message, end, command);
    }

    @Override
    public void sendMessage(Object obj, String message) {
        ICommandSender sender = (ICommandSender) obj;
        sender.addChatMessage(new ChatComponentText(message));
    }

    @Override
    public void sendMessageRun(Object obj, String message, String end, String command) {
        ForgeApi.sendMessageRun(obj, message, end, command);
    }

    @Override
    public void sendMessageSuggest(Object obj, String message, String end, String command) {
        ForgeApi.sendMessageSuggest(obj, message, end, command);
    }

    @Override
    public boolean onMusicPlay(SongInfoObj obj) {
        MusicPlayEvent event = new MusicPlayEvent(obj);
        return MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    public boolean onMusicAdd(Object obj, MusicObj music) {
        MusicAddEvent event = new MusicAddEvent(music, (ICommandSender) obj);
        return MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    public List<String> getPlayerList() {
        List<String> list = new ArrayList<>();
        for (Object player1 : AllMusicForge.server.getConfigurationManager().playerEntityList) {
            EntityPlayerMP player = (EntityPlayerMP) player1;
            list.add(player.getCommandSenderName());
        }
        return list;
    }

    private void send(EntityPlayerMP players, ByteBuf data) {
        if (players == null)
            return;
        try {
            FMLProxyPacket packet = new FMLProxyPacket(new PacketBuffer(data), "allmusic:channel");
            packet.setTarget(Side.CLIENT);
            runTask(() -> AllMusicForge.channel.sendTo(packet, players));
        } catch (Exception e) {
            AllMusic.log.warning("§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}

