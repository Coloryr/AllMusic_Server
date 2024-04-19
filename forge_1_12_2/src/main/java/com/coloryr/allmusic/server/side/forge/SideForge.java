package com.coloryr.allmusic.server.side.forge;

import coloryr.allmusic.AllMusicForge;
import coloryr.allmusic.TaskItem;
import coloryr.allmusic.Tasks;
import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.hud.HudUtils;
import com.coloryr.allmusic.server.core.objs.config.SaveObj;
import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.side.ComType;
import com.coloryr.allmusic.server.core.side.ISide;
import com.coloryr.allmusic.server.side.forge.event.MusicAddEvent;
import com.coloryr.allmusic.server.side.forge.event.MusicPlayEvent;
import com.google.gson.Gson;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.util.Locale;

public class SideForge extends ISide {

    @Override
    public void reload() {
        String path = String.format(Locale.ROOT, "config/%s/", "AllMusic");
        new AllMusic().init(new File(path));
    }

    @Override
    public int getAllPlayer() {
        return AllMusicForge.server.getCurrentPlayerCount();
    }

    @Override
    public void runTask(Runnable run) {
        AllMusicForge.server.addScheduledTask(run);
    }

    @Override
    public void runTask(Runnable run1, int delay) {
        Tasks.add(new TaskItem() {{
            tick = delay;
            run = run1;
        }});
    }

    @Override
    public boolean checkPermission(String player, String permission) {
        EntityPlayerMP player1 = AllMusicForge.server.getPlayerList().getPlayerByUsername(player);
        if (player1 == null)
            return false;

        return player1.canUseCommand(2, "music");
    }

    @Override
    public boolean needPlay() {
        int online = getAllPlayer();
        for (EntityPlayerMP player : AllMusicForge.server.getPlayerList().getPlayers()) {
            if (AllMusic.getConfig().NoMusicPlayer.contains(player.getName())) {
                online--;
            }
        }
        return online > 0;
    }

    @Override
    public void send(String data, String player) {
        send(AllMusicForge.server.getPlayerList().getPlayerByUsername(player), data);
    }

