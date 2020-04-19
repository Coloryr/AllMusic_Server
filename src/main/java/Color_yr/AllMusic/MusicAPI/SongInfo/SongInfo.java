package Color_yr.AllMusic.MusicAPI.SongInfo;

import Color_yr.AllMusic.AllMusic;

public class SongInfo {
    private final String Author;
    private final String Name;
    private final String ID;
    private final String Alia;
    private final String Call;
    private final String Al;

    private final int Length;

    private final boolean isList;

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
        String info = AllMusic.getMessage().getMusicPlay().getMusicInfo();
        info = info.replace("%MusicName%", Name)
                .replace("%MusicAuthor%", Author)
                .replace("%MusicAl%", Al)
                .replace("%MusicAlia%", Alia)
                .replace("%PlayerName%", Call);
        return info;
    }

    public boolean isNull() {
        return Name == null;
    }
}
