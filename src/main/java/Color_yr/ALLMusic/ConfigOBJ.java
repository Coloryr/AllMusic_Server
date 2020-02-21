package Color_yr.ALLMusic;

import java.util.ArrayList;
import java.util.List;

public class ConfigOBJ {
    private String Music_Api1;
    private String Lyric_Api1;
    private String Info_Api1;

    private int MaxList;
    private int MinVote;
    private int Delay;

    private List<String> Admin;
    private List<String> NoMusicServer;
    private List<String> NoMusicPlayer;
    private List<String> BanMusic;

    private String Version;

    public ConfigOBJ() {
        Music_Api1 = "http://music.163.com/song/media/outer/url?id=";
        Lyric_Api1 = "https://api.imjad.cn/cloudmusic/?type=lyric&id=";
        Info_Api1 = "https://api.imjad.cn/cloudmusic/?type=detail&id=";
        MaxList = 10;
        MinVote = 3;
        Delay = 2;
        Admin = new ArrayList<>();
        NoMusicServer = new ArrayList<>();
        NoMusicPlayer = new ArrayList<>();
        BanMusic = new ArrayList<>();

        Version = ALLMusic.Version;
    }

    public String getInfo_Api1() {
        return Info_Api1;
    }

    public String getLyric_Api1() {
        return Lyric_Api1;
    }

    public int getDelay() {
        return Delay;
    }

    public String getVersion() {
        return Version;
    }

    public int getMaxList() {
        return MaxList;
    }

    public int getMinVote() {
        return MinVote;
    }

    public List<String> getAdmin() {
        return Admin;
    }

    public List<String> getNoMusicPlayer() {
        return NoMusicPlayer;
    }

    public List<String> getNoMusicServer() {
        return NoMusicServer;
    }

    public String getMusic_Api1() {
        return Music_Api1;
    }

    public List<String> getBanMusic() {
        return BanMusic;
    }

    public void addBanID(String ID) {
        if (!BanMusic.contains(ID))
            BanMusic.add(ID);
        ALLMusic.Side.save();
    }

    public void AddNoMusicPlayer(String ID) {
        if (!NoMusicPlayer.contains(ID))
            NoMusicPlayer.add(ID);
        ALLMusic.Side.save();
    }
    public void RemoveNoMusicPlayer(String ID) {
        NoMusicPlayer.remove(ID);
        ALLMusic.Side.save();
    }
}
