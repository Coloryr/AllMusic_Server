package Color_yr.AllMusic.music.api.obj.music.search;

import java.util.List;

public class SearchDataOBJ {
    private result result;

    public boolean isok() {
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