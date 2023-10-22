package coloryr.allmusic.core.music.play;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.decoder.Bitstream;
import coloryr.allmusic.core.decoder.Header;
import coloryr.allmusic.core.objs.music.MusicObj;
import coloryr.allmusic.core.objs.music.SongInfoObj;
import coloryr.allmusic.core.utils.Logs;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayMusic {

    /**
     * 播放列表
     */
    private static final List<SongInfoObj> playList = new CopyOnWriteArrayList<>();
    private static final Queue<MusicObj> tasks = new ConcurrentLinkedQueue<>();
    /**
     * 投票时间
     */
    public static int voteTime = 0;
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
    private static boolean isRun;

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
        if (isHave(id))
            return;
        if (sender != null) {
            String text = AllMusic.getMessage().MusicPlay.CheckMusic;
            text = text.replace("%MusicID%", id);
            AllMusic.side.sendMessaget(sender, text);
        }
        Logs.logWrite("玩家：" + player + " 点歌：" + id);
        try {
            SongInfoObj info = AllMusic.getMusicApi().getMusic(id, player, isList);
            if (info == null) {
                if (sender != null) {
                    String data = AllMusic.getMessage().MusicPlay.NoCanPlay;
                    AllMusic.side.sendMessaget(sender, data.replace("%MusicID%", id));
                }
                return;
            }
            if (info.getLength() / 1000 > AllMusic.getConfig().MaxMusicTime) {
                AllMusic.side.sendMessaget(sender, AllMusic.getMessage().AddMusic.TimeOut);
                return;
            }
            playList.add(info);
            if (!AllMusic.getConfig().MuteAddMessage) {
                String data = AllMusic.getMessage().MusicPlay.AddMusic;
                data = data.replace("%MusicName%", info.getName())
                        .replace("%MusicAuthor%", info.getAuthor())
                        .replace("%MusicAl%", info.getAl())
                        .replace("%MusicAlia%", info.getAlia());
                if (AllMusic.getConfig().ShowInBar) {
                    AllMusic.side.sendBar(data);
                } else {
                    AllMusic.side.bqt(data);
                }
            }
            if (AllMusic.getConfig().PlayListSwitch
                    && (PlayMusic.nowPlayMusic != null && PlayMusic.nowPlayMusic.isList())) {
                PlayMusic.musicLessTime = 1;
                if (!isList)
                    AllMusic.side.bqt(AllMusic.getMessage().MusicPlay.Switch);
            }
            error = 0;
        } catch (Exception e) {
            if (isList) {
                error++;
            }
            AllMusic.log.warning("§d[AllMusic]§c歌曲信息解析错误");
            e.printStackTrace();
        }
    }

    /**
     * 获取播放列表长度
     *
     * @return 长度
     */
    public static int getSize() {
        return playList.size();
    }

    /**
     * 获取当前播放列表
     *
     * @return 播放列表
     */
    public static List<SongInfoObj> getList() {
        return new ArrayList<>(playList);
    }

    /**
     * 清理播放列表
     */
    public static void clear() {
        playList.clear();
    }

    /**
     * 从播放列表删除
     *
     * @param index 标号
     * @return 结果
     */
    public static SongInfoObj remove(int index) {
        return playList.remove(index);
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
            a = AllMusic.getMessage().MusicPlay.ListMusic.Item;
            a = a.replace("%Index%", "" + (i + 1))
                    .replace("%MusicName%", info.getName())
                    .replace("%MusicAuthor%", info.getAuthor())
                    .replace("%MusicAl%", info.getAl())
                    .replace("%MusicAlia%", info.getAlia());
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
    public static boolean isHave(String id) {
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
                AllMusic.side.bqt("§d[AllMusic]§c未知音乐类型");
            } else {
                le = (int) h.total_ms(b);
            }
            SongInfoObj info = new SongInfoObj(AllMusic.getMessage().Custom.Info, url, le);
            playList.add(info);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲信息解析错误");
            e.printStackTrace();
        }
    }

    public static boolean havePlayer(String name) {
        int list = AllMusic.getConfig().PlayerMaxList;
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
}

