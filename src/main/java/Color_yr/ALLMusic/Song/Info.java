package Color_yr.ALLMusic.Song;

public class Info {
    private String Author;
    private String Name;
    private String ID;
    private String Alia;
    private String Call;

    public Info(String Author, String Name, String ID, String Alia, String Call) {
        this.Author = Author;
        this.Name = Name;
        this.ID = ID;
        this.Alia = Alia;
        this.Call = Call;
    }

    public String getID() {
        return ID;
    }

    public String getInfo() {
        String info = "";
        if (Name != null && !Name.isEmpty()) {
            info += Name;
            if (Author != null && !Author.isEmpty()) {
                info += " " + Author;
            }
            if (Alia != null && !Alia.isEmpty()) {
                info += " " + Alia;
            }
        } else
            info = ID;
        info += " by:" + Call;
        return info;
    }
}
