package Color_yr.ALLMusic.Side;

import net.md_5.bungee.api.chat.ClickEvent;

public interface ISide {
    void Send(String player, String data, Boolean isplay);

    void Send(String data, Boolean isplay);

    int GetAllPlayer();

    void SendLyric(String data);

    void bq(String data);

    void save();

    boolean NeedPlay();

    void SendMessage(Object obj, String Message);

    void SendMessage(Object obj, String Message, ClickEvent.Action action, String Command);

    void RunTask(Runnable run);

    void reload();

    boolean checkPermission(String player, String permission);
}
