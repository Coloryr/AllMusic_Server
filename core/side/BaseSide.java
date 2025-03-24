package com.coloryr.allmusic.server.core.side;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.config.SaveObj;
import com.coloryr.allmusic.server.core.objs.enums.ComType;
import com.coloryr.allmusic.server.core.objs.enums.HudType;
import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.utils.HudUtils;

import java.io.File;
import java.util.Collection;
import java.util.Locale;

public abstract class BaseSide {
    /**
     * 主线程运行任务
     *
     * @param run 任务
     */
    public abstract void runTask(Runnable run);

    /**
     * 主线程延迟任务
     *
     * @param run   任务
     * @param delay 延迟
     */
    public abstract void runTask(Runnable run, int delay);

    /**
     * 检查权限
     *
     * @param player 用户
     * @return 是否有权限
     */
    public abstract boolean checkPermission(Object player);

    /**
     * 是否为玩家执行指令
     *
     * @param source 命令源
     * @return 是否为玩家
     */
    public abstract boolean isPlayer(Object source);

    /**
     * 检查权限
     *
     * @param player     用户
     * @param permission 权限
     * @return 是否有权限
     */
    public abstract boolean checkPermission(Object player, String permission);

    /**
     * 是否需要播放
     *
     * @return 结果
     */
    public abstract boolean needPlay(boolean islist);

    /**
     * 获取所有玩家
     *
     * @return 玩家列表
     */
    public abstract Collection<Object> getPlayers();

    /**
     * 获取玩家名字
     *
     * @param player 玩家
     * @return 名字
     */
    public abstract String getPlayerName(Object player);

    /**
     * 获取玩家所在的服务器
     *
     * @param player 玩家
     * @return 名字
     */
    public abstract String getPlayerServer(Object player);

    /**
     * 向玩家发送数据包
     *
     * @param player 玩家
     * @param type   数据
     * @param data   数据
     * @param data1  数据
     */
    public abstract void send(Object player, ComType type, String data, int data1);

    /**
     * 根据名字获取玩家
     *
     * @param player 名字
     * @return 玩家
     */
    public abstract Object getPlayer(String player);

    /**
     * 发送bar消息
     *
     * @param player 玩家
     * @param data   消息
     */
    public abstract void sendBar(Object player, String data);

    /**
     * 获取插件配置文件夹
     *
     * @return 文件夹
     */
    public abstract File getFolder();

    /**
     * 发送消息
     *
     * @param obj     接受对象
     * @param message 消息
     */
    public abstract void sendMessage(Object obj, String message);

    /**
     * 发送执行指令
     *
     * @param obj     接受对象
     * @param message 消息
     * @param end     结尾
     * @param command 指令
     */
    public abstract void sendMessageRun(Object obj, String message, String end, String command);

    /**
     * 发送推荐指令
     *
     * @param obj     接受对象
     * @param message 消息
     * @param end     结尾
     * @param command 指令
     */
    public abstract void sendMessageSuggest(Object obj, String message, String end, String command);

    /**
     * 当歌曲添加时触发的事件
     *
     * @param obj 歌曲信息
     * @return 是否阻止播放
     */
    public abstract boolean onMusicPlay(SongInfoObj obj);

    /**
     * 当歌曲播放时触发的事件
     *
     * @param obj   发送者
     * @param music 歌曲信息
     * @return 是否阻止添加
     */
    public abstract boolean onMusicAdd(Object obj, MusicObj music);

    /**
     * 广播消息
     *
     * @param data 消息
     */
    public abstract void broadcast(String data);

    /**
     * 广播点击消息
     *
     * @param message 消息
     * @param end     结尾
     * @param command 指令
     */
    public abstract void broadcastWithRun(String message, String end, String command);

    /**
     * 重载
     */
    public final void reload() {
        new AllMusic().init(getFolder());
    }

