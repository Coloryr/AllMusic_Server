package coloryr.allmusic.music.api.http;

public class HttpRes {
    private final String data;
    private final boolean ok;

    public HttpRes(String data, boolean ok) {
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
