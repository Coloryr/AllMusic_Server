package Color_yr.ALLmusic_BC;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.regex.Pattern;

public class command extends Command {

	public command() {
		super("music");
	}

	public boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}


	public void execute(CommandSender sender, String[] args) {
		if (args[0].equalsIgnoreCase("help")) {
			sender.sendMessage(new TextComponent("§d[ALLmusic_BC]§2帮助手册"));
			sender.sendMessage(new TextComponent("§d[ALLmusic_BC]§2使用/music play [音乐ID] 来点歌"));
			sender.sendMessage(new TextComponent("§d[ALLmusic_BC]§2使用/music stop 停止播放歌曲"));
			sender.sendMessage(new TextComponent("§d[ALLmusic_BC]§2使用/music start 开始播放歌曲"));
			sender.sendMessage(new TextComponent("§d[ALLmusic_BC]§2使用/music vote 投票切歌"));
			return;
		}
		if (args[0].equalsIgnoreCase("play")) {
			if(args.length<1){
				sender.sendMessage(new TextComponent("§d[ALLmusic_BC]§c错误，请输入歌曲ID"));
				return;
			}
			else {
				if (isInteger(args[1])==true) {
					ChannelListener.sendToBukkit("ALLmusic", args[1]);
					sender.sendMessage(new TextComponent("§d[ALLmusic_BC]§2已发送" + args[1]));
				} else
					sender.sendMessage(new TextComponent("§d[ALLmusic_BC]§c错误，请输入歌曲数字ID"));
				return;
			}
		} else {
			sender.sendMessage(new TextComponent("§d[ALLmusic_BC]§c错误，请使用/music help 获取帮助"));
			return;
		}
	}
}
