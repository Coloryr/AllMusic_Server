package coloryr.allmusic.core.objs.api;

public class EncResObj {
    public String params;
    public String encSecKey;

    public EncResObj(String params, String encSecKey) {
        this.encSecKey = encSecKey;
        this.params = params;
    }
}
