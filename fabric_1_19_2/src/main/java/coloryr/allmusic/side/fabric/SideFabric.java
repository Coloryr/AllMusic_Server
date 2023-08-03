package coloryr.allmusic.side.fabric;

import coloryr.allmusic.AllMusicFabric;
import coloryr.allmusic.TaskItem;
import coloryr.allmusic.Tasks;
import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.hud.HudUtils;
import coloryr.allmusic.core.objs.config.SaveObj;
import coloryr.allmusic.core.objs.music.MusicObj;
import coloryr.allmusic.core.objs.music.SongInfoObj;
import coloryr.allmusic.core.side.ComType;
import coloryr.allmusic.core.side.ISide;
import coloryr.allmusic.side.fabric.event.MusicAddEvent;
import coloryr.allmusic.side.fabric.event.MusicPlayEvent;
import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class SideFabric extends ISide {

    @Override
    public void reload() {
        String path = "allmusic/";
        new AllMusic().init(new File(path));
    }

    @Override
    public int getAllPlayer() {
        return AllMusicFabric.server.getCurrentPlayerCount();
    }

    @Override
    public void runTask(Runnable run) {
        AllMusicFabric.server.execute(run);
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
        var player1 = AllMusicFabric.server.getPlayerManager().getPlayer(player);
        if (player1 == null)
            return false;

        return player1.hasPermissionLevel(2);
    }

    @Override
    public boolean needPlay() {
        int online = getAllPlayer();
        for (var player : AllMusicFabric.server.getPlayerManager().getPlayerList()) {
            if (AllMusic.getConfig().NoMusicPlayer.contains(player.getName().getString())) {
                online--;
            }
        }
        return online > 0;
    }

    @Override
    public void send(String data, String player) {
        send(AllMusicFabric.server.getPlayerManager().getPlayer(player), data);
    }

    @Override
    protected void topSendStop() {
        try {
            for (var player : AllMusicFabric.server.getPlayerManager().getPlayerList()) {
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
            var player = AllMusicFabric.server.getPlayerManager().getPlayer(name);
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
            for (var player : AllMusicFabric.server.getPlayerManager().getPlayerList()) {
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
            var player1 = AllMusicFabric.server.getPlayerManager().getPlayer(player);
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
            for (var player : AllMusicFabric.server.getPlayerManager().getPlayerList()) {
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
            var player1 = AllMusicFabric.server.getPlayerManager().getPlayer(player);
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
            var player1 = AllMusicFabric.server.getPlayerManager().getPlayer(player);
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
            for (var player : AllMusicFabric.server.getPlayerManager().getPlayerList()) {
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
            for (var player : AllMusicFabric.server.getPlayerManager().getPlayerList()) {
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
            for (var player : AllMusicFabric.server.getPlayerManager().getPlayerList()) {
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
        for (var player : AllMusicFabric.server.getPlayerManager().getPlayerList()) {
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
        for (var player : AllMusicFabric.server.getPlayerManager().getPlayerList()) {
            try {
                if (AllMusic.isOK(player.getName().getString(), null, true))
                    continue;
                FabricApi.sendBar(player, data);
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
            for (var player : AllMusicFabric.server.getPlayerManager().getPlayerList()) {
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
        for (var player : AllMusicFabric.server.getPlayerManager().getPlayerList()) {
            if (!AllMusic.getConfig().NoMusicPlayer.contains(player.getName().getString())) {
                player.sendMessage(Text.literal(data));
            }
        }
    }

    @Override
    public void bqt(String data) {
        if (AllMusic.getConfig().MessageLimit
                && data.length() > AllMusic.getConfig().MessageLimitSize) {
            data = data.substring(0, AllMusic.getConfig().MessageLimitSize - 1) + "...";
        }
        var finalData = Text.literal(data);
        runTask(() -> {
            for (var player : AllMusicFabric.server.getPlayerManager().getPlayerList()) {
                if (!AllMusic.getConfig().NoMusicPlayer.contains(player.getName().getString())) {
                    player.sendMessage(finalData);
                }
            }
        });
    }

    @Override
    public void sendMessaget(Object obj, String message) {
        runTask(() -> ((CommandOutput) obj).sendMessage(Text.literal(message)));
    }

    @Override
    public void sendMessage(Object obj, String message) {
        CommandOutput sender = (CommandOutput) obj;
        sender.sendMessage(Text.literal(message));
    }

    @Override
    public void sendMessageRun(Object obj, String message, String end, String command) {
        FabricApi.sendMessageRun(obj, message, end, command);
    }

    @Override
    public void sendMessageSuggest(Object obj, String message, String end, String command) {
        FabricApi.sendMessageSuggest(obj, message, end, command);
    }

    @Override
    public boolean onMusicPlay(SongInfoObj obj) {
        return MusicPlayEvent.EVENT.invoker().interact(obj) != ActionResult.PASS;
    }

    @Override
    public boolean onMusicAdd(Object obj, MusicObj music) {
        return MusicAddEvent.EVENT.invoker().interact((ServerPlayerEntity) obj, music) != ActionResult.PASS;
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
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
            buf.writeByte(666);
            buf.writeBytes(bytes);

            runTask(() -> ServerPlayNetworking.send(players, AllMusicFabric.ID, new PacketByteBuf(buf)));
        } catch (Exception e) {
            AllMusic.log.warning("§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}
