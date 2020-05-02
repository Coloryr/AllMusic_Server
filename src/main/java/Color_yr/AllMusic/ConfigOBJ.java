package Color_yr.AllMusic;

import Color_yr.AllMusic.MusicPlay.SendInfo.SaveOBJ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigOBJ {
    private final String Music_Api1;
    private final String Music_Api2;

    private final int Music_Api;

    private final int MaxList;
    private final int MinVote;
    private final int Delay;

    private final List<String> Admin;
    private final List<String> NoMusicServer;
    private final List<String> NoMusicPlayer;
    private final List<String> BanMusic;
    private final List<String> PlayList;

    private final boolean PlayListSwitch;
    private final boolean PlayListRandom;
    private final boolean SendLyric;
    private final boolean NeedPermission;

    private final Map<String, SaveOBJ> InfoSave;

    private final String Version;

    public ConfigOBJ() {
        Music_Api1 = "https://api.imjad.cn/cloudmusic/";
        Music_Api2 = "http://localhost:4000";
        Music_Api = 1;
        MaxList = 10;
        MinVote = 3;
        Delay = 0;
        Admin = new ArrayList<String>() {{
            this.add("CONSOLE");
        }};
        NoMusicServer = new ArrayList<>();
        NoMusicPlayer = new ArrayList<>();
        BanMusic = new ArrayList<>();
        PlayList = new ArrayList<>();
        PlayListSwitch = true;
        PlayListRandom = true;
        SendLyric = true;
        NeedPermission = false;
        InfoSave = new HashMap<>();

        Version = AllMusic.Version;
    }

    public String getMusic_Api2() {
        return Music_Api2;
    }

    public int getMusic_Api() {
        return Music_Api;
    }

    public String getMusic_Api1() {
        return Music_Api1;
    }

    public boolean isNeedPermission() {
        return NeedPermission;
    }

    public boolean isSendLyric() {
        return SendLyric;
    }

    public SaveOBJ getInfoSave(String player) {
        return InfoSave.get(player);
    }

    public void setInfoSave(SaveOBJ obj, String player) {
        InfoSave.put(player, obj);
    }

    public boolean isPlayListRandom() {
        return PlayListRandom;
    }

    public List<String> getPlayList() {
        return PlayList;
    }

    public boolean isPlayListSwitch() {
        return PlayListSwitch;
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

    public List<String> getBanMusic() {
        return BanMusic;
    }

    public void addBanID(String ID) {
        if (!BanMusic.contains(ID))
            BanMusic.add(ID);
        AllMusic.save();
    }

    public void AddNoMusicPlayer(String ID) {
        if (!NoMusicPlayer.contains(ID))
            NoMusicPlayer.add(ID);
        AllMusic.save();
    }

    public void RemoveNoMusicPlayer(String ID) {
        NoMusicPlayer.remove(ID);
        AllMusic.save();
    }
}
