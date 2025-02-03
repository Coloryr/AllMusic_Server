package com.coloryr.allmusic.server.side.forge;

import com.coloryr.allmusic.server.AllMusicForge;
import com.coloryr.allmusic.server.TaskItem;
import com.coloryr.allmusic.server.Tasks;
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
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

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
        return AllMusicForge.server.getPlayerCount();
    }

    @Override
    public void runTask(Runnable run) {
        AllMusicForge.server.execute(run);
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
        CommandSourceStack source = (CommandSourceStack) player;
        return source.hasPermission(2);
    }

    @Override
    public boolean isPlayer(Object player) {
        CommandSourceStack source = (CommandSourceStack) player;
        return source.isPlayer();
    }

    @Override
    public boolean needPlay() {
        for (ServerPlayer player : AllMusicForge.server.getPlayerList().getPlayers()) {
            if (!AllMusic.isSkip(player.getName().getString(), null, false)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void sideSendStop() {
        try {
            for (ServerPlayer player : AllMusicForge.server.getPlayerList().getPlayers()) {
                send(player, new PackData(ComType.STOP, null, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c停止指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    protected void sideSendStop(String name) {
        try {
            ServerPlayer player = AllMusicForge.server.getPlayerList().getPlayerByName(name);
            if (player == null)
                return;
            send(player, new PackData(ComType.STOP, null, 0));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c停止指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendMusic(String url) {
        try {
            for (ServerPlayer player : AllMusicForge.server.getPlayerList().getPlayers()) {
                if (AllMusic.isSkip(player.getName().getString(), null, false))
                    continue;
                send(player, new PackData(ComType.PLAY, url, 0));
                AllMusic.addNowPlayPlayer(player.getName().getString());
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    protected void sideSendMusic(String player, String url) {
        try {
            ServerPlayer player1 = AllMusicForge.server.getPlayerList().getPlayerByName(player);
            if (player1 == null)
                return;
            if (AllMusic.isSkip(player, null, false))
                return;
            send(player1, new PackData(ComType.PLAY, url, 0));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendPic(String url) {
        try {
            for (ServerPlayer player : AllMusicForge.server.getPlayerList().getPlayers()) {
                if (AllMusic.isSkip(player.getName().getString(), null, true))
                    continue;
                String name = player.getName().getString();
                SaveObj obj = HudUtils.get(name);
                if (!obj.pic.enable)
                    continue;
                send(player, new PackData(ComType.IMG, url, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c图片指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendPic(String player, String url) {
        try {
            ServerPlayer player1 = AllMusicForge.server.getPlayerList().getPlayerByName(player);
            if (player1 == null)
                return;
            if (AllMusic.isSkip(player1.getName().getString(), null, true))
                return;
            send(player1, new PackData(ComType.IMG, url, 0));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c图片指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendPos(String player, int pos) {
        try {
            ServerPlayer player1 = AllMusicForge.server.getPlayerList().getPlayerByName(player);
            if (player1 == null)
                return;
            if (AllMusic.isSkip(player1.getName().getString(), null, true))
                return;
            send(player1, new PackData(ComType.POS, null, pos));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudLyric(String data) {
        try {
            for (ServerPlayer player : AllMusicForge.server.getPlayerList().getPlayers()) {
                if (AllMusic.isSkip(player.getName().getString(), null, true))
                    continue;
                String name = player.getName().getString();
                SaveObj obj = HudUtils.get(name);
                if (!obj.lyric.enable)
                    continue;
                send(player, new PackData(ComType.LYRIC, data, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudInfo(String data) {
        try {
            for (ServerPlayer player : AllMusicForge.server.getPlayerList().getPlayers()) {
                if (AllMusic.isSkip(player.getName().getString(), null, true))
                    continue;
                String name = player.getName().getString();
                SaveObj obj = HudUtils.get(name);
                if (!obj.info.enable)
                    continue;
                send(player, new PackData(ComType.INFO, data, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词信息发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudPos(String name) {
        try {
            ServerPlayer player = AllMusicForge.server.getPlayerList().getPlayerByName(name);
            if (player == null)
                return;
            SaveObj obj = HudUtils.get(name);
            String data = AllMusic.gson.toJson(obj);
            send(player, new PackData(ComType.INFO, data, 0));
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
            ServerPlayer player = AllMusicForge.server.getPlayerList().getPlayerByName(name);
            if (player == null)
                return;
            if (AllMusic.isSkip(name, null, true))
                return;
            switch (pos) {
                case INFO:
                    send(player, new PackData(ComType.INFO, data, 0));
                    break;
                case LIST:
                    send(player, new PackData(ComType.LIST, data, 0));
                    break;
                case LYRIC:
                    send(player, new PackData(ComType.LYRIC, data, 0));
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
            for (ServerPlayer player : AllMusicForge.server.getPlayerList().getPlayers()) {
                if (AllMusic.isSkip(player.getName().getString(), null, true))
                    continue;
                String name = player.getName().getString();
                SaveObj obj = HudUtils.get(name);
                if (!obj.list.enable)
                    continue;
                send(player, new PackData(ComType.LIST, data, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲列表发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudUtilsAll() {
        for (ServerPlayer player : AllMusicForge.server.getPlayerList().getPlayers()) {
            String Name = player.getName().getString();
            try {
                SaveObj obj = HudUtils.get(Name);
                String data = new Gson().toJson(obj);
                send(player, new PackData(ComType.HUD, data, 0));
            } catch (Exception e1) {
                AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void sendBar(String data) {
        for (ServerPlayer player : AllMusicForge.server.getPlayerList().getPlayers()) {
            try {
                if (AllMusic.isSkip(player.getName().getString(), null, true))
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
            ServerPlayer player1 = AllMusicForge.server.getPlayerList().getPlayerByName(player);
            if (player1 == null)
                return;
            send(player1, new PackData(ComType.CLEAR, null, 0));
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void clearHud() {
        try {
            for (ServerPlayer player : AllMusicForge.server.getPlayerList().getPlayers()) {
                send(player, new PackData(ComType.CLEAR, null, 0));
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void broadcast(String data) {
        for (ServerPlayer player : AllMusicForge.server.getPlayerList().getPlayers()) {
            if (!AllMusic.isSkip(player.getName().getString(), null, false)) {
                player.sendSystemMessage(Component.literal(data));
            }
        }
    }

    @Override
    public void broadcastWithRun(String message, String end, String command) {
        ForgeApi.sendMessageBqRun(message, end, command);
    }

    @Override
    public void sendMessage(Object obj, String message) {
        CommandSourceStack source = (CommandSourceStack) obj;
        source.sendSystemMessage(Component.literal(message));
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
        NeoForge.EVENT_BUS.post(event);
        return event.isCancel();
    }

    @Override
    public boolean onMusicAdd(Object obj, MusicObj music) {
        CommandSourceStack source = (CommandSourceStack) obj;
        MusicAddEvent event = new MusicAddEvent(music, source.getPlayer());
        NeoForge.EVENT_BUS.post(event);
        return event.isCancel();
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

    @Override
    public List<String> getPlayerList() {
        List<String> list = new ArrayList<>();
        for (ServerPlayer player : AllMusicForge.server.getPlayerList().getPlayers()) {
            list.add(player.getName().getString());
        }
        return list;
    }

    private void send(ServerPlayer players, PackData data) {
        if (players == null)
            return;
        try {
            runTask(() -> PacketDistributor.sendToPlayer(players, data));
        } catch (Exception e) {
            AllMusic.log.warning("§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}
