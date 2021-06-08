package Color_yr.AllMusic.Command;

import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.MusicAPI.SongSearch.SearchOBJ;
import Color_yr.AllMusic.MusicAPI.SongSearch.SearchPage;
import Color_yr.AllMusic.MusicPlay.PlayMusic;
import Color_yr.AllMusic.MusicPlay.SendHud.HudUtils;
import Color_yr.AllMusic.MusicPlay.SendHud.PosOBJ;
import Color_yr.AllMusic.MusicPlay.MusicSearch;
import Color_yr.AllMusic.MusicPlay.MusicObj;
import Color_yr.AllMusic.Utils.Function;

public class CommandEX {
    private static void SearchMusic(Object sender, String Name, String[] args, boolean isDefault) {
        MusicObj obj = new MusicObj();
        obj.sender = sender;
        obj.Name = Name;
        obj.args = args;
        obj.isDefault = isDefault;
        MusicSearch.addSearch(obj);
    }

    private static void AddMusic(Object sender, String Name, String[] args) {
        String MusicID;
        if (args[0].contains("id=") && !args[0].contains("/?userid")) {
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
            if (PlayMusic.getSize() >= AllMusic.getConfig().getMaxList()) {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getAddMusic().getListFull());
            } else if (AllMusic.getConfig().getBanMusic().contains(MusicID)) {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getAddMusic().getBanMusic());
            } else if (PlayMusic.isHave(MusicID)) {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getAddMusic().getExistMusic());
            } else {
                if (AllMusic.getConfig().isUseCost() && AllMusic.Vault != null) {
                    if (!AllMusic.Vault.check(Name, AllMusic.getConfig().getAddMusicCost())) {
                        AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getCost().getNoMoney());
                        return;
                    }
                }
                AllMusic.getConfig().RemoveNoMusicPlayer(Name);
                if (AllMusic.Side.NeedPlay()) {
                    MusicObj obj = new MusicObj();
                    obj.sender = MusicID;
                    obj.Name = Name;
                    obj.isDefault = false;
                    PlayMusic.addTask(obj);
                    AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getAddMusic().getSuccess());
                    if (AllMusic.getConfig().isUseCost() && AllMusic.Vault != null) {
                        AllMusic.Vault.cost(Name, AllMusic.getConfig().getAddMusicCost(),
                                AllMusic.getMessage().getCost().getAddMusic()
                                        .replace("%Cost%", "" + AllMusic.getConfig().getAddMusicCost()));
                    }
                } else
                    AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getAddMusic().getNoPlayer());
            }
        } else
            AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getAddMusic().getNoID());
    }

    public static void ShowSearch(Object sender, SearchPage search) {
        int index = search.getIndex();
        SearchOBJ item;
        String info;
        AllMusic.Side.SendMessage(sender, "");
        if (search.haveLastPage()) {
            AllMusic.Side.SendMessageRun(sender, "§d[AllMusic]§2输入/music lastpage上一页",
                    AllMusic.getMessage().getPage().getLast(), "/music lastpage");
        }
        for (int a = 0; a < index; a++) {
            item = search.getRes(a + search.getPage() * 10);
            info = AllMusic.getMessage().getPage().getChoice();
            info = info.replace("%index%", "" + (a + 1))
                    .replace("%MusicName%", item.getName())
                    .replace("%MusicAuthor%", item.getAuthor())
                    .replace("%MusicAl%", item.getAl());
            AllMusic.Side.SendMessageRun(sender, info,
                    AllMusic.getMessage().getClick().This, "/music select " + (a + 1));
        }
        if (search.haveNextPage()) {
            AllMusic.Side.SendMessageRun(sender, "§d[AllMusic]§2输入/music nextpage下一页",
                    AllMusic.getMessage().getPage().getNext(), "/music nextpage");
        }
        AllMusic.Side.SendMessage(sender, "");
    }

    public static void EX(Object sender, String Name, String[] args) {
        if (args.length == 0) {
            AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getCommand().getError());
        } else if (args[0].equalsIgnoreCase("help")) {
            AllMusic.Side.SendMessage(sender, "§d[AllMusic]§2帮助手册");
            AllMusic.Side.SendMessageSuggest(sender, "§d[AllMusic]§2使用/music [音乐ID] 来点歌",
                    AllMusic.getMessage().getClick().Check, "/music ");
            AllMusic.Side.SendMessageRun(sender, "§d[AllMusic]§2使用/music stop 停止播放歌曲",
                    AllMusic.getMessage().getClick().This, "/music stop");
            AllMusic.Side.SendMessageRun(sender, "§d[AllMusic]§2使用/music list 查看歌曲队列",
                    AllMusic.getMessage().getClick().This, "/music list");
            AllMusic.Side.SendMessageRun(sender, "§d[AllMusic]§2使用/music vote 投票切歌",
                    AllMusic.getMessage().getClick().This, "/music vote");
            AllMusic.Side.SendMessageRun(sender, "§d[AllMusic]§2使用/music nomusic 不再参与点歌",
                    AllMusic.getMessage().getClick().This, "/music nomusic");
            AllMusic.Side.SendMessageSuggest(sender, "§d[AllMusic]§2使用/music search [歌名] 搜索歌曲",
                    AllMusic.getMessage().getClick().Check, "/music search ");
            AllMusic.Side.SendMessageSuggest(sender, "§d[AllMusic]§2使用/music select [序列] 选择歌曲",
                    AllMusic.getMessage().getClick().Check, "/music select ");
            AllMusic.Side.SendMessageSuggest(sender, "§d[AllMusic]§2使用/music hud enable [位置] 启用关闭Hud",
                    AllMusic.getMessage().getClick().Check, "/music hud enable ");
            AllMusic.Side.SendMessageSuggest(sender, "§d[AllMusic]§2使用/music hud [位置] [x] [y] 设置某个Hud的位置",
                    AllMusic.getMessage().getClick().Check, "/music hud ");
        } else if (args[0].equalsIgnoreCase("stop")) {
            AllMusic.Side.ClearHud(Name);
            AllMusic.Side.Send("[Stop]", Name, false);
            HudUtils.clearHud(Name);
            AllMusic.removeNowPlayPlayer(Name);
            AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getMusicPlay().getStopPlay());
        } else if (args[0].equalsIgnoreCase("list")) {
            if (PlayMusic.NowPlayMusic == null || PlayMusic.NowPlayMusic.isNull()) {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getMusicPlay().getNoMusic());
            } else {
                String info = AllMusic.getMessage().getMusicPlay().getPlay();
                info = info.replace("%MusicName%", PlayMusic.NowPlayMusic.getName())
                        .replace("%MusicAuthor%", PlayMusic.NowPlayMusic.getAuthor())
                        .replace("%MusicAl%", PlayMusic.NowPlayMusic.getAl())
                        .replace("%MusicAlia%", PlayMusic.NowPlayMusic.getAlia())
                        .replace("%PlayerName%", PlayMusic.NowPlayMusic.getCall());
                AllMusic.Side.SendMessage(sender, info);
            }
            if (PlayMusic.getSize() == 0) {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getMusicPlay().getNoPlay());
            } else {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getMusicPlay().getListMusic().getHead()
                        .replace("&Count&", "" + PlayMusic.getSize()));
                AllMusic.Side.SendMessage(sender, PlayMusic.getAllList());
            }
        } else if (args[0].equalsIgnoreCase("vote")) {
            if (AllMusic.getConfig().isNeedPermission() && AllMusic.Side.checkPermission(Name, "AllMusic.vote")) {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getVote().getNoPermission());
                return;
            }
            if (PlayMusic.getSize() == 0 && AllMusic.getConfig().getPlayList().size() == 0) {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getMusicPlay().getNoPlay());
            } else if (PlayMusic.VoteTime == 0) {
                PlayMusic.VoteTime = 30;
                AllMusic.addVote(Name);
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getVote().getDoVote());
                String data = AllMusic.getMessage().getVote().getBQ();
                AllMusic.Side.bq(data.replace("%PlayerName%", Name));
            } else if (PlayMusic.VoteTime > 0) {
                if (!AllMusic.containVote(Name)) {
                    AllMusic.addVote(Name);
                    AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getVote().getAgree());
                    String data = AllMusic.getMessage().getVote().getBQAgree();
                    data = data.replace("%PlayerName%", Name)
                            .replace("%Count%", "" + AllMusic.getVoteCount());
                    AllMusic.Side.bq(data);
                } else {
                    AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getVote().getARAgree());
                }
            }
            AllMusic.getConfig().RemoveNoMusicPlayer(Name);
        } else if (args[0].equalsIgnoreCase("reload")) {
            AllMusic.Side.reload();
            AllMusic.Side.SendMessage(sender, "§d[AllMusic]§2已重读配置文件");
        } else if (args[0].equalsIgnoreCase("next") && AllMusic.getConfig().getAdmin().contains(Name)) {
            PlayMusic.MusicLessTime = 1;
            AllMusic.Side.SendMessage(sender, "§d[AllMusic]§2已强制切歌");
            AllMusic.getConfig().RemoveNoMusicPlayer(Name);
        } else if (args[0].equalsIgnoreCase("nomusic")) {
            AllMusic.Side.Send("[Stop]", Name, false);
            AllMusic.Side.ClearHud(Name);
            AllMusic.getConfig().AddNoMusicPlayer(Name);
            AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getMusicPlay().getNoPlayMusic());
        } else if (args[0].equalsIgnoreCase("ban") && args.length == 2
                && AllMusic.getConfig().getAdmin().contains(Name)) {
            if (Function.isInteger(args[1])) {
                AllMusic.getConfig().addBanID(args[1]);
                AllMusic.Side.SendMessage(sender, "§d[AllMusic]§2已禁止" + args[1]);
            } else {
                AllMusic.Side.SendMessage(sender, "§d[AllMusic]§2请输入有效的ID");
            }
        } else if (args[0].equalsIgnoreCase("url") && args.length == 2
                && AllMusic.getConfig().getAdmin().contains(Name)) {
            MusicObj obj = new MusicObj();
            obj.isUrl = true;
            obj.url = args[1];
            PlayMusic.addTask(obj);
            AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getAddMusic().getSuccess());
        } else if (args[0].equalsIgnoreCase("delete") && args.length == 2
                && AllMusic.getConfig().getAdmin().contains(Name)) {
            if (!args[1].isEmpty() && Function.isInteger(args[1])) {
                int music = Integer.parseInt(args[1]);
                if (music == 0) {
                    AllMusic.Side.SendMessage(sender, "§d[AllMusic]§2请输入有效的序列ID");
                    return;
                }
                if (music > PlayMusic.getSize()) {
                    AllMusic.Side.SendMessage(sender, "§d[AllMusic]§2序列号过大");
                    return;
                }
                PlayMusic.remove(music - 1);
                AllMusic.Side.SendMessage(sender, "§d[AllMusic]§2已删除序列" + music);
            } else {
                AllMusic.Side.SendMessage(sender, "§d[AllMusic]§2请输入有效的序列ID");
            }
        } else if (args[0].equalsIgnoreCase("addlist") && args.length == 2
                && AllMusic.getConfig().getAdmin().contains(Name)) {
            if (Function.isInteger(args[1])) {
                AllMusic.getMusic().SetList(args[1], sender);
                AllMusic.Side.SendMessage(sender, "§d[AllMusic]§2添加空闲音乐列表" + args[1]);
            } else {
                AllMusic.Side.SendMessage(sender, "§d[AllMusic]§2请输入有效的音乐列表ID");
            }
        } else if (args[0].equalsIgnoreCase("clearlist")
                && AllMusic.getConfig().getAdmin().contains(Name)) {
            AllMusic.getConfig().getPlayList().clear();
            AllMusic.save();
            AllMusic.Side.SendMessage(sender, "§d[AllMusic]§2添加空闲音乐列表已清空");
        } else if (args[0].equalsIgnoreCase("search") && args.length >= 2) {
            if (AllMusic.getConfig().isNeedPermission() && AllMusic.Side.checkPermission(Name, "AllMusic.search")) {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getSearch().getNoPer());
                return;
            }
            if (AllMusic.getConfig().isUseCost() && AllMusic.Vault != null) {
                if (!AllMusic.Vault.check(Name, AllMusic.getConfig().getSearchCost())) {
                    AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getCost().getNoMoney());
                    return;
                }
                AllMusic.Vault.cost(Name, AllMusic.getConfig().getAddMusicCost(),
                        AllMusic.getMessage().getCost().getAddMusic()
                                .replace("%Cost%", "" + AllMusic.getConfig().getAddMusicCost()));
            }
            AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getSearch().getStartSearch());
            SearchMusic(sender, Name, args, false);
        } else if (args[0].equalsIgnoreCase("select") && args.length == 2) {
            if (AllMusic.getConfig().isNeedPermission() && AllMusic.Side.checkPermission(Name, "AllMusic.search")) {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getSearch().getNoPer());
                return;
            }
            SearchPage obj = AllMusic.getSearch(Name);
            if (obj == null) {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getSearch().getNoSearch());
            } else if (!args[1].isEmpty() && Function.isInteger(args[1])) {
                int a = Integer.parseInt(args[1]);
                if (a == 0) {
                    AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getSearch().getErrorNum());
                    return;
                }
                String[] ID = new String[1];
                ID[0] = obj.GetSong((obj.getPage() * 10) + (a - 1));
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getSearch().getChose().replace("%Num%", "" + a));
                AddMusic(sender, Name, ID);
                AllMusic.removeSearch(Name);
            } else {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getSearch().getErrorNum());
            }
        } else if (args[0].equalsIgnoreCase("nextpage")) {
            if (AllMusic.getConfig().isNeedPermission() && AllMusic.Side.checkPermission(Name, "AllMusic.search")) {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getSearch().getNoPer());
                return;
            }
            SearchPage obj = AllMusic.getSearch(Name);
            if (obj == null) {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getSearch().getNoSearch());
            } else if (obj.nextPage()) {
                ShowSearch(sender, obj);
            } else {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getSearch().getCantNext());
            }
        } else if (args[0].equalsIgnoreCase("lastpage")) {
            if (AllMusic.getConfig().isNeedPermission() && AllMusic.Side.checkPermission(Name, "AllMusic.search")) {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getSearch().getNoPer());
                return;
            }
            SearchPage obj = AllMusic.getSearch(Name);
            if (obj == null) {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getSearch().getNoSearch());
            } else if (obj.lastPage()) {
                ShowSearch(sender, obj);
            } else {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getSearch().getCantLast());
            }
        } else if (args[0].equalsIgnoreCase("hud")) {
            if (args.length == 1) {
                AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getCommand().getError());
            } else {
                if (args[1].equalsIgnoreCase("enable")) {
                    if (args.length == 3) {
                        try {
                            boolean temp = HudUtils.SetHudEnable(Name, args[2]);
                            AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getHud().getState()
                                    .replace("%State%", temp ? "启用" : "关闭")
                                    .replace("%Hud%", AllMusic.getMessage().getHudList().Get(args[2])));
                        } catch (Exception e) {
                            AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getCommand().getError());
                        }
                    } else {
                        boolean temp = HudUtils.SetHudEnable(Name, null);
                        AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getHud().getState()
                                .replace("%State%", temp ? "启用" : "关闭")
                                .replace("%Hud%", AllMusic.getMessage().getHudList().getAll()));
                    }
                } else if (args[1].equalsIgnoreCase("reset")) {
                    HudUtils.Reset(Name);
                    AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getHud().getReset());
                } else if (args.length != 4) {
                    AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getCommand().getError());
                } else {
                    try {
                        PosOBJ obj = HudUtils.SetHudPos(Name, args[1], args[2], args[3]);
                        if (obj == null) {
                            AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getCommand().getError());
                        } else {
                            String temp = AllMusic.getMessage().getHud().getSet()
                                    .replace("%Hud%", args[1])
                                    .replace("%x%", args[2])
                                    .replace("%y%", args[3]);
                            AllMusic.Side.SendMessage(sender, temp);
                        }
                    } catch (Exception e) {
                        AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getCommand().getError());
                    }
                }
            }
        } else if (args[0].equalsIgnoreCase("login")) {
            AllMusic.Side.SendMessage(sender, "§d[AllMusic]§d重新登录网易云账户");
            AllMusic.getMusic().login();
        } else if (AllMusic.getConfig().isNeedPermission() && AllMusic.Side.checkPermission(Name, "AllMusic.addmusic"))
            AllMusic.Side.SendMessage(sender, AllMusic.getMessage().getCommand().getNoPer());
        else {
            switch (AllMusic.getConfig().getDefaultAddMusic()) {
                case 1:
                    SearchMusic(sender, Name, args, true);
                    break;
                case 0:
                default:
                    AddMusic(sender, Name, args);
            }
        }
    }
}
