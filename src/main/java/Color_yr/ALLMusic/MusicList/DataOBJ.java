package Color_yr.ALLMusic.MusicList;

import java.util.ArrayList;
import java.util.List;

public class DataOBJ {
    private data data;

    public List<String> getPlaylist() {
        List<String> list = new ArrayList<>();
        for (track item : data.getTracks()) {
            list.add(item.getId());
        }
        return list;
    }

    public String getName() {
        return data.getName();
    }
}
class track {
    private int id;

    public String getId() {
        return String.valueOf(id);
    }
}
class data {
    private List<track> tracks;
    private String name;

    public List<track> getTracks() {
        return tracks;
    }

    public String getName() {
        return name;
    }
}