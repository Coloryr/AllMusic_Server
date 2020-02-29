package Color_yr.ALLMusic.Search;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Http.Get;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Search {
    private List<SearchOBJ> res = new ArrayList<>();
    private boolean done = false;

    public Search(String name) {
        String data = Get.realData(ALLMusic.Config.getSearch_Api1() + name);
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

    public String GetInfo() {
        StringBuilder Info = new StringBuilder();
        int a = 1;
        for (SearchOBJ item : res) {
            Info.append(a).append("->").append(item.getName()).append(" | ").append(item.getAuthor()).append(" | ").append(item.getAila()).append("\n");
            a++;
        }
        return Info.substring(0, Info.length() - 1);
    }

    public boolean isDone() {
        return done;
    }
}