    @Override
    protected void topSendStop() {
        try {
            for (EntityPlayerMP player : AllMusicForge.server.getPlayerList().getPlayers()) {
                send(player, ComType.stop);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c停止指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    protected void topSendStop(String name) {
        try {
            EntityPlayerMP player = AllMusicForge.server.getPlayerList().getPlayerByUsername(name);
            if (player == null)
                return;
            send(player, ComType.stop);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c停止指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendMusic(String url) {
        try {
            for (EntityPlayerMP player : AllMusicForge.server.getPlayerList().getPlayers()) {
                if (AllMusic.isOK(player.getName(), null, false))
                    continue;
                send(player, ComType.play + url);
                AllMusic.addNowPlayPlayer(player.getName());
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    protected void topSendMusic(String player, String url) {
        try {
            EntityPlayerMP player1 = AllMusicForge.server.getPlayerList().getPlayerByUsername(player);
            if (player1 == null)
                return;
            if (AllMusic.isOK(player, null, false))
                return;
            send(ComType.play + url, player);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendPic(String url) {
        try {
            for (EntityPlayerMP player : AllMusicForge.server.getPlayerList().getPlayers()) {
                if (AllMusic.isOK(player.getName(), null, true))
                    continue;
                String name = player.getName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.EnablePic)
                    continue;
                send(player, ComType.img + url);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c图片指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendPic(String player, String url) {
        try {
            EntityPlayerMP player1 = AllMusicForge.server.getPlayerList().getPlayerByUsername(player);
            if (player1 == null)
                return;
            if (AllMusic.isOK(player1.getName(), null, true))
                return;
            send(ComType.img + url, player);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c图片指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendPos(String player, int pos) {
        try {
            EntityPlayerMP player1 = AllMusicForge.server.getPlayerList().getPlayerByUsername(player);
            if (player1 == null)
                return;
            if (AllMusic.isOK(player1.getName(), null, true))
                return;
            send(ComType.pos + pos, player);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudLyric(String data) {
        try {
            for (EntityPlayerMP player : AllMusicForge.server.getPlayerList().getPlayers()) {
                if (AllMusic.isOK(player.getName(), null, true))
                    continue;
                String name = player.getName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.EnableLyric)
                    continue;
                send(player, ComType.lyric + data);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudInfo(String data) {
        try {
            for (EntityPlayerMP player : AllMusicForge.server.getPlayerList().getPlayers()) {
                if (AllMusic.isOK(player.getName(), null, true))
                    continue;
                String name = player.getName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.EnableInfo)
                    continue;
                send(player, ComType.info + data);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词信息发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudList(String data) {
        try {
            for (EntityPlayerMP player : AllMusicForge.server.getPlayerList().getPlayers()) {
                if (AllMusic.isOK(player.getName(), null, true))
                    continue;
                String name = player.getName();
                SaveObj obj = HudUtils.get(name);
                if (!obj.EnableList)
                    continue;
                send(player, ComType.list + data);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲列表发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudUtilsAll() {
        for (EntityPlayerMP player : AllMusicForge.server.getPlayerList().getPlayers()) {
            String Name = player.getName();
            try {
                SaveObj obj = HudUtils.get(Name);
                String data = new Gson().toJson(obj);
                send(player, data);
            } catch (Exception e1) {
                AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void sendBar(String data) {
        for (EntityPlayerMP player : AllMusicForge.server.getPlayerList().getPlayers()) {
            try {
                if (AllMusic.isOK(player.getName(), null, true))
                    continue;
                ForgeApi.sendBar(player, data);
            } catch (Exception e1) {
                AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void clearHud(String player) {
        try {
            send(ComType.clear, player);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void clearHud() {
        try {
            for (EntityPlayerMP player : AllMusicForge.server.getPlayerList().getPlayers()) {
                send(player, ComType.clear);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void bq(String data) {
        if (AllMusic.getConfig().MessageLimit
                && data.length() > AllMusic.getConfig().MessageLimitSize) {
            data = data.substring(0, AllMusic.getConfig().MessageLimitSize - 1) + "...";
        }
        for (EntityPlayerMP player : AllMusicForge.server.getPlayerList().getPlayers()) {
            if (!AllMusic.getConfig().NoMusicPlayer.contains(player.getName())) {
                player.sendMessage(new TextComponentString(data));
            }
        }
    }

    @Override
    public void bqt(String data) {
        if (AllMusic.getConfig().MessageLimit
                && data.length() > AllMusic.getConfig().MessageLimitSize) {
            data = data.substring(0, AllMusic.getConfig().MessageLimitSize - 1) + "...";
        }
        TextComponentString finalData = new TextComponentString(data);
        runTask(() -> {
            for (EntityPlayerMP player : AllMusicForge.server.getPlayerList().getPlayers()) {
                if (!AllMusic.getConfig().NoMusicPlayer.contains(player.getName())) {
                    player.sendMessage(finalData);
                }
            }
        });
    }

    @Override
    public void sendMessaget(Object obj, String message) {
        runTask(() -> ((ICommandSender) obj).sendMessage(new TextComponentString(message)));
    }

    @Override
    public void sendMessage(Object obj, String message) {
        ICommandSender sender = (ICommandSender) obj;
        sender.sendMessage(new TextComponentString(message));
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
    public void updateInfo() {

    }

    @Override
    public void updateLyric() {

    }

    @Override
    public void ping() {

    }

    private void send(EntityPlayerMP players, String data) {
        if (players == null)
            return;
        try {
//            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
//            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
//            buf.writeByte(666);
//            buf.writeBytes(bytes);

            runTask(() -> AllMusicForge.channel.sendTo(new PacketMessage(data), players));
        } catch (Exception e) {
            AllMusic.log.warning("§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}

