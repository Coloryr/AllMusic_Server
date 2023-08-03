package coloryr.allmusic.core.objs.message;

public class HelpObj {
    public HelpNormalObj Normal;
    public HelpAdminObj Admin;

    public boolean check() {
        boolean save = false;
        if (Normal == null || Normal.check()) {
            save = true;
            Normal = new HelpNormalObj();
        }
        if (Admin == null || Admin.check()) {
            save = true;
            Admin = new HelpAdminObj();
        }
        return save;
    }

    public void init() {
        Normal = HelpNormalObj.make();
        Admin = HelpAdminObj.make();
    }

    public static HelpObj make() {
        HelpObj obj = new HelpObj();
        obj.init();

        return obj;
    }
}
