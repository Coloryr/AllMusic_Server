package coloryr.allmusic.core.objs.config;

public class EconomyObj {
    public String MysqlUrl;
    public boolean Vault;

    public EconomyObj() {
        MysqlUrl = "jdbc:mysql://localhost:3306/minecraft?autoReconnect=true&autoReconnectForPools=true";
        Vault = true;
    }

    public boolean check() {
        boolean res = MysqlUrl == null;

        return res;
    }

}
