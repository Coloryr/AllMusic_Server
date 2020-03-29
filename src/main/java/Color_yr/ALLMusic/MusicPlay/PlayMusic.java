package Color_yr.ALLMusic.MusicPlay;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.MusicAPI.SongInfo.SongInfo;
import Color_yr.ALLMusic.MusicAPI.SongLyric.LyricSave;
import Color_yr.ALLMusic.MusicAPI.SongLyric.ShowOBJ;
import Color_yr.ALLMusic.Utils.logs;

import java.util.ArrayList;
import java.util.List;

public class PlayMusic {

    public static final List<SongInfo> PlayList = new ArrayList<>();
    public static int VoteTime = 0;
    public static int MusicAllTime = 0;
    public static int MusicNowTime = 0;
    public static SongInfo NowPlayMusic;

    public static LyricSave Lyric;
    public static ShowOBJ nowLyric;
    private static PlayGo PlayGo;

    public static void stop() {
        if (PlayGo != null && PlayGo.isAlive()) {
            PlayGo.closeTimer();
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
            String text = ALLMusic.getMessage().getMusicPlay().getPlayerAdd();
            text = text.replace("%PlayerName%", player)
                    .replace("%MusicID%", ID);
            ALLMusic.Side.bq(text);
            logs.logWrite("玩家：" + player + " 点歌：" + ID);
            try {
                SongInfo info = ALLMusic.Music.GetMusic(ID, player, isList);
                if (info != null) {
                    PlayList.add(info);
                    String data = ALLMusic.getMessage().getMusicPlay().getAddMusic();
                    data = data.replace("%MusicName%", info.getName())
                            .replace("%MusicAuthor%", info.getAuthor())
                            .replace("%MusicAl%", info.getAl())
                            .replace("%MusicAlia%", info.getAlia());
                    ALLMusic.Side.bq(data);
                } else {
                    String data = ALLMusic.getMessage().getMusicPlay().getNoCanPlay();
                    ALLMusic.Side.bq(data.replace("%MusicID%", ID));
                }
                if (ALLMusic.getConfig().isPlayListSwitch()
                        && (PlayMusic.NowPlayMusic != null && PlayMusic.NowPlayMusic.isList())) {
                    PlayMusic.MusicAllTime = 1;
                    if (!isList)
                        ALLMusic.Side.bq(ALLMusic.getMessage().getMusicPlay().getSwitch());
                }
            } catch (Exception e) {
                ALLMusic.log.warning("§d[ALLMusic]§c歌曲信息解析错误");
                e.printStackTrace();
            }
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
        String a;
        SongInfo info;
        for (int i = 0; i < PlayList.size(); i++) {
            info = PlayList.get(i);
            a = ALLMusic.getMessage().getMusicPlay().getListMusic().getItem();
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
}

