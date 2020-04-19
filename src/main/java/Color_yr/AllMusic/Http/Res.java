package Color_yr.AllMusic.Http;

public class Res {
    private final String data;
    private final boolean ok;

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
