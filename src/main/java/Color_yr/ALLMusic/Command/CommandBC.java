package Color_yr.ALLMusic.Command;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.ALLMusicBC;
import Color_yr.ALLMusic.Play.PlayMusic;
import Color_yr.ALLMusic.Utils.Function;
import Color_yr.ALLMusic.Utils.logs;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CommandBC extends Command implements TabExecutor {

    public CommandBC() {
        super("music");
    }

    private void AddMusic(CommandSender sender, String[] args) {

        String MusicID;
        if (args[0].contains("id=")) {
            if (args[0].contains("&user"))
                MusicID = Function.getString(args[0], "id=", "&user");
            else
                MusicID = Function.getString(args[0], "id=", null);
        } else if (args[0].contains("song/")) {
            if (args[0].contains("/?userid"))
                MusicID = Function.getString(args[0], "song/", "/?userid");
            else
                MusicID = Function.getString(args[0], "song/", null);
        } else
            MusicID = args[0];
        if (Function.isInteger(MusicID)) {
            if (PlayMusic.PlayList.size() == ALLMusic.Config.getMaxList()) {
                sender.sendMessage(new TextComponent("§d[ALLMusic]§c错误，队列已满"));
                return;
            } else if (ALLMusic.Config.getBanMusic().contains(MusicID)) {
                sender.sendMessage(new TextComponent("§d[ALLMusic]§c错误，这首歌被禁点了"));
            } else if (PlayMusic.PlayList.contains(MusicID)) {
                sender.sendMessage(new TextComponent("§d[ALLMusic]§c错误，这首歌已经在队列了"));
            } else {
                PlayMusic.PlayList.add(MusicID);
                sender.sendMessage(new TextComponent("§d[ALLMusic]§c点歌" + MusicID + "成功"));
                ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLMusic]§2" + sender.getName() +
                        "点歌" + MusicID));
                logs.log_write("玩家：" + sender.getName() + " 点歌：" + MusicID);
            }
            ALLMusic.Config.RemoveNoMusicPlayer(sender.getName());
        } else
            sender.sendMessage(new TextComponent("§d[ALLmusic]§c错误，请输入歌曲数字ID"));
    }

    public void execute(CommandSender sender, String[] args) {
        String name = sender.getName();
        if (args.length == 0) {
            sender.sendMessage(new TextComponent("§d[ALLMusic]§c错误，请使用/music help 获取帮助"));
        } else if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(new TextComponent("§d[ALLMusic]§2帮助手册"));
            sender.sendMessage(new TextComponent("§d[ALLMusic]§2使用/music [音乐ID] 来点歌"));
            sender.sendMessage(new TextComponent("§d[ALLMusic]§2使用/music stop 停止播放歌曲"));
            sender.sendMessage(new TextComponent("§d[ALLMusic]§2使用/music list 查看歌曲队列"));
            sender.sendMessage(new TextComponent("§d[ALLmusic]§2使用/music vote 投票切歌"));
            sender.sendMessage(new TextComponent("§d[ALLMusic]§2使用/music nomusic 不再参与点歌"));
        } else if (args[0].equalsIgnoreCase("stop")) {
            ALLMusic.Side.Send("[Stop]", name, false);
            sender.sendMessage(new TextComponent("§d[ALLMusic]§2已停止" + name + "的音乐播放"));
        } else if (args[0].equalsIgnoreCase("list")) {
            if (PlayMusic.NowPlayMusic == null) {
                sender.sendMessage(new TextComponent("§d[ALLMusic]§2无正在播放的歌曲"));
            } else {
                sender.sendMessage(new TextComponent("§d[ALLMusic]§2正在播放：" + PlayMusic.NowPlayMusic));
            }
            if (PlayMusic.PlayList.size() == 0) {
                sender.sendMessage(new TextComponent("§d[ALLMusic]§2队列中无歌曲"));
            } else {
                sender.sendMessage(new TextComponent("§d[ALLMusic]§2队列中有歌曲数：" + PlayMusic.PlayList.size()));
                for (int now = 0; now < PlayMusic.PlayList.size(); now++) {
                    sender.sendMessage(new TextComponent("§d[ALLMusic]§2当前队列" + now + "->" + PlayMusic.PlayList.get(now)));
                }
            }
        } else if (args[0].equalsIgnoreCase("vote")) {
            if (PlayMusic.PlayList.size() == 0) {
                sender.sendMessage(new TextComponent("§d[ALLMusic]§2队列中无歌曲"));
            } else if (PlayMusic.Vote_time == 0) {
                PlayMusic.Vote_time = 30;
                PlayMusic.Vote.add(name);
                sender.sendMessage(new TextComponent("§d[ALLMusic]§2已发起切歌投票"));
                ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLMusic]§2" + name +
                        "发起了切歌投票，30秒后结束，输入/music vote 同意切歌。"));
            } else if (PlayMusic.Vote_time > 0) {
                if (!PlayMusic.Vote.contains(name)) {
                    PlayMusic.Vote.add(name);
                    ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLMusic]§2" + name + "同意切歌，共有" +
                            PlayMusic.Vote.size() + "名玩家同意切歌。"));
                } else {
                    ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLMusic]§2你已申请切歌"));
                }
            }
        } else if (args[0].equalsIgnoreCase("reload")) {
            ALLMusicBC music = new ALLMusicBC();
            music.setConfig();
            sender.sendMessage(new TextComponent("§d[ALLMusic]§2已重读配置文件"));
        } else if (args[0].equalsIgnoreCase("v")) {
            if (args.length == 2) {
                ALLMusic.Side.Send("[V]" + args[1], sender.getName(), null);
                sender.sendMessage(new TextComponent("§d[ALLMusic]§2已设置你的音量为：" + args[1]));
            } else
                sender.sendMessage(new TextComponent("§d[ALLMusic]§4请输入音量"));
        } else if (args[0].equalsIgnoreCase("next") && ALLMusic.Config.getAdmin().contains(name)) {
            PlayMusic.MusicAllTime = 1;
            sender.sendMessage(new TextComponent("§d[ALLMusic]§2已强制切歌"));
        } else if (args[0].equalsIgnoreCase("nomusic")) {
            ALLMusic.Side.Send("[Stop]", name, false);
            ALLMusic.Config.AddNoMusicPlayer(sender.getName());
            sender.sendMessage(new TextComponent("§d[ALLMusic]§2你不会再收到点歌了！想要再次参与点歌就点一首歌吧！"));
        } else if (args[0].equalsIgnoreCase("ban") && args.length == 2
                && ALLMusic.Config.getAdmin().contains(name)) {
            if (Function.isInteger(args[1])) {
                ALLMusic.Config.addBanID(args[1]);
                sender.sendMessage(new TextComponent("§d[ALLMusic]§2已禁止" + args[1]));
            } else {
                sender.sendMessage(new TextComponent("§d[ALLMusic]§2请输入有效的ID"));
            }
        } else if (args[0].equalsIgnoreCase("delete") && args.length == 2
                && ALLMusic.Config.getAdmin().contains(name)) {
            if (Function.isInteger(args[1])) {
                String music = args[1];
                if (PlayMusic.PlayList.contains(music)) {
                    PlayMusic.PlayList.remove(music);
                    sender.sendMessage(new TextComponent("§d[ALLMusic]§2已删除" + music));
                } else {
                    sender.sendMessage(new TextComponent("§d[ALLMusic]§2找不到" + music));
                }
            } else {
                sender.sendMessage(new TextComponent("§d[ALLMusic]§2请输入有效的ID"));
            }
        } else
            AddMusic(sender, args);
    }

    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 0) {
            arguments.add("stop");
            arguments.add("list");
            arguments.add("vote");
            arguments.add("nomusic");
            if (ALLMusic.Config.getAdmin().contains(sender.getName())) {
                arguments.add("reload");
                arguments.add("next");
                arguments.add("ban");
                arguments.add("delete");
            }
        }
        return arguments;
    }
}
