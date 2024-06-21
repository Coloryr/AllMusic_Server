package com.coloryr.allmusic.server.core.music.play;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.decoder.Bitstream;
import com.coloryr.allmusic.server.core.decoder.Header;
import com.coloryr.allmusic.server.core.objs.config.LimitObj;
import com.coloryr.allmusic.server.core.objs.message.PAL;
import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.sql.DataSql;
import com.coloryr.allmusic.server.core.utils.Logs;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PlayMusic {

    /**
     * 播放列表
     */
    private static final List<SongInfoObj> playList = new ArrayList<>();
    private static final Queue<MusicObj> tasks = new ConcurrentLinkedQueue<>();
    private static final List<String> playIdleList = new ArrayList<>();
    /**
     * 投票时间
     */
    public static int voteTime = 0;
    public static String voteSender;
    public static int pushTime = 0;
    public static String pushSender;
    /**
     * 总歌曲长度
     */
    public static int musicAllTime = 0;
    /**
     * 剩余歌曲长度
     */
    public static int musicLessTime = 0;
    /**
     * 歌曲现在位置
     */
    public static int musicNowTime = 0;
    /**
     * 当前歌曲信息
     */
    public static SongInfoObj nowPlayMusic;
    /**
     * 当前歌词信息
     */
    public static LyricSave lyric;
    /**
     * 错误次数
     */
    public static int error;
    public static SongInfoObj push;
    private static boolean isRun;
    private static int idleNow;

    /**
     * 停止歌曲逻辑
     */
    public static void stop() {
        PlayMusic.clear();
        isRun = false;
    }

    /**
     * 开始歌曲逻辑
     */
    public static void start() {
        Thread addT = new Thread(PlayMusic::task, "AllMusic_list");
        isRun = true;
        addT.start();
    }

    /**
     * 添加点歌任务
     *
     * @param obj 歌曲
     */
    public static void addTask(MusicObj obj) {
        tasks.add(obj);
    }

    private static void task() {
        while (isRun) {
            try {
                MusicObj obj = tasks.poll();
                if (obj != null) {
                    if (obj.isUrl) {
                        addUrl(obj.url);
                    } else {
                        addMusic(obj.sender, obj.id, obj.name, obj.isDefault);
                    }
                }
                Thread.sleep(10);
            } catch (Exception e) {
                AllMusic.log.warning("歌曲处理出现问题");
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加歌曲
     *
     * @param sender 发送者
     * @param id     歌曲ID
     * @param player 用户名
     * @param isList 是否是空闲歌单
     */
    public static void addMusic(Object sender, String id, String player, boolean isList) {
        if (haveMusic(id))
            return;
        if (sender != null) {
            String text = AllMusic.getMessage().musicPlay.checkMusic;
            text = text.replace(PAL.musicId, id);
            AllMusic.side.sendMessageTask(sender, text);
        }
        Logs.logWrite("player:" + player + " add:" + id);
        try {
            SongInfoObj info = AllMusic.getMusicApi().getMusic(id, player, isList);
            if (info == null) {
                if (sender != null) {
                    String data = AllMusic.getMessage().musicPlay.emptyCanPlay;
                    AllMusic.side.sendMessageTask(sender, data.replace(PAL.musicId, id));
                }
                return;
            }
            LimitObj limit = AllMusic.getConfig().limit;
            if (limit.musicTimeLimit && info.getLength() / 1000 > limit.maxMusicTime) {
                AllMusic.side.sendMessageTask(sender, AllMusic.getMessage().addMusic.timeOut);
                return;
            }
            playList.add(info);
            if (!AllMusic.getConfig().muteAddMessage) {
                String data = getData(info);
                if (AllMusic.getConfig().showInBar) {
                    AllMusic.side.sendBar(data);
                } else {
                    AllMusic.side.bqTask(data);
                }
            }
            if (AllMusic.getConfig().playListSwitch
                    && (PlayMusic.nowPlayMusic != null && PlayMusic.nowPlayMusic.isList())) {
                PlayMusic.musicLessTime = 1;
                if (!isList)
                    AllMusic.side.bqTask(AllMusic.getMessage().musicPlay.switchMusic);
            }
            error = 0;
        } catch (Exception e) {
            if (isList) {
                error++;
            }
            AllMusic.log.warning("§d[AllMusic3]§c歌曲信息解析错误");
            e.printStackTrace();
        }
    }

    /**
     * 构建字符串
     *
     * @param info 歌曲信息
     * @return 信息
     */
    private static @NotNull String getData(SongInfoObj info) {
        LimitObj limit = AllMusic.getConfig().limit;
        String message = AllMusic.getMessage().musicPlay.addMusic
                .replace(PAL.musicName, info.getName())
                .replace(PAL.musicAuthor, info.getAuthor())
                .replace(PAL.musicAl, info.getAl())
                .replace(PAL.musicAlia, info.getAlia());
        if (limit.messageLimit
                && message.length() > limit.messageLimitSize) {
            message = message.substring(0, limit.messageLimitSize) + limit.limitText;
        }
        return message;
    }

    /**
     * 将歌曲移动到队列头
     *
     * @param obj
     */
    public static void pushMusic(SongInfoObj obj) {
        synchronized (playList) {
            playList.remove(obj);
            playList.add(0, obj);
        }
    }

    /**
     * 获取播放列表长度
     *
     * @return 长度
     */
    public static int getListSize() {
        synchronized (playList) {
            return playList.size();
        }
    }

    /**
     * 获取当前播放列表
     *
     * @return 播放列表
     */
    public static List<SongInfoObj> getList() {
        synchronized (playList) {
            return new ArrayList<>(playList);
        }
    }

    /**
     * 清理播放列表
     */
    public static void clear() {
        synchronized (playList) {
            playList.clear();
        }
    }

    /**
     * 从播放列表删除
     *
     * @param index 标号
     * @return 结果
     */
    public static SongInfoObj remove(int index) {
        synchronized (playList) {
            return playList.remove(index);
        }
    }

    /**
     * 从播放列表删除
     *
     * @param index
     */
    public static void remove(SongInfoObj index) {
        synchronized (playList) {
            playList.remove(index);
        }
    }

    /**
     * 获取播放列表所有信息
     *
     * @return 信息
     */
    public static String getAllList() {
        StringBuilder list = new StringBuilder();
        String a;

        SongInfoObj info;
        for (int i = 0; i < playList.size(); i++) {
            info = playList.get(i);
            a = AllMusic.getMessage().musicPlay.listMusic.item;
            a = a.replace(PAL.index, String.valueOf(i + 1))
                    .replace(PAL.musicName, info.getName())
                    .replace(PAL.musicAuthor, info.getAuthor())
                    .replace(PAL.musicAl, info.getAl())
                    .replace(PAL.musicAlia, info.getAlia());
            list.append(a).append("\n");
        }
        String temp = list.toString();
        if (temp.isEmpty())
            return "";
        return temp.substring(0, temp.length() - 1);
    }

    /**
     * 是否在播放列表中
     *
     * @param id 音乐ID
     * @return 结果
     */
    public static boolean haveMusic(String id) {
        if (nowPlayMusic != null && nowPlayMusic.getID().equalsIgnoreCase(id))
            return true;
        for (SongInfoObj item : playList) {
            if (item.getID().equalsIgnoreCase(id))
                return true;
        }
        return false;
    }

    /**
     * 添加Url歌曲
     *
     * @param url 链接
     */
    private static void addUrl(String url) {
        try {
            URL urlfile = new URL(url);
            URLConnection con = urlfile.openConnection();
            int b = con.getContentLength();// 得到音乐文件的总长度
            BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
            Bitstream bt = new Bitstream(bis);
            Header h = bt.readFrame();
            int le = 6000000;
            if (h == null) {
                AllMusic.side.bqTask(AllMusic.getMessage().musicPlay.error1);
            } else {
                le = (int) h.total_ms(b);
            }
            SongInfoObj info = new SongInfoObj(AllMusic.getMessage().custom.info, url, le);
            playList.add(info);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic3]§c歌曲信息解析错误");
            e.printStackTrace();
        }
    }

    /**
     * 判断玩家点歌数量是否超上限
     *
     * @param name 玩家名
     * @return
     */
    public static boolean isPlayerMax(String name) {
        int list = AllMusic.getConfig().maxPlayerList;
        if (list == 0) {
            return false;
        }
        int count = 0;
        for (MusicObj obj : tasks) {
            if (obj.name.equalsIgnoreCase(name)) {
                count++;
            }
        }

        return list <= count;
    }

    public static void addIdleList(List<String> list) {
        playIdleList.addAll(list);
    }

    public static void clearIdleList() {
        playIdleList.clear();
        DataSql.clearIdleList();
    }

    public static int getIdleListSize() {
        return playIdleList.size();
    }

    /**
     * 获取空闲歌单的一首歌
     *
     * @return 结果
     */
    public static String getIdleMusic() {
        if (AllMusic.getMusicApi().isUpdate()) {
            return null;
        }
        String ID;
        if (playIdleList.isEmpty())
            return null;
        if (AllMusic.getConfig().playListRandom) {
            if (playIdleList.size() == 1)
                return playIdleList.get(0);
            ID = playIdleList.get(new Random().nextInt(playIdleList.size()));
        } else {
            ID = playIdleList.get(idleNow);
            idleNow++;
            if (idleNow >= playIdleList.size()) {
                idleNow = 0;
            }
        }
        return ID;
    }

    public static SongInfoObj findPlayerMusic(String name) {
        List<SongInfoObj> list1 = getList();
        for (int a = 0; a < playList.size(); a++) {
            SongInfoObj item = list1.get(a);
            if (name.equalsIgnoreCase(item.getCall())) {
                return item;
            }
        }

        return null;
    }

    public static SongInfoObj findMusicIndex(int index) {
        List<SongInfoObj> list1 = getList();
        index--;
        if (index <= 0) {
            return null;
        }
        if (list1.size() <= index) {
            return null;
        }

        return list1.get(index);
    }
}

