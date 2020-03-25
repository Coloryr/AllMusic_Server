package Color_yr.ALLMusic.MusicAPI.SongInfo;

import Color_yr.ALLMusic.ALLMusic;

public class SongInfo {
    private String Author;
    private String Name;
    private String ID;
    private String Alia;
    private String Call;
    private String Al;

    private int Length;

    private boolean isList;

    public SongInfo(String Author, String Name, String ID, String Alia, String Call, String Al, boolean isList, int Length) {
        this.Author = Author;
        this.Name = Name;
        this.ID = ID;
        this.Alia = Alia;
        this.Call = Call;
        this.Al = Al;

        this.isList = isList;
        this.Length = Length;
    }

    public String getAl() {
        return Al;
    }

    public String getAlia() {
        return Alia;
    }

    public String getCall() {
        return Call;
    }

    public String getAuthor() {
        return Author;
    }

    public int getLength() {
        return Length;
    }

    public boolean isList() {
        return isList;
    }

    public String getName() {
        return Name;
    }

    public String getID() {
        return ID;
    }

    public String getInfo() {
        String info = ALLMusic.Message.getMusicPlay().getMusicInfo();
        info = info.replace("%MusicName%", Name)
                .replace("%MusicAuthor%", Author)
                .replace("%MusicAl%", Al)
                .replace("%MusicAlia%", Alia)
                .replace("%PlayerName%", Call);
        return info;
    }
}
