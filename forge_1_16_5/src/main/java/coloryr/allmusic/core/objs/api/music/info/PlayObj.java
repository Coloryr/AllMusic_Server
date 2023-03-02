package coloryr.allmusic.core.objs.api.music.info;

import java.util.List;

public class PlayObj {
    private List<obj> data;
    private int code;

    public String getData() {
        if (data == null)
            return null;
        if (data.size() == 0)
            return null;
        obj obj = data.get(0);
        return obj.getUrl();
    }

    public int getCode() {
        return code;
    }
}

class obj {
    private String url;

    public String getUrl() {
        return url;
    }
}

