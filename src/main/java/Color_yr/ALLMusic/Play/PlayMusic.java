package Color_yr.ALLMusic.Play;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Lyric.LyricDo;
import Color_yr.ALLMusic.Song.GetInfo;
import Color_yr.ALLMusic.Song.SongInfo;
import Color_yr.ALLMusic.Utils.logs;

import java.util.ArrayList;
import java.util.List;

public class PlayMusic {

    public static final List<String> VotePlayer = new ArrayList<>();
    public static final List<String> NowPlayPlayer = new ArrayList<>();
    private static final List<SongInfo> PlayList = new ArrayList<>();
    public static int VoteTime = 0;
    public static int MusicAllTime = 0;
    public static int MusicNowTime = 0;
    public static SongInfo NowPlayMusic;

    public static LyricDo Lyric;
    public static boolean haveLyric;
    public static String nowLyric = "";
    private static PlayGo PlayGo;

    public static void stop() {
        if (PlayGo != null && PlayGo.isAlive()) {
            PlayGo.close();
            PlayGo.stop();
        }
    }

    public static void start() {
        PlayGo = new PlayGo();
        PlayGo.start();
    }

    public static void addMusic(String ID, String player, boolean isList) {
        synchronized (PlayList) {
            if (isHave(ID))
                return;
            ALLMusic.Side.bq("§d[ALLMusic]§2" + player +
                    "点歌" + ID);
            logs.log_write("玩家：" + player + " 点歌：" + ID);
            SongInfo info = new SongInfo(null, null, ID, null, player, null, isList);
            try {
                info = GetInfo.Get(ID, player, isList);
            } catch (Exception e) {
                ALLMusic.log.warning("§d[ALLMusic]§c歌曲信息解析错误");
                e.printStackTrace();
            }
            PlayList.add(info);
        }
    }

    public static SongInfo getMusic(int index) {
        synchronized (PlayList) {
            return PlayList.get(index);
        }
    }

    public static int getSize() {
        synchronized (PlayList) {
            return PlayList.size();
        }
    }

    public static List<SongInfo> getList() {
        return new ArrayList<>(PlayList);
    }

    public static void clear() {
        PlayList.clear();
    }

    public static void remove(int index) {
        synchronized (PlayList) {
            PlayList.remove(index);
        }
    }

    public static String getAllList() {
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < PlayList.size(); i++) {
            SongInfo info = PlayList.get(i);
            list.append("§2").append(i + 1).append("->").append(info.getInfo()).append("\n");
        }
        return list.toString();
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
}

