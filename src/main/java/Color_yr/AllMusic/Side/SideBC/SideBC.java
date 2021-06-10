package Color_yr.AllMusic.Side.SideBC;

import Color_yr.AllMusic.API.ISide;
import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.AllMusicBC;
import Color_yr.AllMusic.MusicPlay.SendHud.SaveOBJ;
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
    public void Send(String data, String player, Boolean isplay) {
        Send(ProxyServer.getInstance().getPlayer(player), data, isplay);
    }

    @Override
    public void Send(String data, Boolean isplay) {
        try {
            Collection<ProxiedPlayer> values = ProxyServer.getInstance().getPlayers();
            for (ProxiedPlayer player : values) {
                if (isplay && !isOK(player, false))
                    continue;
                Send(player, data, isplay);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲发送发生错误");
            e.printStackTrace();
        }
    }

    @Override
    public int GetAllPlayer() {
        return ProxyServer.getInstance().getOnlineCount();
    }

    @Override
    public boolean SendHudLyric(String data) {
        boolean Save = false;
        try {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (!isOK(player, true))
                    continue;
                SaveOBJ obj = AllMusic.getConfig().getInfoSave(player.getName());
                if (obj == null) {
                    obj = AllMusic.getConfig().getDefaultHud().copy();
                    AllMusic.getConfig().setInfoSave(obj, player.getName());
                    Save = true;
                }
                if (!obj.isEnableLyric())
                    continue;
                Send(player, "[Lyric]" + data, null);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词发送出错");
            e.printStackTrace();
        }
        return Save;
    }

    @Override
    public boolean SendHudInfo(String data) {
        boolean Save = false;
        try {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (!isOK(player, true))
                    continue;
                SaveOBJ obj = AllMusic.getConfig().getInfoSave(player.getName());
                if (obj == null) {
                    obj = AllMusic.getConfig().getDefaultHud().copy();
                    AllMusic.getConfig().setInfoSave(obj, player.getName());
                    Save = true;
                }
                if (!obj.isEnableInfo())
                    continue;
                Send(player, "[Info]" + data, null);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌词信息发送出错");
            e.printStackTrace();
        }
        return Save;
    }

    @Override
    public boolean SendHudList(String data) {
        boolean Save = false;
        try {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (!isOK(player, true))
                    continue;
                String name = player.getName();
                SaveOBJ obj = AllMusic.getConfig().getInfoSave(name);
                if (obj == null) {
                    obj = AllMusic.getConfig().getDefaultHud().copy();
                    AllMusic.getConfig().setInfoSave(obj, name);
                    Save = true;
                }
                if (!obj.isEnableList())
                    continue;
                Send(player, "[List]" + data, null);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲列表发送出错");
            e.printStackTrace();
        }
        return Save;
    }

    @Override
    public void SendHudSaveAll() {
        for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
            String Name = players.getName();
            try {
                SaveOBJ obj = AllMusic.getConfig().getInfoSave(Name);
                if (obj == null) {
                    obj = AllMusic.getConfig().getDefaultHud().copy();
                    AllMusic.getConfig().setInfoSave(obj, Name);
                    AllMusic.save();
                }
                String data = new Gson().toJson(obj);
                Send(data, Name, null);
            } catch (Exception e1) {
                AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void ClearHud(String player) {
        Send("[clear]", player, null);
    }

    @Override
    public void ClearHudAll() {
        try {
            Collection<ProxiedPlayer> values = ProxyServer.getInstance().getPlayers();
            for (ProxiedPlayer players : values) {
                Send(players, "[clear]", null);
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
        int online = GetAllPlayer();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (AllMusic.getConfig().getNoMusicPlayer().contains(player.getName())) {
                online--;
            } else {
                if (player.getServer() != null) {
                    ServerInfo server = player.getServer().getInfo();
                    if (server != null && AllMusic.getConfig().getNoMusicServer().contains(server.getName())) {
                        online--;
                    }
                }
            }
        }
        return online > 0;
    }


    @Override
    public void SendMessaget(Object obj, String Message) {
        CommandSender sender = (CommandSender) obj;
        sender.sendMessage(new TextComponent(Message));
    }

    @Override
    public void SendMessage(Object obj, String Message) {
        CommandSender sender = (CommandSender) obj;
        sender.sendMessage(new TextComponent(Message));
    }

    @Override
    public void SendMessageRun(Object obj, String Message, String end, String command) {
        CommandSender sender = (CommandSender) obj;
        TextComponent send = new TextComponent(Message + end);
        send.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        sender.sendMessage(send);
    }

    @Override
    public void SendMessageSuggest(Object obj, String Message, String end, String command) {
        CommandSender sender = (CommandSender) obj;
        TextComponent send = new TextComponent(Message + end);
        send.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        sender.sendMessage(send);
    }

    @Override
    public void RunTask(Runnable run) {
        ProxyServer.getInstance().getScheduler().runAsync(AllMusicBC.plugin, run);
    }

    @Override
    public void reload() {
        new AllMusic().init(AllMusicBC.plugin.getDataFolder());
    }

    @Override
    public boolean checkPermission(String player, String permission) {
        ProxiedPlayer player1 = ProxyServer.getInstance().getPlayer(player);
        if (player1 == null)
            return true;
        return !player1.hasPermission(permission);
    }

    @Override
    public void task(Runnable run, int delay) {
        ProxyServer.getInstance().getScheduler().schedule(AllMusicBC.plugin, run, delay, TimeUnit.MICROSECONDS);
    }

    private void Send(ProxiedPlayer players, String data, Boolean isplay) {
        if (players == null)
            return;
        try {
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.buffer(bytes.length + 1);
            buf.writeByte(666);
            buf.writeBytes(bytes);
            RunTask(() -> players.sendData(AllMusic.channel, buf.array()));
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
