package com.coloryr.allmusic.server.core.side;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.enums.HudType;
import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;

import java.util.List;

public abstract class ISide {
    /**
     * 重载
     */
    public abstract void reload();

    /**
     * 获取当前玩家数量
     *
     * @return 数量
     */
    public abstract int getAllPlayer();

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
     * @param player     用户名
     * @param permission 权限
     * @return 是否有权限
     */
    public abstract boolean checkPermission(String player, String permission);

    /**
     * 是否需要播放
     *
     * @return 结果
     */
    public abstract boolean needPlay();

    /**
     * 发送停止指令
     */
    public final void sendStop() {
        AllMusic.clearNowPlayer();
        topSendStop();
    }

    protected abstract void topSendStop();

    /**
     * 发送停止指令
     *
     * @param name 用户名
     */
    public final void sendStop(String name) {
        AllMusic.removeNowPlayPlayer(name);
        topSendStop(name);
    }

    protected abstract void topSendStop(String name);

    /**
     * 发送播放歌曲指令
     *
     * @param url 歌曲Url
     */
    public abstract void sendMusic(String url);

    public final void sendMusic(String player, String url) {
        AllMusic.addNowPlayPlayer(player);
        topSendMusic(player, url);
    }

    protected abstract void topSendMusic(String player, String url);

    /**
     * 发送图片数据指令
     *
     * @param url 图片地址
     */
    public abstract void sendPic(String url);

    /**
     * 发送图片数据指令
     *
     * @param player 用户名
     * @param url    图片地址
     */
    public abstract void sendPic(String player, String url);

    /**
     * 发送歌曲位置信息
     *
     * @param player 用户名
     * @param pos    歌曲位置(毫秒)
     */
    public abstract void sendPos(String player, int pos);

    /**
     * 发送Hud的歌词数据
     *
     * @param data 数据
     */
    public abstract void sendHudLyric(String data);

    /**
     * 发送Hud的信息数据
     *
     * @param data 数据
     */
    public abstract void sendHudInfo(String data);

    /**
     * 发送Hud的位置信息
     *
     * @param name 用户名
     */
    public abstract void sendHudPos(String name);

    /**
     * 发送Hud数据
     * @param name 用户名
     * @param pos 位置
     * @param data 信息
     */
    public abstract void sendHud(String name, HudType pos, String data);

    /**
     * 发送Hud的歌曲列表数据
     *
     * @param data 数据
     */
    public abstract void sendHudList(String data);

    /**
     * 发送Hud位置数据
     */
    public abstract void sendHudUtilsAll();

    /**
     * 发送Bar数据
     *
     * @param data 数据
     */
    public abstract void sendBar(String data);

    /**
     * 清理Hud数据
     *
     * @param player 用户名
     */
    public abstract void clearHud(String player);

    /**
     * 清理Hud数据
     */
    public abstract void clearHud();

    /**
     * 广播消息
     *
     * @param data 消息
     */
    public abstract void bq(String data);

    /**
     * 广播点击消息
     * @param message 消息
     * @param end     结尾
     * @param command 指令
     */
    public abstract void bqRun(String message, String end, String command);

    /**
     * 在主线程中广播消息
     *
     * @param data 消息
     */
    public abstract void bqt(String data);

    /**
     * 在主线程发送消息
     *
     * @param obj     接受对象
     * @param message 消息
     */
    public abstract void sendMessaget(Object obj, String message);

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
     * 更新信息
     */
    public abstract void updateInfo();

    /**
     * 更新歌词
     */
    public abstract void updateLyric();

    /**
     * 发送BC的ping包
     */
    public abstract void ping();

    /**
     * 获取玩家列表
     * @return 玩家列表
     */
    public abstract List<String> getPlayerList();
}
