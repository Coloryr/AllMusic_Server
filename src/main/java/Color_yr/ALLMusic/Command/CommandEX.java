package Color_yr.ALLMusic.Command;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Play.PlayMusic;
import Color_yr.ALLMusic.PlayList.GetList;
import Color_yr.ALLMusic.Search.PlayerSearch;
import Color_yr.ALLMusic.Search.Search;
import Color_yr.ALLMusic.Utils.Function;

public class CommandEX {

    private static void AddMusic(Object sender, String Name, String[] args) {
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
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§c错误，队列已满");
            } else if (ALLMusic.Config.getBanMusic().contains(MusicID)) {
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§c错误，这首歌被禁点了");
            } else if (PlayMusic.isHave(MusicID)) {
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§c错误，这首歌已经存在了");
            } else {
                ALLMusic.Config.RemoveNoMusicPlayer(Name);
                if (ALLMusic.Side.NeedPlay()) {
                    PlayMusic.AddMusic(MusicID, Name);
                    if (PlayMusic.isList && ALLMusic.Config.isPlayListSwitch()) {
                        PlayMusic.MusicAllTime = 1;
                        PlayMusic.isList = false;
                    }
                    ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2点歌成功");
                } else
                    ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§c没有播放的玩家");
            }
        } else
            ALLMusic.Side.SendMessage(sender, "§d[ALLmusic]§c错误，请输入歌曲数字ID");
    }

    public static void EX(Object sender, String Name, String[] args) {
        if (args.length == 0) {
            ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§c错误，请使用/music help 获取帮助");
        } else if (args[0].equalsIgnoreCase("help")) {
            ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2帮助手册");
            ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2使用/music [音乐ID] 来点歌");
            ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2使用/music stop 停止播放歌曲");
            ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2使用/music list 查看歌曲队列");
            ALLMusic.Side.SendMessage(sender, "§d[ALLmusic]§2使用/music vote 投票切歌");
            ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2使用/music nomusic 不再参与点歌");
            ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2使用/music v [音量] 调节音量");
            ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2使用/music search [歌名] 搜索歌曲");
            ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2使用/music select [序列] 选择歌曲");
            if (ALLMusic.VV != null) {
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2使用/music vv enable 启用关闭VV");
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2使用/music vv [位置] [坐标] [数值] 设置VV位置");
            }
        } else if (args[0].equalsIgnoreCase("stop")) {
            ALLMusic.Side.Send("[Stop]", Name, false);
            ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2已停止" + Name + "的音乐播放");
        } else if (args[0].equalsIgnoreCase("list")) {
            if (PlayMusic.NowPlayMusic == null) {
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2无正在播放的歌曲");
            } else {
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2正在播放：" + PlayMusic.NowPlayMusic.getInfo());
            }
            if (PlayMusic.PlayList.size() == 0) {
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2队列中无歌曲");
            } else {
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2队列中有歌曲数：" + PlayMusic.PlayList.size());
                ALLMusic.Side.SendMessage(sender, PlayMusic.getList());
            }
        } else if (args[0].equalsIgnoreCase("vote")) {
            if (PlayMusic.PlayList.size() == 0) {
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2队列中无歌曲");
            } else if (PlayMusic.Vote_time == 0) {
                PlayMusic.Vote_time = 30;
                PlayMusic.Vote.add(Name);
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2已发起切歌投票");
                ALLMusic.Side.bq("§d[ALLMusic]§2" + Name +
                        "发起了切歌投票，30秒后结束，输入/music vote 同意切歌。");
            } else if (PlayMusic.Vote_time > 0) {
                if (!PlayMusic.Vote.contains(Name)) {
                    PlayMusic.Vote.add(Name);
                    ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2你已同意");
                    ALLMusic.Side.bq("§d[ALLMusic]§2" + Name + "同意切歌，共有" +
                            PlayMusic.Vote.size() + "名玩家同意切歌。");
                } else {
                    ALLMusic.Side.bq("§d[ALLMusic]§2你已申请切歌");
                }
            }
        } else if (args[0].equalsIgnoreCase("reload")) {
            ALLMusic.Side.reload();
            ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2已重读配置文件");
        } else if (args[0].equalsIgnoreCase("v")) {
            if (args.length == 2) {
                ALLMusic.Side.Send("[V]" + args[1], Name, null);
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2已设置你的音量为：" + args[1]);
            } else
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§4请输入音量");
        } else if (args[0].equalsIgnoreCase("next") && ALLMusic.Config.getAdmin().contains(Name)) {
            PlayMusic.MusicAllTime = 1;
            ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2已强制切歌");
        } else if (args[0].equalsIgnoreCase("nomusic")) {
            ALLMusic.Side.Send("[Stop]", Name, false);
            ALLMusic.Config.AddNoMusicPlayer(Name);
            ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2你不会再收到点歌了！想要再次参与点歌就点一首歌吧！");
        } else if (args[0].equalsIgnoreCase("ban") && args.length == 2
                && ALLMusic.Config.getAdmin().contains(Name)) {
            if (Function.isInteger(args[1])) {
                ALLMusic.Config.addBanID(args[1]);
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2已禁止" + args[1]);
            } else {
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2请输入有效的ID");
            }
        } else if (args[0].equalsIgnoreCase("delete") && args.length == 2
                && ALLMusic.Config.getAdmin().contains(Name)) {
            if (Function.isInteger(args[1])) {
                int music = Integer.parseInt(args[1]);
                PlayMusic.PlayList.remove(music);
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2已删除序列" + music);
            } else {
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2请输入有效的序列ID");
            }
        } else if (args[0].equalsIgnoreCase("addlist") && args.length == 2
                && ALLMusic.Config.getAdmin().contains(Name)) {
            if (Function.isInteger(args[1])) {
                GetList.GetL(args[1]);
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2添加音乐列表" + args[1]);
            } else {
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2请输入有效的音乐列表ID");
            }
        } else if (args[0].equalsIgnoreCase("search") && args.length >= 2) {
            Search search = new Search(args);
            if (!search.isDone())
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§c无法搜索歌曲：" + args[1]);
            else {
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2搜索结果");
                ALLMusic.Side.SendMessage(sender, "§2" + search.GetInfo());
                PlayerSearch.SearchSave.put(Name, search);
            }
        } else if (args[0].equalsIgnoreCase("select") && args.length == 2) {
            Search obj = PlayerSearch.SearchSave.get(Name);
            if (obj == null) {
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§c你没有搜索音乐");
            } else if (!args[1].isEmpty() && Function.isInteger(args[1])) {
                int a = Integer.parseInt(args[1]);
                if (a == 0) {
                    ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§c请输入正确的序号");
                    return;
                }
                String[] ID = new String[1];
                ID[0] = obj.GetSong(a - 1);
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2你选择了序号" + a);
                AddMusic(sender, Name, ID);
                PlayerSearch.SearchSave.remove(Name);
            } else {
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§c请输入正确的序号");
            }
        } else if (ALLMusic.VV != null && args[0].equalsIgnoreCase("vv")) {
            if (args[1].equalsIgnoreCase("enable")) {
                ALLMusic.VV.SetEnable(Name);
            } else if (args.length != 4) {
                ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§c参数错误，请输入/music help获取帮助");
            } else {
                ALLMusic.Side.RunTask(() -> {
                    try {
                        if (!ALLMusic.VV.SetPot(Name, args[1], args[2], args[3]))
                            ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§c参数错误，请输入/music help获取帮助");
                        else
                            ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§2已设置");
                    } catch (Exception e) {
                        ALLMusic.Side.SendMessage(sender, "§d[ALLMusic]§c参数错误，请输入/music help获取帮助");
                    }
                });
            }
        } else
            AddMusic(sender, Name, args);
    }
}
