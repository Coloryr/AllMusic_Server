package coloryr.allmusic.side.forge;

import coloryr.allmusic.AllMusicForge;
import coloryr.allmusic.TaskItem;
import coloryr.allmusic.Tasks;
import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.hud.HudUtils;
import coloryr.allmusic.core.objs.config.SaveObj;
import coloryr.allmusic.core.objs.music.MusicObj;
import coloryr.allmusic.core.objs.music.SongInfoObj;
import coloryr.allmusic.core.side.ComType;
import coloryr.allmusic.core.side.ISide;
import coloryr.allmusic.side.forge.event.MusicAddEvent;
import coloryr.allmusic.side.forge.event.MusicPlayEvent;
import com.google.gson.Gson;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.PacketDistributor;

import java.io.File;
import java.util.Locale;
import java.util.UUID;

public class SideForge extends ISide {

    @Override
    public void reload() {
        String path = String.format(Locale.ROOT, "config/%s/", "AllMusic");
        new AllMusic().init(new File(path));
    }

    @Override
    public int getAllPlayer() {
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
    public boolean checkPermission(String player, String permission) {
        ServerPlayerEntity player1 = AllMusicForge.server.getPlayerList().getPlayerByName(player);
        if (player1 == null)
            return false;

        return player1.hasPermissions(2);
    }

    @Override
    public boolean needPlay() {
        int online = getAllPlayer();
        for (ServerPlayerEntity player : AllMusicForge.server.getPlayerList().getPlayers()) {
            if (AllMusic.getConfig().NoMusicPlayer.contains(player.getName().getString())) {
                online--;
            }
        }
        return online > 0;
    }

    @Override
    public void send(String data, String player) {
        send(AllMusicForge.server.getPlayerList().getPlayerByName(player), data);
    }

    @Override
    protected void topSendStop() {
        try {
            for (ServerPlayerEntity player : AllMusicForge.server.getPlayerList().getPlayers()) {
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
            ServerPlayerEntity player =  AllMusicForge.server.getPlayerList().getPlayerByName(name);
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
            for (ServerPlayerEntity player : AllMusicForge.server.getPlayerList().getPlayers()) {
                if (AllMusic.isOK(player.getName().getString(), null, false))
                    continue;
                send(player, ComType.play + url);
                AllMusic.addNowPlayPlayer(player.getName().getString());
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲指令发送出错");
            e.printStackTrace();
        }
    }

    @Override
    protected void topSendMusic(String player, String url) {
        try {
            ServerPlayerEntity player1 = AllMusicForge.server.getPlayerList().getPlayerByName(player);
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
            for (ServerPlayerEntity player : AllMusicForge.server.getPlayerList().getPlayers()) {
                if (AllMusic.isOK(player.getName().getString(), null, true))
                    continue;
                String name = player.getName().getString();
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
            ServerPlayerEntity player1 = AllMusicForge.server.getPlayerList().getPlayerByName(player);
            if (player1 == null)
                return;
            if (AllMusic.isOK(player1.getName().getString(), null, true))
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
            ServerPlayerEntity player1 = AllMusicForge.server.getPlayerList().getPlayerByName(player);
            if (player1 == null)
                return;
            if (AllMusic.isOK(player1.getName().getString(), null, true))
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
            for (ServerPlayerEntity player : AllMusicForge.server.getPlayerList().getPlayers()) {
                if (AllMusic.isOK(player.getName().getString(), null, true))
                    continue;
                String name = player.getName().getString();
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
            for (ServerPlayerEntity player : AllMusicForge.server.getPlayerList().getPlayers()) {
                if (AllMusic.isOK(player.getName().getString(), null, true))
                    continue;
                String name = player.getName().getString();
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
            for (ServerPlayerEntity player : AllMusicForge.server.getPlayerList().getPlayers()) {
                if (AllMusic.isOK(player.getName().getString(), null, true))
                    continue;
                String name = player.getName().getString();
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
        for (ServerPlayerEntity player :  AllMusicForge.server.getPlayerList().getPlayers()) {
            String Name = player.getName().getString();
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
        for (ServerPlayerEntity player : AllMusicForge.server.getPlayerList().getPlayers()) {
            try {
                if (AllMusic.isOK(player.getName().getString(), null, true))
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
            for (ServerPlayerEntity player :  AllMusicForge.server.getPlayerList().getPlayers()) {
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
        for (ServerPlayerEntity player :  AllMusicForge.server.getPlayerList().getPlayers()) {
            if (!AllMusic.getConfig().NoMusicPlayer.contains(player.getName().getString())) {
                player.sendMessage(new StringTextComponent(data), UUID.randomUUID());
            }
        }
    }

    @Override
    public void bqt(String data) {
        if (AllMusic.getConfig().MessageLimit
                && data.length() > AllMusic.getConfig().MessageLimitSize) {
            data = data.substring(0, AllMusic.getConfig().MessageLimitSize - 1) + "...";
        }
        StringTextComponent finalData = new StringTextComponent(data);
        runTask(() -> {
            for (ServerPlayerEntity player : AllMusicForge.server.getPlayerList().getPlayers()) {
                if (!AllMusic.getConfig().NoMusicPlayer.contains(player.getName().getString())) {
                    player.sendMessage(finalData, UUID.randomUUID());
                }
            }
        });
    }

    @Override
    public void sendMessaget(Object obj, String message) {
        runTask(() -> ((CommandSource) obj).sendSuccess(new StringTextComponent(message), false));
    }

    @Override
    public void sendMessage(Object obj, String message) {
        CommandSource sender = (CommandSource) obj;
        sender.sendSuccess(new StringTextComponent(message), false);
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
        MusicAddEvent event = new MusicAddEvent(music, (CommandSource) obj);
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

    private void send(ServerPlayerEntity players, String data) {
        if (players == null)
            return;
        try {
//            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
//            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
//            buf.writeByte(666);
//            buf.writeBytes(bytes);

            runTask(() -> AllMusicForge.channel.send(PacketDistributor.PLAYER.with(
                    () -> players
            ), data));
        } catch (Exception e) {
            AllMusic.log.warning("§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}
