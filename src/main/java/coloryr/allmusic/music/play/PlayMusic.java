package coloryr.allmusic.music.play;

import coloryr.allmusic.AllMusic;
import coloryr.allmusic.decoder.Bitstream;
import coloryr.allmusic.decoder.Header;
import coloryr.allmusic.music.api.SongInfo;
import coloryr.allmusic.music.lyric.LyricItem;
import coloryr.allmusic.music.lyric.LyricSave;
import coloryr.allmusic.utils.Logs;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayMusic {

    public static final List<SongInfo> playList = new CopyOnWriteArrayList<>();
    public static int voteTime = 0;
    public static int musicAllTime = 0;
    public static int musicLessTime = 0;
    public static int musicNowTime = 0;
    public static SongInfo nowPlayMusic;

    public static LyricSave lyric;
    public static LyricItem lyricItem;
    public static int error;

    private static boolean isRun;
    private static final Queue<MusicObj> tasks = new ConcurrentLinkedQueue<>();

    public static void stop() {
        PlayMusic.clear();
        isRun = false;
    }

    public static void start() {
        Thread addT = new Thread(PlayMusic::task, "AllMusic_list");
        isRun = true;
        addT.start();
    }

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

    public static void addMusic(Object sender, String id, String player, boolean isList) {
        if (isHave(id))
            return;
        if (sender != null) {
            String text = AllMusic.getMessage().getMusicPlay().getCheckMusic();
            text = text.replace("%MusicID%", id);
            AllMusic.side.sendMessaget(sender, text);
        }
        Logs.logWrite("玩家：" + player + " 点歌：" + id);
        try {
            SongInfo info = AllMusic.getMusicApi().getMusic(id, player, isList);
            if (info == null) {
                if (sender != null) {
                    String data = AllMusic.getMessage().getMusicPlay().getNoCanPlay();
                    AllMusic.side.sendMessaget(sender, data.replace("%MusicID%", id));
                }
                return;
            }
            if (info.getLength() / 1000 > AllMusic.getConfig().getMaxMusicTime()) {
                AllMusic.side.sendMessaget(sender, AllMusic.getMessage().getAddMusic().getTimeOut());
                return;
            }
            playList.add(info);
            if (!AllMusic.getConfig().isMuteAddMessage()) {
                String data = AllMusic.getMessage().getMusicPlay().getAddMusic();
                data = data.replace("%MusicName%", info.getName())
                        .replace("%MusicAuthor%", info.getAuthor())
                        .replace("%MusicAl%", info.getAl())
                        .replace("%MusicAlia%", info.getAlia());
                AllMusic.side.bqt(data);
            }
            if (AllMusic.getConfig().isPlayListSwitch()
                    && (PlayMusic.nowPlayMusic != null && PlayMusic.nowPlayMusic.isList())) {
                PlayMusic.musicLessTime = 1;
                if (!isList)
                    AllMusic.side.bqt(AllMusic.getMessage().getMusicPlay().getSwitch());
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

    public static int getSize() {
        return playList.size();
    }

    public static List<SongInfo> getList() {
        return new ArrayList<>(playList);
    }

    public static void clear() {
        playList.clear();
    }

    public static SongInfo remove(int index) {
        return playList.remove(index);
    }

    public static String getAllList() {
        StringBuilder list = new StringBuilder();
        String a;
        SongInfo info;
        for (int i = 0; i < playList.size(); i++) {
            info = playList.get(i);
            a = AllMusic.getMessage().getMusicPlay().getListMusic().getItem();
            a = a.replace("%index%", "" + (i + 1))
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

    public static boolean isHave(String ID) {
        if (nowPlayMusic != null && nowPlayMusic.getID().equalsIgnoreCase(ID))
            return true;
        for (SongInfo item : playList) {
            if (item.getID().equalsIgnoreCase(ID))
                return true;
        }
        return false;
    }

    private static void addUrl(String arg) {
        try {
            URL urlfile = new URL(arg);
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
            SongInfo info = new SongInfo(AllMusic.getMessage().getCustom().getInfo(), arg, le);
            playList.add(info);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲信息解析错误");
            e.printStackTrace();
        }
    }
}

