package coloryr.allmusic.command;

import coloryr.allmusic.AllMusic;
import coloryr.allmusic.hud.HudUtils;
import coloryr.allmusic.music.play.MusicSearch;
import coloryr.allmusic.music.play.PlayMusic;
import coloryr.allmusic.objs.SearchMusicObj;
import coloryr.allmusic.objs.hud.PosOBJ;
import coloryr.allmusic.objs.music.MusicObj;
import coloryr.allmusic.objs.music.SearchPageObj;
import coloryr.allmusic.utils.Function;

import java.util.Locale;

public class CommandEX {
    /**
     * 搜索音乐
     *
     * @param sender    发送者
     * @param name      用户名
     * @param args      参数
     * @param isDefault 是否是默认点歌方式
     */
    private static void searchMusic(Object sender, String name, String[] args, boolean isDefault) {
        MusicObj obj = new MusicObj();
        obj.sender = sender;
        obj.name = name;
        obj.args = args;
        obj.isDefault = isDefault;

        if (AllMusic.side.onMusicAdd(sender, obj)) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().AddMusic.Cancel);
            return;
        }

        MusicSearch.addSearch(obj);
    }

    /**
     * 检查金额是否够
     *
     * @param sender 发送者
     * @param name   用户名
     * @param cost   金额
     * @return 结果
     */
    private static boolean checkMoney(Object sender, String name, int cost) {
        if (!AllMusic.getConfig().UseCost || AllMusic.economy == null) {
            return false;
        }

        if (!AllMusic.economy.check(name, cost)) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Cost.NoMoney);
            return true;
        }
        return false;
    }

    /**
     * 扣钱
     *
     * @param sender  发送者
     * @param name    用户名
     * @param cost    金额
     * @param message 结果消息
     * @return 结果
     */
    private static boolean cost(Object sender, String name, int cost, String message) {
        if (!AllMusic.getConfig().UseCost || AllMusic.economy == null) {
            return false;
        }

        if (AllMusic.economy.cost(name, cost)) {
            AllMusic.side.sendMessage(sender, message
                    .replace("%Cost%", "" + cost));
            return false;
        } else {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Cost.CostFail);
            return true;
        }
    }

    /**
     * 添加音乐
     *
     * @param sender 发送者
     * @param name   用户名
     * @param args   参数
     */
    private static void addMusic(Object sender, String name, String[] args) {
        String musicID;
        if (args[0].contains("id=") && !args[0].contains("/?userid")) {
            if (args[0].contains("&user"))
                musicID = Function.getString(args[0], "id=", "&user");
            else
                musicID = Function.getString(args[0], "id=", null);
        } else if (args[0].contains("song/")) {
            if (args[0].contains("/?userid"))
                musicID = Function.getString(args[0], "song/", "/?userid");
            else
                musicID = Function.getString(args[0], "song/", null);
        } else
            musicID = args[0];
        if (Function.isInteger(musicID)) {
            if (PlayMusic.getSize() >= AllMusic.getConfig().MaxList) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().AddMusic.ListFull);
            } else if (AllMusic.getConfig().BanMusic.contains(musicID)) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().AddMusic.BanMusic);
            } else if (PlayMusic.isHave(musicID)) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().AddMusic.ExistMusic);
            } else {
                if (checkMoney(sender, name, AllMusic.getConfig().AddMusicCost)) {
                    return;
                }
                if (cost(sender, name, AllMusic.getConfig().AddMusicCost,
                        AllMusic.getMessage().Cost.AddMusic)) {
                    return;
                }
                AllMusic.getConfig().RemoveNoMusicPlayer(name);
                if (AllMusic.side.needPlay()) {
                    MusicObj obj = new MusicObj();
                    obj.sender = sender;
                    obj.id = musicID;
                    obj.name = name;
                    obj.isDefault = false;

                    if (AllMusic.side.onMusicAdd(sender, obj)) {
                        AllMusic.side.sendMessage(sender, AllMusic.getMessage().AddMusic.Cancel);
                        return;
                    }

                    PlayMusic.addTask(obj);
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().AddMusic.Success);
                } else
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().AddMusic.NoPlayer);
            }
        } else
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().AddMusic.NoID);
    }

    /**
     * 展示搜歌结果
     *
     * @param sender 发送者
     * @param search 搜歌结果
     */
    public static void showSearch(Object sender, SearchPageObj search) {
        int index = search.getIndex();
        SearchMusicObj item;
        String info;
        AllMusic.side.sendMessage(sender, "");
        if (search.haveLastPage()) {
            AllMusic.side.sendMessageRun(sender, "§d[AllMusic]§2输入/music lastpage上一页",
                    AllMusic.getMessage().Page.Last, "/music lastpage");
        }
        for (int a = 0; a < index; a++) {
            item = search.getRes(a + search.getPage() * 10);
            info = AllMusic.getMessage().Page.Choice;
            info = info.replace("%index%", "" + (a + 1))
                    .replace("%MusicName%", item.name)
                    .replace("%MusicAuthor%", item.author)
                    .replace("%MusicAl%", item.al);
            AllMusic.side.sendMessageRun(sender, info,
                    AllMusic.getMessage().Click.This, "/music select " + (a + 1));
        }
        if (search.haveNextPage()) {
            AllMusic.side.sendMessageRun(sender, "§d[AllMusic]§2输入/music nextpage下一页",
                    AllMusic.getMessage().Page.Next, "/music nextpage");
        }
        AllMusic.side.sendMessage(sender, "");
    }

    /**
     * 执行命令
     *
     * @param sender 发送者
     * @param name   用户名
     * @param args   参数
     */
    public static void ex(Object sender, String name, String[] args) {
        if (args.length == 0) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
            return;
        } else if (args[0].equalsIgnoreCase("help")) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Help.Normal.Head);
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Normal.Base,
                    AllMusic.getMessage().Click.Check, "/music ");
            AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().Help.Normal.Stop,
                    AllMusic.getMessage().Click.This, "/music stop");
            AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().Help.Normal.List,
                    AllMusic.getMessage().Click.This, "/music list");
            AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().Help.Normal.Vote,
                    AllMusic.getMessage().Click.This, "/music vote");
            AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().Help.Normal.NoMusic,
                    AllMusic.getMessage().Click.This, "/music nomusic");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Normal.Search,
                    AllMusic.getMessage().Click.Check, "/music search ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Normal.Select,
                    AllMusic.getMessage().Click.Check, "/music select ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Normal.Hud1,
                    AllMusic.getMessage().Click.Check, "/music hud enable ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Normal.Hud2,
                    AllMusic.getMessage().Click.Check, "/music hud ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Normal.Hud3,
                    AllMusic.getMessage().Click.Check, "/music hud picsize ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Normal.Hud4,
                    AllMusic.getMessage().Click.Check, "/music hud picrotate ");
            AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Normal.Hud5,
                    AllMusic.getMessage().Click.Check, "/music hud picrotatespeed ");
            if (AllMusic.getConfig().Admin.contains(name)) {
                AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().Help.Admin.Reload,
                        AllMusic.getMessage().Click.This, "/music reload");
                AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().Help.Admin.Next,
                        AllMusic.getMessage().Click.This, "/music next");
                AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Admin.Ban,
                        AllMusic.getMessage().Click.Check, "/music ban ");
                AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Admin.Url,
                        AllMusic.getMessage().Click.Check, "/music url ");
                AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Admin.Delete,
                        AllMusic.getMessage().Click.Check, "/music delete ");
                AllMusic.side.sendMessageSuggest(sender, AllMusic.getMessage().Help.Admin.AddList,
                        AllMusic.getMessage().Click.Check, "/music addlist ");
                AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().Help.Admin.ClearList,
                        AllMusic.getMessage().Click.This, "/music clearlist");
                AllMusic.side.sendMessageRun(sender, AllMusic.getMessage().Help.Admin.Login,
                        AllMusic.getMessage().Click.This, "/music login");
            }
            return;
        } else if (args[0].equalsIgnoreCase("stop")) {
            AllMusic.side.clearHud(name);
            AllMusic.side.sendStop(name);
            HudUtils.clearHud(name);
            AllMusic.removeNowPlayPlayer(name);
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().MusicPlay.StopPlay);
            return;
        } else if (args[0].equalsIgnoreCase("list")) {
            if (PlayMusic.nowPlayMusic == null || PlayMusic.nowPlayMusic.isNull()) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().MusicPlay.NoMusic);
            } else {
                String info = AllMusic.getMessage().MusicPlay.Play;
                info = info.replace("%MusicName%", PlayMusic.nowPlayMusic.getName())
                        .replace("%MusicAuthor%", PlayMusic.nowPlayMusic.getAuthor())
                        .replace("%MusicAl%", PlayMusic.nowPlayMusic.getAl())
                        .replace("%MusicAlia%", PlayMusic.nowPlayMusic.getAlia())
                        .replace("%PlayerName%", PlayMusic.nowPlayMusic.getCall());
                AllMusic.side.sendMessage(sender, info);
            }
            if (PlayMusic.getSize() == 0) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().MusicPlay.NoPlay);
            } else {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().MusicPlay.ListMusic.Head
                        .replace("&Count&", "" + PlayMusic.getSize()));
                AllMusic.side.sendMessage(sender, PlayMusic.getAllList());
            }
            return;
        } else if (args[0].equalsIgnoreCase("vote")) {
            if (AllMusic.getConfig().NeedPermission &&
                    AllMusic.side.checkPermission(name, "allmusic.vote")) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Vote.NoPermission);
                return;
            }
            if (PlayMusic.getSize() == 0 && AllMusic.getConfig().PlayList.size() == 0) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().MusicPlay.NoPlay);
            } else if (PlayMusic.voteTime == 0) {
                PlayMusic.voteTime = 30;
                AllMusic.addVote(name);
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Vote.DoVote);
                String data = AllMusic.getMessage().Vote.BQ;
                AllMusic.side.bq(data.replace("%PlayerName%", name));
            } else if (PlayMusic.voteTime > 0) {
                if (!AllMusic.containVote(name)) {
                    AllMusic.addVote(name);
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().Vote.Agree);
                    String data = AllMusic.getMessage().Vote.BQAgree;
                    data = data.replace("%PlayerName%", name)
                            .replace("%Count%", "" + AllMusic.getVoteCount());
                    AllMusic.side.bq(data);
                } else {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().Vote.ARAgree);
                }
            }
            AllMusic.getConfig().RemoveNoMusicPlayer(name);
            return;
        } else if (args[0].equalsIgnoreCase("nomusic")) {
            AllMusic.side.sendStop(name);
            AllMusic.side.clearHud(name);
            AllMusic.getConfig().AddNoMusicPlayer(name);
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().MusicPlay.NoPlayMusic);
            return;
        } else if (args[0].equalsIgnoreCase("search") && args.length >= 2) {
            if (AllMusic.getConfig().NeedPermission &&
                    AllMusic.side.checkPermission(name, "allmusic.search")) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.NoPer);
                return;
            }
            if (checkMoney(sender, name, AllMusic.getConfig().SearchCost)) {
                return;
            }
            if (cost(sender, name, AllMusic.getConfig().SearchCost,
                    AllMusic.getMessage().Cost.Search)) {
                return;
            }

            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.StartSearch);
            searchMusic(sender, name, args, false);
            return;
        } else if (args[0].equalsIgnoreCase("select") && args.length == 2) {
            if (AllMusic.getConfig().NeedPermission &&
                    AllMusic.side.checkPermission(name, "allmusic.search")) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.NoPer);
                return;
            }
            SearchPageObj obj = AllMusic.getSearch(name);
            if (obj == null) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.NoSearch);
            } else if (!args[1].isEmpty() && Function.isInteger(args[1])) {
                int a = Integer.parseInt(args[1]);
                if (a == 0) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.ErrorNum);
                    return;
                }
                String[] ID = new String[1];
                ID[0] = obj.getSong((obj.getPage() * 10) + (a - 1));
                AllMusic.side.sendMessage(sender,
                        AllMusic.getMessage().Search.Chose.replace("%Num%", "" + a));
                addMusic(sender, name, ID);
                AllMusic.removeSearch(name);
            } else {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.ErrorNum);
            }
            return;
        } else if (args[0].equalsIgnoreCase("nextpage")) {
            if (AllMusic.getConfig().NeedPermission &&
                    AllMusic.side.checkPermission(name, "allmusic.search")) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.NoPer);
                return;
            }
            SearchPageObj obj = AllMusic.getSearch(name);
            if (obj == null) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.NoSearch);
            } else if (obj.nextPage()) {
                showSearch(sender, obj);
            } else {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.CantNext);
            }
            return;
        } else if (args[0].equalsIgnoreCase("lastpage")) {
            if (AllMusic.getConfig().NeedPermission &&
                    AllMusic.side.checkPermission(name, "allmusic.search")) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.NoPer);
                return;
            }
            SearchPageObj obj = AllMusic.getSearch(name);
            if (obj == null) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.NoSearch);
            } else if (obj.lastPage()) {
                showSearch(sender, obj);
            } else {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Search.CantLast);
            }
            return;
        } else if (args[0].equalsIgnoreCase("hud")) {
            if (args.length == 1) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
            } else {
                if (args[1].equalsIgnoreCase("enable")) {
                    if (args.length == 3) {
                        try {
                            String pos = args[2].toLowerCase(Locale.ROOT);
                            boolean temp = HudUtils.setHudEnable(name, pos);
                            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Hud.State
                                    .replace("%State%", temp ? "启用" : "关闭")
                                    .replace("%Hud%", AllMusic.getMessage().HudList.Get(pos)));
                        } catch (Exception e) {
                            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
                        }
                    } else {
                        boolean temp = HudUtils.setHudEnable(name, null);
                        AllMusic.side.sendMessage(sender, AllMusic.getMessage().Hud.State
                                .replace("%State%", temp ? "启用" : "关闭")
                                .replace("%Hud%", AllMusic.getMessage().HudList.All));
                    }
                } else if (args[1].equalsIgnoreCase("reset")) {
                    HudUtils.reset(name);
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().Hud.Reset);
                } else if (args[1].equalsIgnoreCase("picsize")) {
                    if (args.length != 3 || !HudUtils.setPicSize(name, args[2])) {
                        AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
                        return;
                    }
                    AllMusic.side.sendMessage(sender,
                            AllMusic.getMessage().Hud.PicSize.replace("%Size%", args[2]));
                } else if (args[1].equalsIgnoreCase("picrotate")) {
                    if (args.length != 3) {
                        AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
                        return;
                    }
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().Hud.PicRotate
                            .replace("%State%", String.valueOf(HudUtils.setPicRotate(name, args[2]))));
                } else if (args[1].equalsIgnoreCase("picrotatespeed")) {
                    if (args.length != 3 || !HudUtils.setPicRotateSpeed(name, args[2])) {
                        AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
                        return;
                    }
                    AllMusic.side.sendMessage(sender,
                            AllMusic.getMessage().Hud.PicRotate.replace("%Size%", args[2]));
                } else if (args.length != 4) {
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
                } else {
                    try {
                        PosOBJ obj = HudUtils.setHudPos(name, args[1], args[2], args[3]);
                        if (obj == null) {
                            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
                        } else {
                            String temp = AllMusic.getMessage().Hud.Set
                                    .replace("%Hud%", args[1])
                                    .replace("%x%", args[2])
                                    .replace("%y%", args[3]);
                            AllMusic.side.sendMessage(sender, temp);
                        }
                    } catch (Exception e) {
                        AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.Error);
                    }
                }
            }
            return;
        } else if (args[0].equalsIgnoreCase("reload")) {
            AllMusic.side.reload();
            AllMusic.side.sendMessage(sender, "§d[AllMusic]§2已重读配置文件");
            return;
        } else if (AllMusic.getConfig().Admin.contains(name)) {
            if (args[0].equalsIgnoreCase("next")) {
                PlayMusic.musicLessTime = 1;
                AllMusic.side.sendMessage(sender, "§d[AllMusic]§2已强制切歌");
                AllMusic.getConfig().RemoveNoMusicPlayer(name);
                return;
            } else if (args[0].equalsIgnoreCase("ban") && args.length == 2) {
                if (Function.isInteger(args[1])) {
                    AllMusic.getConfig().addBanID(args[1]);
                    AllMusic.side.sendMessage(sender, "§d[AllMusic]§2已禁止" + args[1]);
                } else {
                    AllMusic.side.sendMessage(sender, "§d[AllMusic]§2请输入有效的ID");
                }
                return;
            } else if (args[0].equalsIgnoreCase("url") && args.length == 2) {
                MusicObj obj = new MusicObj();
                obj.sender = sender;
                obj.isUrl = true;
                obj.url = args[1];
                PlayMusic.addTask(obj);
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().AddMusic.Success);
                return;
            } else if (args[0].equalsIgnoreCase("delete") && args.length == 2) {
                if (!args[1].isEmpty() && Function.isInteger(args[1])) {
                    int music = Integer.parseInt(args[1]);
                    if (music == 0) {
                        AllMusic.side.sendMessage(sender, "§d[AllMusic]§2请输入有效的序列ID");
                        return;
                    }
                    if (music > PlayMusic.getSize()) {
                        AllMusic.side.sendMessage(sender, "§d[AllMusic]§2序列号过大");
                        return;
                    }
                    PlayMusic.remove(music - 1);
                    AllMusic.side.sendMessage(sender, "§d[AllMusic]§2已删除序列" + music);
                } else {
                    AllMusic.side.sendMessage(sender, "§d[AllMusic]§2请输入有效的序列ID");
                }
                return;
            } else if (args[0].equalsIgnoreCase("addlist") && args.length == 2) {
                if (Function.isInteger(args[1])) {
                    AllMusic.getMusicApi().setList(args[1], sender);
                    AllMusic.side.sendMessage(sender, "§d[AllMusic]§2添加空闲音乐列表" + args[1]);
                } else {
                    AllMusic.side.sendMessage(sender, "§d[AllMusic]§2请输入有效的音乐列表ID");
                }
                return;
            } else if (args[0].equalsIgnoreCase("clearlist")) {
                AllMusic.getConfig().PlayList.clear();
                AllMusic.save();
                AllMusic.side.sendMessage(sender, "§d[AllMusic]§2添加空闲音乐列表已清空");
                return;
            } else if (args[0].equalsIgnoreCase("code")) {
                AllMusic.getMusicApi().sendCode(sender);
                return;
            } else if (args[0].equalsIgnoreCase("login")) {
                if (args.length != 2) {
                    AllMusic.side.sendMessage(sender, "§d[AllMusic]§c没有手机验证码");
                    return;
                }
                AllMusic.side.sendMessage(sender, "§d[AllMusic]§d重新登录网易云账户");
                AllMusic.getMusicApi().login(sender, args[1]);
                return;
            }
        }
        if (AllMusic.getConfig().NeedPermission &&
                AllMusic.side.checkPermission(name, "allmusic.addmusic"))
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().Command.NoPer);
        else {
            switch (AllMusic.getConfig().DefaultAddMusic) {
                case 1:
                    searchMusic(sender, name, args, true);
                    break;
                case 0:
                default:
                    addMusic(sender, name, args);
            }
        }
    }
}
