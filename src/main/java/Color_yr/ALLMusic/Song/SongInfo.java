package Color_yr.ALLMusic.Song;

public class SongInfo {
    private String Author;
    private String Name;
    private String ID;
    private String Alia;
    private String Call;
    private String Al;

    public SongInfo(String Author, String Name, String ID, String Alia, String Call, String Al) {
        this.Author = Author;
        this.Name = Name;
        this.ID = ID;
        this.Alia = Alia;
        this.Call = Call;
        this.Al = Al;
    }

    public String getAuthor() {
        return Author;
    }

    public String getName() {
        return Name;
    }

    public String getCall() {
        return Call;
    }

    public String getAlia() {
        return Alia;
    }

    public String getAl() {
        return Al;
    }

    public String getID() {
        return ID;
    }

    public String getInfo() {
        String info = "";
        if (Name != null && !Name.isEmpty()) {
            info += Name;
            if (Author != null && !Author.isEmpty()) {
                info += " | " + Author;
            }
            if (Alia != null && !Alia.isEmpty()) {
                info += " | " + Alia;
            }
            if (Al != null && Al.isEmpty()) {
                info += " | " + Al;
            }
        } else
            info = ID;
        info += " by:" + Call;
        return info;
    }
}