    /**
     * 发送歌曲位置信息
     *
     * @param player 用户名
     * @param pos    歌曲位置(毫秒)
     */
    public final void sendPos(String player, int pos) {
        Object player1 = getPlayer(player);
        if (player1 == null)
            return;
        if (AllMusic.isSkip(player, getPlayerServer(player), true))
            return;
        try {
            send(player1, ComType.POS, null, pos);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    /**
     * 发送停止指令
     *
     * @param name 用户名
     */
    public final void sendStop(String name) {
        AllMusic.removeNowPlayPlayer(name);
        Object player = getPlayer(name);
        if (player == null)
            return;
        try {
            send(player, ComType.STOP, null, 0);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c停止指令发送出错");
            e.printStackTrace();
        }
    }

    /**
     * 发送停止指令
     */
    public final void sendStop() {
        AllMusic.clearNowPlayer();
        try {
            for (Object player : getPlayers()) {
                send(player, ComType.STOP, null, 0);
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c停止指令发送出错");
            e.printStackTrace();
        }
    }

    /**
     * 向指定玩家发送播放歌曲指令
     *
     * @param player 玩家
     * @param url    地址
     */
    public final void sendMusic(String player, String url) {
        AllMusic.addNowPlayPlayer(player);
        Object player1 = getPlayer(player);
        if (player1 == null)
            return;
        try {
            if (AllMusic.isSkip(player, getPlayerServer(player), false))
                return;
            send(player1, ComType.PLAY, url, 0);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲指令发送出错");
            e.printStackTrace();
        }
    }

    /**
     * 发送Hud的歌词数据
     *
     * @param data 数据
     */
    public final void sendHudLyric(String data) {
        for (Object player : getPlayers()) {
            String name = getPlayerName(player);
            if (name == null)
                continue;
            name = name.toLowerCase(Locale.ROOT);
            if (AllMusic.isSkip(name, getPlayerServer(player), true))
                continue;
            try {
                SaveObj obj = HudUtils.get(name);
                if (!obj.lyric.enable)
                    continue;
                send(player, ComType.LYRIC, data, 0);
            } catch (Exception e) {
                AllMusic.log.warning("§d[AllMusic]§c歌词发送出错");
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送Hud的信息数据
     *
     * @param data 数据
     */
    public final void sendHudInfo(String data) {
        for (Object player : getPlayers()) {
            String name = getPlayerName(player);
            if (name == null)
                continue;
            name = name.toLowerCase(Locale.ROOT);
            if (AllMusic.isSkip(name, getPlayerServer(player), true))
                continue;
            try {
                SaveObj obj = HudUtils.get(name);
                if (!obj.lyric.enable)
                    continue;
                send(player, ComType.INFO, data, 0);
            } catch (Exception e) {
                AllMusic.log.warning("§d[AllMusic]§c歌词信息发送出错");
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送Hud的位置信息
     *
     * @param name 用户名
     */
    public final void sendHudPos(String name) {
        Object player = getPlayer(name);
        if (player == null)
            return;
        if (AllMusic.isSkip(name, null, false))
            return;
        try {
            SaveObj obj = HudUtils.get(name);
            String data = AllMusic.gson.toJson(obj);
            send(player, ComType.HUD, data, 0);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c界面位置发送出错");
            e.printStackTrace();
        }
    }

    /**
     * 发送Hud数据
     *
     * @param name 用户名
     * @param pos  位置
     * @param data 信息
     */
    public final void sendHud(String name, HudType pos, String data) {
        try {
            if (pos == HudType.PIC) {
                return;
            }
            Object player = getPlayer(name);
            if (player == null)
                return;
            if (AllMusic.isSkip(name, getPlayerServer(player), true))
                return;
            switch (pos) {
                case INFO:
                    send(player, ComType.INFO, data, 0);
                    break;
                case LIST:
                    send(player, ComType.LIST, data, 0);
                    break;
                case LYRIC:
                    send(player, ComType.LYRIC, data, 0);
                    break;
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c停止指令发送出错");
            e.printStackTrace();
        }
    }

    /**
     * 发送Hud的歌曲列表数据
     *
     * @param data 数据
     */
    public final void sendHudList(String data) {
        for (Object player : getPlayers()) {
            String name = getPlayerName(player);
            if (name == null)
                continue;
            name = name.toLowerCase(Locale.ROOT);
            if (AllMusic.isSkip(name, getPlayerServer(player), true))
                continue;
            try {
                SaveObj obj = HudUtils.get(name);
                if (!obj.list.enable)
                    continue;
                send(player, ComType.LIST, data, 0);
            } catch (Exception e) {
                AllMusic.log.warning("§d[AllMusic]§c歌曲列表发送出错");
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送Hud位置数据
     */
    public final void sendHudUtilsAll() {
        for (Object player : getPlayers()) {
            String name = getPlayerName(player);
            if (name == null)
                continue;
            name = name.toLowerCase(Locale.ROOT);
            try {
                SaveObj obj = HudUtils.get(name);
                String data = AllMusic.gson.toJson(obj);
                send(player, ComType.HUD, data, 0);
            } catch (Exception e1) {
                AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
                e1.printStackTrace();
            }
        }
    }

    /**
     * 发送Bar数据
     *
     * @param data 数据
     */
    public final void sendBar(String data) {
        for (Object player : getPlayers()) {
            String name = getPlayerName(player);
            if (name == null)
                continue;
            name = name.toLowerCase(Locale.ROOT);
            try {
                if (AllMusic.isSkip(name, getPlayerServer(player), true))
                    continue;
                sendBar(player, data);
            } catch (Exception e1) {
                AllMusic.log.warning("§d[AllMusic]§c数据发送发生错误");
                e1.printStackTrace();
            }
        }
    }

    /**
     * 发送播放歌曲指令
     *
     * @param url 歌曲Url
     */
    public final void sendMusic(String url) {
        for (Object player : getPlayers()) {
            String name = getPlayerName(player);
            if (name == null)
                continue;
            name = name.toLowerCase(Locale.ROOT);
            if (AllMusic.isSkip(name, getPlayerServer(player), false))
                continue;
            try {
                send(player, ComType.PLAY, url, 0);
                AllMusic.addNowPlayPlayer(name);
            } catch (Exception e) {
                AllMusic.log.warning("§d[AllMusic]§c歌曲指令发送出错");
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送图片数据指令
     *
     * @param url 图片地址
     */
    public final void sendPic(String url) {
        for (Object player : getPlayers()) {
            String name = getPlayerName(player);
            if (name == null)
                continue;
            name = name.toLowerCase(Locale.ROOT);
            if (AllMusic.isSkip(name, getPlayerServer(player), true))
                continue;
            SaveObj obj = HudUtils.get(name);
            if (!obj.pic.enable)
                continue;
            try {
                send(player, ComType.IMG, url, 0);
            } catch (Exception e) {
                AllMusic.log.warning("§d[AllMusic]§c图片指令发送出错");
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送图片数据指令
     *
     * @param player 用户名
     * @param url    图片地址
     */
    public final void sendPic(String player, String url) {
        Object player1 = getPlayer(player);
        if (player1 == null)
            return;
        if (AllMusic.isSkip(player, getPlayerServer(player), true))
            return;
        try {
            send(player1, ComType.IMG, url, 0);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c图片指令发送出错");
            e.printStackTrace();
        }
    }

    /**
     * 清理Hud数据
     *
     * @param name 用户名
     */
    public final void clearHud(String name) {
        Object player = getPlayer(name);
        if (player == null)
            return;
        try {
            send(player, ComType.CLEAR, null, 0);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
            e.printStackTrace();
        }
    }

    /**
     * 清理Hud数据
     */
    public final void clearHud() {
        for (Object player : getPlayers()) {
            try {
                send(player, ComType.CLEAR, null, 0);
            } catch (Exception e) {
                AllMusic.log.warning("§d[AllMusic]§c清空Hud发生出错");
                e.printStackTrace();
            }
        }
    }

    /**
     * 在主线程中广播消息
     *
     * @param data 消息
     */
    public final void broadcastInTask(String data) {
        runTask(() -> broadcast(data));
    }

    /**
     * 在主线程发送消息
     *
     * @param obj     接受对象
     * @param message 消息
     */
    public final void sendMessageTask(Object obj, String message) {
        runTask(() -> sendMessage(obj, message));
    }

    /**
     * 更新信息
     */
    public void updateInfo() {

    }

    /**
     * 更新歌词
     */
    public void updateLyric() {

    }

    /**
     * BC发送ping包
     */
    public void ping() {

    }
}
