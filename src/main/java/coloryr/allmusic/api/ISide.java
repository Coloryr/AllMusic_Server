package coloryr.allmusic.api;

public interface ISide {
    void send(String data, String player, Boolean isplay);

    void send(String data, Boolean isplay);

    int getAllPlayer();

    void bq(String data);

    void bqt(String data);

    boolean NeedPlay();

    void sendHudLyric(String data);

    void sendHudInfo(String data);

    void sendHudList(String data);

    void sendHudSaveAll();

    void clearHud(String player);

    void clearHudAll();

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
}
