package Color_yr.ALLMusic.Http;

public class Res {
    private String data;
    private boolean ok;

    public Res(String data, boolean ok) {
        this.data = data;
        this.ok = ok;
    }

    public String getData() {
        return data;
    }

    public boolean isOk() {
        return ok;
    }
}
