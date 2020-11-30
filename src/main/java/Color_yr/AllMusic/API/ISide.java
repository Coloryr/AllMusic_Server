package Color_yr.AllMusic.API;

public interface ISide {
    void Send(String player, String data, Boolean isplay);

    void Send(String data, Boolean isplay);

    int GetAllPlayer();

    void bq(String data);

    void bqt(String data);

    boolean NeedPlay();

    boolean SendHudLyric(String data);

    boolean SendHudInfo(String data);

    boolean SendHudList(String data);

    void SendHudSaveAll();

    void ClearHud(String player);

    void ClearHudAll();

    void SendMessaget(Object obj, String Message);

    void SendMessage(Object obj, String Message);

    void RunTask(Runnable run);

    void reload();

    boolean checkPermission(String player, String permission);
}
