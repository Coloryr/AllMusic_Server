package Color_yr.AllMusic.MusicAPI.MusicAPI2.GetMusicList;

import java.util.ArrayList;
import java.util.List;

public class DataOBJ {
    private result result;

    public List<String> getPlaylist() {
        List<String> list = new ArrayList<>();
        for (track item : result.getTracks()) {
            list.add(item.getId());
        }
        return list;
    }

    public String getName() {
        return result.getName();
    }
}

class track {
    private int id;

    public String getId() {
        return String.valueOf(id);
    }
}

class result {
    private List<track> tracks;
    private String name;

    public List<track> getTracks() {
        return tracks;
    }

    public String getName() {
        return name;
    }
}
