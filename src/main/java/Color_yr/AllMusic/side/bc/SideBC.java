package Color_yr.AllMusic.side.bc;

import Color_yr.AllMusic.api.ISide;
import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.AllMusicBC;
import Color_yr.AllMusic.hud.HudSave;
import Color_yr.AllMusic.hud.obj.SaveOBJ;
import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class SideBC implements ISide {
    private boolean isOK(ProxiedPlayer player, boolean in) {
        if (player == null || player.getServer() == null)
            return false;
        if (AllMusic.getConfig().getNoMusicServer()
                .contains(player.getServer().getInfo().getName()))
            return false;
        String name = player.getName();
        if (AllMusic.getConfig().getNoMusicPlayer().contains(player.getName()))
            return false;
        return !in || AllMusic.containNowPlay(name);
    }

    @Override
    public void send(String data, String player, Boolean isplay) {
        send(ProxyServer.getInstance().getPlayer(player), data, isplay);
    }

    @Override
    public void send(String data, Boolean isplay) {
        try {
            Collection<ProxiedPlayer> values = ProxyServer.getInstance().getPlayers();
            for (ProxiedPlayer player : values) {
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
        return ProxyServer.getInstance().getOnlineCount();
    }

    @Override
    public void sendHudLyric(String data) {
        try {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (!isOK(player, true))
                    continue;
                SaveOBJ obj = HudSave.get(player.getName());
                if (!obj.isEnableLyric())
                    continue;
                send(player, "[Lyric]" + data, null);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudInfo(String data) {
        try {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (!isOK(player, true))
                    continue;
                SaveOBJ obj = HudSave.get(player.getName());
                if (!obj.isEnableInfo())
                    continue;
                send(player, "[Info]" + data, null);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词信息发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudList(String data) {
        try {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (!isOK(player, true))
                    continue;
                String name = player.getName();
                SaveOBJ obj = HudSave.get(name);
                if (!obj.isEnableList())
                    continue;
                send(player, "[List]" + data, null);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲列表发送出错");
            e.printStackTrace();
        }
    }

    @Override
    public void sendHudSaveAll() {
        for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
            String Name = players.getName();
            try {
                SaveOBJ obj = HudSave.get(Name);
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
            Collection<ProxiedPlayer> values = ProxyServer.getInstance().getPlayers();
            for (ProxiedPlayer players : values) {
                send(players, "[clear]", null);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词发生出错");
            e.printStackTrace();
        }
    }

    @Override
    public void bq(String data) {
        ProxyServer.getInstance().broadcast(new TextComponent(data));
    }

    @Override
    public void bqt(String data) {
        ProxyServer.getInstance().broadcast(new TextComponent(data));
    }

    @Override
    public boolean NeedPlay() {
        int online = 0;
        for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
            if (AllMusic.getConfig().getNoMusicServer().contains(server.getName()))
                continue;
            for (ProxiedPlayer player : server.getPlayers())
                if (!AllMusic.getConfig().getNoMusicPlayer().contains(player.getName()))
                    online++;
        }
        return online > 0;
    }


    @Override
    public void sendMessaget(Object obj, String Message) {
        CommandSender sender = (CommandSender) obj;
        sender.sendMessage(new TextComponent(Message));
    }

    @Override
    public void sendMessage(Object obj, String Message) {
        CommandSender sender = (CommandSender) obj;
        sender.sendMessage(new TextComponent(Message));
    }

    @Override
    public void sendMessageRun(Object obj, String Message, String end, String command) {
        CommandSender sender = (CommandSender) obj;
        TextComponent send = new TextComponent(Message + end);
        send.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        sender.sendMessage(send);
    }

    @Override
    public void sendMessageSuggest(Object obj, String Message, String end, String command) {
        CommandSender sender = (CommandSender) obj;
        TextComponent send = new TextComponent(Message + end);
        send.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        sender.sendMessage(send);
    }

    @Override
    public void runTask(Runnable run) {
        ProxyServer.getInstance().getScheduler().runAsync(AllMusicBC.plugin, run);
    }

    @Override
    public void reload() {
        new AllMusic().init(AllMusicBC.plugin.getDataFolder());
    }

    @Override
    public boolean checkPermission(String player, String permission) {
        if(AllMusic.getConfig().getAdmin().contains(player))
            return false;
        ProxiedPlayer player1 = ProxyServer.getInstance().getPlayer(player);
        if (player1 == null)
            return true;
        return !player1.hasPermission(permission);
    }

    @Override
    public void task(Runnable run, int delay) {
        ProxyServer.getInstance().getScheduler().schedule(AllMusicBC.plugin, run, delay, TimeUnit.MICROSECONDS);
    }

    private void send(ProxiedPlayer players, String data, Boolean isplay) {
        if (players == null)
            return;
        try {
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
            buf.writeByte(666);
            buf.writeBytes(bytes);
            runTask(() -> players.sendData(AllMusic.channel, buf.array()));
            if (isplay != null) {
                if (isplay) {
                    AllMusic.addNowPlayPlayer(players.getName());
                } else {
                    AllMusic.removeNowPlayPlayer(players.getName());
                }
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}
