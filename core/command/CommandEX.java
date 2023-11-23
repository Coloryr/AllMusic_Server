package coloryr.allmusic.core.command;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.music.play.MusicSearch;
import coloryr.allmusic.core.music.play.PlayMusic;
import coloryr.allmusic.core.objs.SearchMusicObj;
import coloryr.allmusic.core.objs.music.MusicObj;
import coloryr.allmusic.core.objs.music.SearchPageObj;
import coloryr.allmusic.core.utils.Function;

import java.util.HashMap;
import java.util.Map;

public class CommandEX {

    private static final Map<String, ICommand> CommandList = new HashMap<>();
    private static final Map<String, ICommand> CommandAdminList = new HashMap<>();

    static {
        CommandList.put("stop", new CommandStop());
        CommandList.put("help", new CommandHelp());
        CommandList.put("list", new CommandList());
        CommandList.put("vote", new CommandVote());
        CommandList.put("nomusic", new CommandNoMusic());
        CommandList.put("search", new CommandSearch());
        CommandList.put("select", new CommandSelect());
        CommandList.put("nextpage", new CommandNextPage());
        CommandList.put("lastpage", new CommandLastPage());
        CommandList.put("hud", new CommandHud());
        CommandList.put("reload", new CommandReload());

        CommandAdminList.put("next", new CommandNext());
        CommandAdminList.put("ban", new CommandBan());
        CommandAdminList.put("banplayer", new CommandBanPlayer());
        CommandAdminList.put("url", new CommandUrl());
        CommandAdminList.put("delete", new CommandDelete());
        CommandAdminList.put("addlist", new CommandAddList());
        CommandAdminList.put("clearlist", new CommandClearList());
        CommandAdminList.put("code", new CommandCode());
        CommandAdminList.put("login", new CommandLogin());
    }

    /**
     * 搜索音乐
     *
     * @param sender    发送者
     * @param name      用户名
     * @param args      参数
     * @param isDefault 是否是默认点歌方式
     */
    public static void searchMusic(Object sender, String name, String[] args, boolean isDefault) {
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
    public static boolean checkMoney(Object sender, String name, int cost) {
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
    public static boolean cost(Object sender, String name, int cost, String message) {
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
    public static void addMusic(Object sender, String name, String[] args) {
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
            } else if (PlayMusic.havePlayer(name)) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().AddMusic.PlayerToMany);
            } else if (AllMusic.getConfig().BanPlayer.contains(name)) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().AddMusic.PlayerBan);
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
            info = info.replace("%Index%", "" + (a + 1))
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
        }
        ICommand command = CommandList.get(args[0]);
        if (command != null) {
            command.ex(sender, name, args);
            return;
        }

        if (AllMusic.getConfig().Admin.stream().anyMatch(name::equalsIgnoreCase)) {
            command = CommandAdminList.get(args[0]);
            if (command != null) {
                command.ex(sender, name, args);
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
