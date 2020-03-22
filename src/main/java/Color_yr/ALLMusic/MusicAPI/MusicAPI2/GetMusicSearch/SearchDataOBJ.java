package Color_yr.ALLMusic.MusicAPI.MusicAPI2.GetMusicSearch;

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