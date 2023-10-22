package coloryr.allmusic.core.objs.config;

public class EconomyObj {
    public String MysqlUrl;
    public String Backend;
    public boolean Vault;

    public boolean check() {
        boolean res = MysqlUrl == null;

        return res;
    }

    public void init() {
        Backend = "Server1";
        MysqlUrl = "jdbc:mysql://localhost:3306/minecraft?autoReconnect=true&autoReconnectForPools=true";
        Vault = true;
    }

    public static EconomyObj make() {
        EconomyObj obj = new EconomyObj();
        obj.init();

        return obj;
    }
}
