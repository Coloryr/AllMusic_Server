package Color_yr.ALLMusic.PlayList;

import java.util.ArrayList;
import java.util.List;

public class DataOBJ {
    private PlayList playlist;

    public List<String> getPlaylist() {
        List<String> list = new ArrayList<>();
        for (track item : playlist.getTrackIds()) {
            list.add(item.getId());
        }
        return list;
    }
}

class PlayList {
    private List<track> trackIds;

    public List<track> getTrackIds() {
        return trackIds;
    }
}

class track {
    private int id;

    public String getId() {
        return String.valueOf(id);
    }
}