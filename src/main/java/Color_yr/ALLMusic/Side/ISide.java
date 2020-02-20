package Color_yr.ALLMusic.Side;

public interface ISide {
    void Send(String player, String data, Boolean isplay);
    void Send(String data, Boolean isplay);
    int GetAllPlayer();
    void SendLyric(String data);
}
