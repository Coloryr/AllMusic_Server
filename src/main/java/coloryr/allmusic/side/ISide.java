package coloryr.allmusic.side;

import coloryr.allmusic.music.api.SongInfo;
import coloryr.allmusic.music.play.MusicObj;

public interface ISide {
    void send(String data, String player, Boolean isplay);

    int getAllPlayer();

    void bq(String data);

    void bqt(String data);

    boolean needPlay();

    void sendStop();

    void sendStop(String name);

    void sendMusic(String url);

    void sendPic(String url);

    void sendHudLyric(String data);

    void sendHudInfo(String data);

    void sendHudList(String data);

    void sendHudSaveAll();

    void sendBar(String data);

    void clearHud(String player);

    void clearHud();

    void sendMessaget(Object obj, String message);

    void sendMessage(Object obj, String message);

    void sendMessageRun(Object obj, String message, String end, String command);

    void sendMessageSuggest(Object obj, String message, String end, String command);

    void runTask(Runnable run);

    void reload();

    boolean checkPermission(String player, String permission);

    void task(Runnable run, int delay);

    void updateInfo();

    void updateLyric();

    void ping();

    boolean onMusicPlay(SongInfo obj);

    boolean onMusicAdd(Object obj, MusicObj music);
}
