package coloryr.allmusic.message;

public class HelpObj {
    public HelpNormalObj Normal;
    public HelpAdminObj Admin;

    public HelpObj() {
        Normal = new HelpNormalObj();
        Admin = new HelpAdminObj();
    }

    public boolean check() {
        boolean save = false;
        if (Normal == null) {
            save = true;
            Normal = new HelpNormalObj();
        }
        if (Admin == null) {
            save = true;
            Admin = new HelpAdminObj();
        }
        return save;
    }
}
