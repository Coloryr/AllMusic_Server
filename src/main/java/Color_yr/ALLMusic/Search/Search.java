package Color_yr.ALLMusic.Search;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Http.Get;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Search {
    private List<SearchOBJ> res = new ArrayList<>();
    private boolean done = false;

    public Search(String[] name) {
        StringBuilder name1 = new StringBuilder();
        for (int a = 1; a < name.length; a++) {
            name1.append(name[a]).append(" ");
        }
        String data = name1.toString();
        data = data.substring(0, data.length() - 1);
        data = Get.realData(ALLMusic.Config.getSearch_Api1(), data);
        if (data != null) {
            DataOBJ obj = new Gson().fromJson(data, DataOBJ.class);
            if (obj != null && obj.getResult() != null) {
                List<songs> res1 = obj.getResult();
                SearchOBJ item;
                for (songs temp : res1) {
                    item = new SearchOBJ(String.valueOf(temp.getId()), temp.getName(), temp.getArtists(), temp.getAlbum());
                    res.add(item);
                }
                done = true;
            }
        }
    }

    public String GetSong(int index) {
        return res.get(index).getID();
    }

    public List<SearchOBJ> getRes() {
        return res;
    }

    public boolean isDone() {
        return done;
    }
}
