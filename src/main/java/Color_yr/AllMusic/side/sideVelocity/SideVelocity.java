package Color_yr.AllMusic.side.sideVelocity;

import Color_yr.AllMusic.api.ISide;
import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.AllMusicVelocity;
import Color_yr.AllMusic.musicPlay.sendHud.SaveOBJ;
import com.google.gson.Gson;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class SideVelocity implements ISide {
    private boolean isOK(Player player, boolean in) {
        try {
            if (player == null)
                return false;
            if (AllMusic.getConfig().getNoMusicServer()
                    .contains(player.getCurrentServer().get().getServerInfo().getName()))
                return false;
            String name = player.getUsername();
            if (AllMusic.getConfig().getNoMusicPlayer().contains(player.getUsername()))
                return false;
            return !in || AllMusic.containNowPlay(name);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public void send(String data, String player, Boolean isplay) {
        if (AllMusicVelocity.plugin.server.getPlayer(player).isPresent()) {
            send(AllMusicVelocity.plugin.server.getPlayer(player).get(), data, isplay);
        } else {
            AllMusic.log.warning("§d[AllMusic]§c找不到玩家:" + player);
        }
    }

    @Override
    public void send(String data, Boolean isplay) {
        try {
            Collection<Player> values = AllMusicVelocity.plugin.server.getAllPlayers();
            for (Player player : values) {
                if (isplay && !isOK(player, false))
                    continue;
                send(player, data, isplay);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲发送发生错误");
            e.printStackTrace();
        }
    }

    @Override
    public int getAllPlayer() {
        return AllMusicVelocity.plugin.server.getPlayerCount();
    }

    @Override
    public void bq(String data) {
        AllMusicVelocity.plugin.server.sendMessage(Component.text(data));
    }

    @Override
    public void bqt(String data) {
        AllMusicVelocity.plugin.server.sendMessage(Component.text(data));
    }

    @Override
    public boolean NeedPlay() {
        int online = 0;
        for (RegisteredServer server : AllMusicVelocity.plugin.server.getAllServers()) {
            if (AllMusic.getConfig().getNoMusicServer().contains(server.getServerInfo().getName()))
                continue;
            for (Player player : server.getPlayersConnected())
                if (!AllMusic.getConfig().getNoMusicPlayer().contains(player.getUsername()))
                    online++;
        }
        return online > 0;
    }

    @Override
    public boolean sendHudLyric(String data) {
        boolean Save = false;
        try {
            for (Player player : AllMusicVelocity.plugin.server.getAllPlayers()) {
                if (!isOK(player, true))
                    continue;
                SaveOBJ obj = AllMusic.getConfig().getInfoSave(player.getUsername());
                if (obj == null) {
                    obj = AllMusic.getConfig().getDefaultHud().copy();
                    AllMusic.getConfig().setInfoSave(obj, player.getUsername());
                    Save = true;
                }
                if (!obj.isEnableLyric())
                    continue;
                send(player, "[Lyric]" + data, null);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词发送出错");
            e.printStackTrace();
        }
        return Save;
    }

    @Override
    public boolean sendHudInfo(String data) {
        boolean Save = false;
        try {
            for (Player player : AllMusicVelocity.plugin.server.getAllPlayers()) {
                if (!isOK(player, true))
                    continue;
                SaveOBJ obj = AllMusic.getConfig().getInfoSave(player.getUsername());
                if (obj == null) {
                    obj = AllMusic.getConfig().getDefaultHud().copy();
                    AllMusic.getConfig().setInfoSave(obj, player.getUsername());
                    Save = true;
                }
                if (!obj.isEnableInfo())
                    continue;
                send(player, "[Info]" + data, null);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词信息发送出错");
            e.printStackTrace();
        }
        return Save;
    }

    @Override
    public boolean sendHudList(String data) {
        boolean Save = false;
        try {
            for (Player player : AllMusicVelocity.plugin.server.getAllPlayers()) {
                if (!isOK(player, true))
                    continue;
                String name = player.getUsername();
                SaveOBJ obj = AllMusic.getConfig().getInfoSave(name);
                if (obj == null) {
                    obj = AllMusic.getConfig().getDefaultHud().copy();
                    AllMusic.getConfig().setInfoSave(obj, name);
                    Save = true;
                }
                if (!obj.isEnableList())
                    continue;
                send(player, "[List]" + data, null);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲列表发送出错");
            e.printStackTrace();
        }
        return Save;
    }

    @Override
    public void sendHudSaveAll() {
        for (Player players : AllMusicVelocity.plugin.server.getAllPlayers()) {
            String Name = players.getUsername();
            try {
                SaveOBJ obj = AllMusic.getConfig().getInfoSave(Name);
                if (obj == null) {
                    obj = AllMusic.getConfig().getDefaultHud().copy();
                    AllMusic.getConfig().setInfoSave(obj, Name);
                    AllMusic.save();
                }
                String data = new Gson().toJson(obj);
                send(data, Name, null);
            } catch (Exception e1) {
                AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void clearHud(String player) {
        send("[clear]", player, null);
    }

    @Override
    public void clearHudAll() {
        try {
            Collection<Player> values = AllMusicVelocity.plugin.server.getAllPlayers();
            for (Player players : values) {
                send(players, "[clear]", null);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessaget(Object obj, String Message) {
        CommandSource sender = (CommandSource) obj;
        sender.sendMessage(Component.text(Message));
    }

    @Override
    public void sendMessage(Object obj, String Message) {
        CommandSource sender = (CommandSource) obj;
        sender.sendMessage(Component.text(Message));
    }

    @Override
    public void sendMessageRun(Object obj, String Message, String end, String command) {
        CommandSource sender = (CommandSource) obj;
        TextComponent send = Component.text(Message + end)
                .clickEvent(ClickEvent.runCommand(command));
        sender.sendMessage(send);
    }

    @Override
    public void sendMessageSuggest(Object obj, String Message, String end, String command) {
        CommandSource sender = (CommandSource) obj;
        TextComponent send = Component.text(Message + end)
                .clickEvent(ClickEvent.suggestCommand(command));
        sender.sendMessage(send);
    }

    @Override
    public void runTask(Runnable run) {
        AllMusicVelocity.plugin.server.getScheduler().buildTask(AllMusicVelocity.plugin, run).schedule();
    }

    @Override
    public void reload() {
        new AllMusic().init(AllMusicVelocity.plugin.dataDirectory.toFile());
    }

    @Override
    public boolean checkPermission(String player, String permission) {
        try {
            Player player1 = AllMusicVelocity.plugin.server.getPlayer(player).get();
            player1.hasPermission(permission);
        } catch (NoSuchElementException ignored) {

        }
        return false;
    }

    @Override
    public void task(Runnable run, int delay) {
        AllMusicVelocity.plugin.server.getScheduler().buildTask(AllMusicVelocity.plugin, run)
                .delay(delay, TimeUnit.MICROSECONDS).schedule();
    }

    private void send(Player players, String data, Boolean isplay) {
        if (players == null)
            return;
        try {
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
            buf.writeByte(666);
            buf.writeBytes(bytes);
            runTask(() -> players.sendPluginMessage(AllMusicVelocity.plugin.channel, buf.array()));
            if (isplay != null) {
                if (isplay) {
                    AllMusic.addNowPlayPlayer(players.getUsername());
                } else {
                    AllMusic.removeNowPlayPlayer(players.getUsername());
                }
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}
