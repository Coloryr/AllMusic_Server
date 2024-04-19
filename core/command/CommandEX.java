package com.coloryr.allmusic.server.core.command;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.music.play.MusicSearch;
import com.coloryr.allmusic.server.core.music.play.PlayMusic;
import com.coloryr.allmusic.server.core.objs.SearchMusicObj;
import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import com.coloryr.allmusic.server.core.objs.music.SearchPageObj;
import com.coloryr.allmusic.server.core.utils.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandEX {

    public static final Map<String, ICommand> commandList = new HashMap<>();
    public static final Map<String, ICommand> commandAdminList = new HashMap<>();

    static {
        commandList.put("stop", new CommandStop());
        commandList.put("help", new CommandHelp());
        commandList.put("list", new CommandList());
        commandList.put("vote", new CommandVote());
        commandList.put("mute", new CommandMute());
        commandList.put("search", new CommandSearch());
        commandList.put("select", new CommandSelect());
        commandList.put("nextpage", new CommandNextPage());
        commandList.put("lastpage", new CommandLastPage());
        commandList.put("hud", new CommandHud());
        commandList.put("reload", new CommandReload());
        commandList.put("push", new CommandPush());
        commandList.put("cancel", new CommandCancel());

        commandAdminList.put("next", new CommandNext());
        commandAdminList.put("ban", new CommandBan());
        commandAdminList.put("banplayer", new CommandBanPlayer());
        commandAdminList.put("url", new CommandUrl());
        commandAdminList.put("delete", new CommandDelete());
        commandAdminList.put("addlist", new CommandAddList());
        commandAdminList.put("clearlist", new CommandClearList());
        commandAdminList.put("code", new CommandCode());
        commandAdminList.put("login", new CommandLogin());
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
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().addMusic.cancel);
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
        if (!AllMusic.getConfig().useCost || AllMusic.economy == null) {
            return false;
        }

        if (!AllMusic.economy.check(name, cost)) {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().cost.noMoney);
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
        if (!AllMusic.getConfig().useCost || AllMusic.economy == null) {
            return false;
        }

        if (AllMusic.economy.cost(name, cost)) {
            AllMusic.side.sendMessage(sender, message
                    .replace("%Cost%", "" + cost));
            return false;
        } else {
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().cost.costFail);
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
            if (PlayMusic.getListSize() >= AllMusic.getConfig().maxPlayList) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().addMusic.listFull);
            } else if (AllMusic.getConfig().banMusic.contains(musicID)) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().addMusic.banMusic);
            } else if (PlayMusic.haveMusic(musicID)) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().addMusic.existMusic);
            } else if (PlayMusic.havePlayer(name)) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().addMusic.playerToMany);
            } else if (AllMusic.getConfig().banPlayer.contains(name)) {
                AllMusic.side.sendMessage(sender, AllMusic.getMessage().addMusic.playerBan);
            } else {
                if (checkMoney(sender, name, AllMusic.getConfig().addMusicCost)) {
                    return;
                }
                if (cost(sender, name, AllMusic.getConfig().addMusicCost,
                        AllMusic.getMessage().cost.addMusic)) {
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
                        AllMusic.side.sendMessage(sender, AllMusic.getMessage().addMusic.cancel);
                        return;
                    }

                    PlayMusic.addTask(obj);
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().addMusic.success);
                } else
                    AllMusic.side.sendMessage(sender, AllMusic.getMessage().addMusic.noPlayer);
            }
        } else
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().addMusic.noID);
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
            AllMusic.side.sendMessageRun(sender, "§d[AllMusic3]§2输入/music lastpage上一页",
                    AllMusic.getMessage().page.last, "/music lastpage");
        }
        for (int a = 0; a < index; a++) {
            item = search.getRes(a + search.getPage() * 10);
            info = AllMusic.getMessage().page.choice;
            info = info.replace("%Index%", "" + (a + 1))
                    .replace("%MusicName%", item.name)
                    .replace("%MusicAuthor%", item.author)
                    .replace("%MusicAl%", item.al);
            AllMusic.side.sendMessageRun(sender, info,
                    AllMusic.getMessage().click.clickRun, "/music select " + (a + 1));
        }
        if (search.haveNextPage()) {
            AllMusic.side.sendMessageRun(sender, "§d[AllMusic3]§2输入/music nextpage下一页",
                    AllMusic.getMessage().page.next, "/music nextpage");
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
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.error);
            return;
        }
        ICommand command = commandList.get(args[0]);
        if (command != null) {
            command.ex(sender, name, args);
            return;
        }

        if (AllMusic.getConfig().adminList.stream().anyMatch(name::equalsIgnoreCase)) {
            command = commandAdminList.get(args[0]);
            if (command != null) {
                command.ex(sender, name, args);
                return;
            }
        }
        if (AllMusic.getConfig().needPermission &&
                AllMusic.side.checkPermission(name, "allmusic.addmusic"))
            AllMusic.side.sendMessage(sender, AllMusic.getMessage().command.noPer);
        else {
            switch (AllMusic.getConfig().defaultAddMusic) {
                case 1:
                    searchMusic(sender, name, args, true);
                    break;
                case 0:
                default:
                    addMusic(sender, name, args);
            }
        }
    }

    private static final List<String> normal = new ArrayList<String>() {{
        this.add("stop");
        this.add("cancel");
        this.add("list");
        this.add("vote");
        this.add("mute");
        this.add("search");
        this.add("hud");
        this.add("push");
    }};

    /**
     * 管理员的指令
     */
    private static final List<String> admin = new ArrayList<String>() {{
        this.add("reload");
        this.add("next");
        this.add("ban");
        this.add("delete");
        this.add("addlist");
        this.add("clearlist");
        this.add("login");
        this.add("code");
    }};

    /**
     * 搜歌的指令
     */
    private static final List<String> search = new ArrayList<String>() {{
        this.add("select");
        this.add("nextpage");
        this.add("lastpage");
    }};

    /**
     * 获取Tab指令列表
     *
     * @param name 用户名
     * @param arg  参数
     * @return 指令列表
     */
    public static List<String> getTabList(String name, String[] arg) {
        List<String> arguments = new ArrayList<>();
        if (arg.length == 0) {
            arguments.addAll(normal);
            if (AllMusic.getConfig().adminList.contains(name)) {
                arguments.addAll(admin);
            }
            if (AllMusic.getSearch(name) != null) {
                return search;
            }
        } else {
            if (arg[0].isEmpty() || arg.length == 1) {
                arguments.addAll(normal);
                if (AllMusic.getConfig().adminList.contains(name)) {
                    arguments.addAll(admin);
                }
                if (arg[0].isEmpty()) {
                    if (AllMusic.getSearch(name) != null) {
                        return search;
                    }
                }
            } else {
                ICommand command = CommandEX.commandList.get(arg[0]);
                if (command != null) {
                    arguments.addAll(command.tab(name, arg, 1));
                }
                command = CommandEX.commandAdminList.get(arg[0]);
                if (command != null) {
                    arguments.addAll(command.tab(name, arg, 1));
                }
            }
        }

        return arguments;
    }
}
