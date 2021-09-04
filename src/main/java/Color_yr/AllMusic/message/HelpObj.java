package Color_yr.AllMusic.message;

public class HelpObj {
    private HelpNormalObj Normal;
    private HelpAdminObj Admin;

    public HelpAdminObj getAdmin() {
        return Admin;
    }

    public HelpNormalObj getNormal() {
        return Normal;
    }

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
