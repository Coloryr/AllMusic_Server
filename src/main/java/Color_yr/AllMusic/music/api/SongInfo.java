package Color_yr.AllMusic.music.api;

import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.music.api.obj.music.trialinfo.freeTrialInfo;

public class SongInfo {
    private String author;
    private String name;
    private String id;
    private String alia;
    private String call;
    private String al;
    private String playerUrl;
    private String picUrl;
    private boolean isTrial;
    private freeTrialInfo trialInfo;

    private int length;

    private boolean isList;

    public SongInfo(String Name, String Url, int Length) {
        this.length = Length;
        playerUrl = Url;
        this.name = Name;
        id = alia = call = al = author = "";
        isList = false;
    }

    public SongInfo(String Author, String Name, String ID, String Alia, String Call, String Al,
                    boolean isList, int Length, String picUrl, boolean isTrial, freeTrialInfo trialInfo) {
        this.author = Author;
        this.name = Name;
        this.id = ID;
        this.alia = Alia;
        this.call = Call;
        this.al = Al;
        this.picUrl = picUrl;
        this.isList = isList;
        this.length = Length;
        this.isTrial = isTrial;
        this.trialInfo = trialInfo;
    }

    public boolean isTrial() {
        return isTrial;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public freeTrialInfo getTrialInfo() {
        return trialInfo;
    }

    public String getPlayerUrl() {
        return playerUrl;
    }

    public String getAl() {
        return al == null ? "" : al;
    }

    public String getAlia() {
        return alia == null ? "" : alia;
    }

    public String getCall() {
        return call == null ? "" : call;
    }

    public String getAuthor() {
        return author == null ? "" : author;
    }

    public int getLength() {
        return length;
    }

    public boolean isList() {
        return isList;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public String getID() {
        return id;
    }

    public String getInfo() {
        String info = AllMusic.getMessage().getMusicPlay().getMusicInfo();
        info = info.replace("%MusicName%", name == null ? "" : name)
                .replace("%MusicAuthor%", author == null ? "" : author)
                .replace("%MusicAl%", al == null ? "" : al)
                .replace("%MusicAlia%", alia == null ? "" : alia)
                .replace("%PlayerName%", call == null ? "" : call);
        return info;
    }

    public boolean isNull() {
        return name == null;
    }
}
