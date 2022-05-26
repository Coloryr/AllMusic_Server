package coloryr.allmusic;

import coloryr.allmusic.hud.obj.SaveOBJ;

import java.util.ArrayList;
import java.util.List;

public class ConfigOBJ {
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

    private SaveOBJ DefaultHud;

    private String Version;

    private boolean UseCost;
    private int SearchCost;
    private int AddMusicCost;

    private int DefaultAddMusic;

    private String LoginUser;
    private String MusicBR;

    public ConfigOBJ() {
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
        DefaultHud = new SaveOBJ();

        UseCost = false;
        SearchCost = 20;
        AddMusicCost = 10;

        DefaultAddMusic = 0;
        LoginUser = "";

        MusicBR = "320000";

        Version = AllMusic.version;
    }

    public String getMusicBR() {
        return MusicBR;
    }

    public boolean isUseCost() {
        return UseCost;
    }

    public String getLoginUser() {
        return LoginUser;
    }

    public int getDefaultAddMusic() {
        return DefaultAddMusic;
    }

    public int getAddMusicCost() {
        return AddMusicCost;
    }

    public int getSearchCost() {
        return SearchCost;
    }

    public SaveOBJ getDefaultHud() {
        return DefaultHud;
    }

    public void setDefaultHud(SaveOBJ defaultHud) {
        DefaultHud = defaultHud;
    }

    public boolean isNeedPermission() {
        return NeedPermission;
    }

    public boolean isSendLyric() {
        return SendLyric;
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

    public boolean check() {
        boolean saveConfig = false;
        if (PlayList == null) {
            saveConfig = true;
            PlayList = new ArrayList<>();
        }
        if (Admin == null) {
            saveConfig = true;
            Admin = new ArrayList<>();
        }
        if (BanMusic == null) {
            saveConfig = true;
            BanMusic = new ArrayList<>();
        }
        if (DefaultHud == null) {
            saveConfig = true;
            DefaultHud = new SaveOBJ();
        }
        if (NoMusicPlayer == null) {
            saveConfig = true;
            NoMusicPlayer = new ArrayList<>();
        }
        if (NoMusicServer == null) {
            saveConfig = true;
            NoMusicServer = new ArrayList<>();
        }
        return saveConfig;
    }
}
