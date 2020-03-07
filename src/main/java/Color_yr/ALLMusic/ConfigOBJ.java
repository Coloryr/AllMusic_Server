package Color_yr.ALLMusic;

import Color_yr.ALLMusic.VV.VVSaveOBJ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigOBJ {
    private String Music_Api1;
    private String Lyric_Api1;
    private String Info_Api1;
    private String List_Api1;
    private String Search_Api1;

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

    private boolean VexView;
    private Map<String, VVSaveOBJ> VVSave;

    private String Version;

    public ConfigOBJ() {
        Music_Api1 = "http://music.163.com/song/media/outer/url?id=";
        Lyric_Api1 = "https://api.imjad.cn/cloudmusic/?type=lyric&id=";
        Info_Api1 = "https://api.imjad.cn/cloudmusic/?type=detail&id=";
        List_Api1 = "https://api.imjad.cn/cloudmusic/?type=playlist&id=";
        Search_Api1 = "https://music.163.com/api/search/get/web?type=1&s=";
        MaxList = 10;
        MinVote = 3;
        Delay = 2;
        Admin = new ArrayList<>();
        NoMusicServer = new ArrayList<>();
        NoMusicPlayer = new ArrayList<>();
        BanMusic = new ArrayList<>();
        PlayList = new ArrayList<>();
        PlayListSwitch = true;
        PlayListRandom = true;
        SendLyric = true;
        NeedPermission = false;
        VexView = false;
        VVSave = new HashMap<>();

        Version = ALLMusic.Version;
    }

    public boolean isNeedPermission() {
        return NeedPermission;
    }

    public boolean isSendLyric() {
        return SendLyric;
    }

    public VVSaveOBJ getVVSave(String player) {
        return VVSave.get(player);
    }

    public void setVVSave(VVSaveOBJ obj, String player) {
        VVSave.put(player, obj);
    }

    public boolean isVexView() {
        return VexView;
    }

    public String getSearch_Api1() {
        return Search_Api1;
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

    public String getInfo_Api1() {
        return Info_Api1;
    }

    public String getList_Api1() {
        return List_Api1;
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
