package Color_yr.AllMusic.musicPlay;

import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.musicAPI.SongInfo;
import Color_yr.AllMusic.musicAPI.songLyric.LyricSave;
import Color_yr.AllMusic.musicAPI.songLyric.ShowOBJ;
import Color_yr.AllMusic.Utils.logs;
import Color_yr.AllMusic.decoder.Bitstream;
import Color_yr.AllMusic.decoder.Header;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayMusic {

    public static final List<SongInfo> PlayList = new CopyOnWriteArrayList<>();
    public static int VoteTime = 0;
    public static int MusicAllTime = 0;
    public static int MusicLessTime = 0;
    public static int MusicNowTime = 0;
    public static SongInfo NowPlayMusic;

    public static LyricSave Lyric;
    public static ShowOBJ nowLyric;

    private static boolean isRun;
    private static final Queue<MusicObj> tasks = new ConcurrentLinkedQueue<>();

    public static void stop() {
        PlayMusic.clear();
        isRun = false;
        PlayGo.stop();
    }

    public static void start() {
        Thread addT = new Thread(PlayMusic::task, "AllMusic_list");
        isRun = true;
        PlayGo.start();
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
                        addMusic((String) obj.sender, obj.Name, obj.isDefault);
                    }
                }
                Thread.sleep(10);
            } catch (Exception e) {
                AllMusic.log.warning("歌曲处理出现问题");
                e.printStackTrace();
            }
        }
    }

    public static void addMusic(String ID, String player, boolean isList) {
        if (isHave(ID))
            return;
        String text = AllMusic.getMessage().getMusicPlay().getPlayerAdd();
        text = text.replace("%PlayerName%", player)
                .replace("%MusicID%", ID);
        AllMusic.Side.bqt(text);
        logs.logWrite("玩家：" + player + " 点歌：" + ID);
        try {
            SongInfo info = AllMusic.getMusicApi().getMusic(ID, player, isList);
            if (info != null) {
                PlayList.add(info);
                String data = AllMusic.getMessage().getMusicPlay().getAddMusic();
                data = data.replace("%MusicName%", info.getName())
                        .replace("%MusicAuthor%", info.getAuthor())
                        .replace("%MusicAl%", info.getAl())
                        .replace("%MusicAlia%", info.getAlia());
                AllMusic.Side.bqt(data);
            } else {
                String data = AllMusic.getMessage().getMusicPlay().getNoCanPlay();
                AllMusic.Side.bqt(data.replace("%MusicID%", ID));
            }
            if (AllMusic.getConfig().isPlayListSwitch()
                    && (PlayMusic.NowPlayMusic != null && PlayMusic.NowPlayMusic.isList())) {
                PlayMusic.MusicLessTime = 1;
                if (!isList)
                    AllMusic.Side.bqt(AllMusic.getMessage().getMusicPlay().getSwitch());
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲信息解析错误");
            e.printStackTrace();
        }
    }

    public static SongInfo getMusic(int index) {
        return PlayList.get(index);
    }

    public static int getSize() {
        return PlayList.size();
    }

    public static List<SongInfo> getList() {
        return new ArrayList<>(PlayList);
    }

    public static void clear() {
        PlayList.clear();
    }

    public static void remove(int index) {
        PlayList.remove(index);
    }

    public static String getAllList() {
        StringBuilder list = new StringBuilder();
        String a;
        SongInfo info;
        for (int i = 0; i < PlayList.size(); i++) {
            info = PlayList.get(i);
            a = AllMusic.getMessage().getMusicPlay().getListMusic().getItem();
            a = a.replace("%index%", "" + (i + 1))
                    .replace("%MusicName%", info.getName())
                    .replace("%MusicAuthor%", info.getAuthor())
                    .replace("%MusicAl%", info.getAl())
                    .replace("%MusicAlia%", info.getAlia());
            list.append(a).append("\n");
        }
        String temp = list.toString();
        return temp.substring(0, temp.length() - 1);
    }

    public static boolean isHave(String ID) {
        if (NowPlayMusic != null && NowPlayMusic.getID().equalsIgnoreCase(ID))
            return true;
        for (SongInfo item : PlayList) {
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
            int le = (int) h.total_ms(b);
            SongInfo info = new SongInfo(AllMusic.getMessage().getCustom().getInfo(), arg, le);
            PlayList.add(info);
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic]§c歌曲信息解析错误");
            e.printStackTrace();
        }
    }
}

