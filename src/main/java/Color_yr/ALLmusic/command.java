package Color_yr.ALLmusic;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class command extends Command {

    public command() {
        super("music");
    }

    public boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public String get_string(String a, String b, String c) {
        int x = a.indexOf(b) + b.length();
        int y;
        if (c != null)
            y = a.indexOf(c);
        else y = a.length();
        return a.substring(x, y);
    }

    public void add_music(CommandSender sender, String[] args) {

        String music_id;
        if (args[0].indexOf("id=") != -1) {
            if (args[0].indexOf("&user") != -1)
                music_id = get_string(args[0], "id=", "&user");
            else
                music_id = get_string(args[0], "id=", null);
        } else
            music_id = args[0];
        if (isInteger(music_id)) {
            if (PlayMusic.playlist.size() == ALLmusic_BC.Maxlist) {
                sender.sendMessage(new TextComponent("§d[ALLmusic]§c错误，队列已满"));
                return;
            } else if (ALLmusic_BC.Banconfig.getBoolean(music_id, false) == true) {
                sender.sendMessage(new TextComponent("§d[ALLmusic]§c错误，这首歌被禁点了"));
            } else if (PlayMusic.playlist.containsValue(music_id)) {
                sender.sendMessage(new TextComponent("§d[ALLmusic]§c错误，这首歌已经在队列了"));
            } else {
                PlayMusic.playlist.put(PlayMusic.All_music, music_id);
                PlayMusic.All_music++;
                sender.sendMessage(new TextComponent("§d[ALLmusic]§c点歌" + music_id + "成功"));
                ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLmusic]§2" + sender.getName() +
                        "点歌" + music_id));
                logs logs = new logs();
                logs.log_write("玩家：" + sender.getName() + " 点歌：" + music_id);
            }
            PlayMusic.stop.put(sender.getName(), "false");
            ALLmusic_BC.config.set("nomusic", PlayMusic.stop);
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(ALLmusic_BC.config, ALLmusic_BC.FileName);
            } catch (Exception e) {
                logs log = new logs();
                log.log_write(e.getMessage());
            }
        } else
            sender.sendMessage(new TextComponent("§d[ALLmusic]§c错误，请输入歌曲数字ID"));
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new TextComponent("§d[ALLmusic]§c错误，请使用/music help 获取帮助"));
            return;
        } else if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(new TextComponent("§d[ALLmusic]§2帮助手册"));
            sender.sendMessage(new TextComponent("§d[ALLmusic]§2使用/music [音乐ID] 来点歌"));
            sender.sendMessage(new TextComponent("§d[ALLmusic]§2使用/music stop 停止播放歌曲"));
            sender.sendMessage(new TextComponent("§d[ALLmusic]§2使用/music list 查看歌曲队列"));
            sender.sendMessage(new TextComponent("§d[ALLmusic]§2使用/music vote 投票切歌"));
            sender.sendMessage(new TextComponent("§d[ALLmusic]§2使用/music nomusic 不再参与点歌"));
            return;
        } else if (args[0].equalsIgnoreCase("stop")) {
            PlayMusic.SendToOnePlayer("[Stop]", sender.getName());
            sender.sendMessage(new TextComponent("§d[ALLmusic]§2已停止" + sender.getName() + "的音乐播放"));
        } else if (args[0].equalsIgnoreCase("list")) {
            if (PlayMusic.now_music == null) {
                sender.sendMessage(new TextComponent("§d[ALLmusic]§2无正在播放的歌曲"));
            } else {
                sender.sendMessage(new TextComponent("§d[ALLmusic]§2正在播放：" + PlayMusic.now_music));
            }
            if (PlayMusic.playlist.size() == 0) {
                sender.sendMessage(new TextComponent("§d[ALLmusic]§2队列中无歌曲"));
            } else {
                sender.sendMessage(new TextComponent("§d[ALLmusic]§2队列中有歌曲数：" + PlayMusic.playlist.size()));
                for (Map.Entry<Integer, String> music : PlayMusic.playlist.entrySet()) {
                    sender.sendMessage(new TextComponent("§d[ALLmusic]§2当前队列" + music.getKey() + "->" + music.getValue()));
                }
            }
        } else if (args[0].equalsIgnoreCase("vote")) {
            if (PlayMusic.playlist.size() == 0) {
                sender.sendMessage(new TextComponent("§d[ALLmusic]§2队列中无歌曲"));
            } else if (PlayMusic.Vote_time == 0) {
                PlayMusic.Vote_time = 30;
                PlayMusic.Vote.clear();
                PlayMusic.Vote.add(sender.getName());
                sender.sendMessage(new TextComponent("§d[ALLmusic]§2已发起切歌投票"));
                ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLmusic]§2" + sender.getName() +
                        "发起了切歌投票，30秒后结束，输入/music vote 同意切歌。"));
            } else if (PlayMusic.Vote_time > 0) {
                if (!PlayMusic.Vote.contains(sender.getName())) {
                    PlayMusic.Vote.add(sender.getName());
                    ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLmusic]§2" + sender.getName() + "同意切歌，共有" +
                            PlayMusic.Vote.size() + "名玩家同意切歌。"));
                }
            }
        } else if (args[0].equalsIgnoreCase("reload")) {
            ALLmusic_BC music = new ALLmusic_BC();
            music.loadconfig();
            sender.sendMessage(new TextComponent("§d[ALLmusic]§2已重读配置文件"));
        } else if (args[0].equalsIgnoreCase("next") && sender.hasPermission("ALLmusic.admin")) {
            PlayMusic.Music_time = 1;
            PlayMusic.SendToPlayer("[Stop]");
            sender.sendMessage(new TextComponent("§d[ALLmusic]§2已强制切歌"));
        } else if (args[0].equalsIgnoreCase("nomusic")) {
            PlayMusic.stop.put(sender.getName(), "true");
            PlayMusic.SendToOnePlayer("[Stop]", sender.getName());
            sender.sendMessage(new TextComponent("§d[ALLmusic]§2你不会再收到点歌了！想要再次参与点歌就点一首歌吧！"));
            ALLmusic_BC.config.set("nomusic", PlayMusic.stop);
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(ALLmusic_BC.config, ALLmusic_BC.FileName);
            } catch (Exception e) {
                logs log = new logs();
                log.log_write(e.getMessage());
            }
        } else
            add_music(sender, args);
    }
}
