package com.coloryr.allmusic.server.core.objs.message;

public class HelpObj {
    public HelpNormalObj normal;
    public HelpAdminObj admin;

    public boolean check() {
        boolean save = false;
        if (normal == null || normal.check()) {
            save = true;
            normal = new HelpNormalObj();
        }
        if (admin == null || admin.check()) {
            save = true;
            admin = new HelpAdminObj();
        }
        return save;
    }

    public void init() {
        normal = HelpNormalObj.make();
        admin = HelpAdminObj.make();
    }

    public static HelpObj make() {
        HelpObj obj = new HelpObj();
        obj.init();

        return obj;
    }
}
