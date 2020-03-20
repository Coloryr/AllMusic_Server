package Color_yr.ALLMusic.MusicAPI.MusicAPI1.GetMusicSearch;

import java.util.List;

public class SearchDataOBJ {
    private data data;

    public boolean isok() {
        return data != null;
    }

    public List<songs> getResult() {
        return data.getSongs();
    }
}

class data {
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