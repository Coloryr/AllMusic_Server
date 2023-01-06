package coloryr.allmusic.side;

import coloryr.allmusic.objs.music.SongInfoObj;
import coloryr.allmusic.objs.music.MusicObj;

public interface ISide {
    /**
     * 发送AllMusic插件信道消息
     * @param data 数据
     * @param player 用户名
     * @param isplay 是否是播放
     */
    void send(String data, String player, Boolean isplay);

    /**
     * 获取当前玩家数量
     * @return 数量
     */
    int getAllPlayer();

    /**
     * 广播消息
     * @param data 消息
     */
    void bq(String data);

    /**
     * 在主线程中广播消息
     * @param data 消息
     */
    void bqt(String data);

    /**
     * 是否需要播放
     * @return 结果
     */
    boolean needPlay();

    /**
     * 发送停止指令
     */
    void sendStop();

    /**
     * 发送停止指令
     * @param name 用户名
     */
    void sendStop(String name);

    /**
     * 发送播放歌曲指令
     * @param url 歌曲Url
     */
    void sendMusic(String url);

    /**
     * 发送图片数据指令
     * @param url 图片地址
     */
    void sendPic(String url);

    /**
     * 发送Hud的歌词数据
     * @param data 数据
     */
    void sendHudLyric(String data);

    /**
     * 发送Hud的信息数据
     * @param data 数据
     */
    void sendHudInfo(String data);

    /**
     * 发送Hud的歌曲列表数据
     * @param data 数据
     */
    void sendHudList(String data);

    /**
     * 发送Hud位置数据
     */
    void sendHudUtilsAll();

    /**
     * 发送Bar数据
     * @param data 数据
     */
    void sendBar(String data);

    /**
     * 清理Hud数据
     * @param player 用户名
     */
    void clearHud(String player);

    /**
     * 清理Hud数据
     */
    void clearHud();

    /**
     * 在主线程发送消息
     * @param obj 接受对象
     * @param message 消息
     */
    void sendMessaget(Object obj, String message);

    /**
     * 发送消息
     * @param obj 接受对象
     * @param message 消息
     */
    void sendMessage(Object obj, String message);

    /**
     * 发送执行指令
     * @param obj 接受对象
     * @param message 消息
     * @param end 结尾
     * @param command 指令
     */
    void sendMessageRun(Object obj, String message, String end, String command);

    /**
     * 发送推荐指令
     * @param obj 接受对象
     * @param message 消息
     * @param end 结尾
     * @param command 指令
     */
    void sendMessageSuggest(Object obj, String message, String end, String command);

    /**
     * 主线程运行任务
     * @param run 任务
     */
    void runTask(Runnable run);

    /**
     * 重载
     */
    void reload();

    /**
     * 检查权限
     * @param player 用户名
     * @param permission 权限
     * @return 是否有权限
     */
    boolean checkPermission(String player, String permission);

    /**
     * 主线程延迟任务
     * @param run 任务
     * @param delay 延迟
     */
    void runTask(Runnable run, int delay);

    /**
     * 更新信息
     */
    void updateInfo();

    /**
     * 更新歌词
     */
    void updateLyric();

    /**
     * 发送BC的ping包
     */
    void ping();

    /**
     * 当歌曲添加时触发的事件
     * @param obj 歌曲信息
     * @return 是否阻止播放
     */
    boolean onMusicPlay(SongInfoObj obj);

    /**
     * 当歌曲播放时触发的事件
     * @param obj 发送者
     * @param music 歌曲信息
     * @return 是否阻止添加
     */
    boolean onMusicAdd(Object obj, MusicObj music);
}
