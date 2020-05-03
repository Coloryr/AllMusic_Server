package Color_yr.AllMusic.API;

import net.md_5.bungee.api.chat.ClickEvent;

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

    void SendMessage(Object obj, String Message, ClickEvent.Action action, String Command);

    void RunTask(Runnable run);

    void reload();

    boolean checkPermission(String player, String permission);
}
