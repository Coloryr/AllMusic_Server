package Color_yr.ALLMusic;

import Color_yr.ALLMusic.Side.SideBukkit.VVSaveOBJ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigOBJ {
    private String Music_Api1;
    private String Music_Api2;
    private String Music_Api3;

    private int Music_Api;

    private int MaxList;
    private int MinVote;
    private int Delay;

    private List<String> Admin;
    private List<String> NoMusicServer;
    private List<String> NoMusicPlayer;
    private List<String> BanMusic;
    private List<String> PlayList;

    private boolean PlayListSwitch;
    private boolean PlayListRandom;
    private boolean SendLyric;
    private boolean NeedPermission;
    private boolean ModCheck;

    private Map<String, VVSaveOBJ> VVSave;

    private String Version;

    public ConfigOBJ() {
        Music_Api1 = "https://v1.itooi.cn/netease/";
        Music_Api2 = "https://api.imjad.cn/cloudmusic/";
        Music_Api3 = "http://localhost:3000/";
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
        ModCheck = false;
        VVSave = new HashMap<>();

        Version = ALLMusic.Version;
    }

    public String getMusic_Api3() {
        return Music_Api3;
    }

    public boolean isModCheck() {
        return ModCheck;
    }

    public int getMusic_Api() {
        return Music_Api;
    }

    public String getMusic_Api2() {
        return Music_Api2;
    }

    public boolean isNeedPermission() {
        return NeedPermission;
    }

    public boolean isSendLyric() {
        return SendLyric;
    }

    public String getMusic_Api1() {
        return Music_Api1;
    }

    public VVSaveOBJ getVVSave(String player) {
        return VVSave.get(player);
    }

    public void setVVSave(VVSaveOBJ obj, String player) {
        VVSave.put(player, obj);
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
        ALLMusic.save();
    }

    public void AddNoMusicPlayer(String ID) {
        if (!NoMusicPlayer.contains(ID))
            NoMusicPlayer.add(ID);
        ALLMusic.save();
    }

    public void RemoveNoMusicPlayer(String ID) {
        NoMusicPlayer.remove(ID);
        ALLMusic.save();
    }
}
