package coloryr.allmusic.core.objs.api.music.search;

import java.util.List;

public class SearchDataObj {
    private result result;

    public boolean isOk() {
        return result != null && result.getSongs() != null;
    }

    public List<songs> getResult() {
        return result.getSongs();
    }
}

class result {
    private List<songs> songs;

    public List<songs> getSongs() {
        return songs;
    }
}


class artists {
    private String name;

    public String getName() {
        return name;
    }
}

class album {
    private String name;

    public String getName() {
        return name;
    }
}