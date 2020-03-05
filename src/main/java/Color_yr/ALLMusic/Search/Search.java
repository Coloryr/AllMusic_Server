package Color_yr.ALLMusic.Search;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Http.Get;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Search {
    private List<SearchOBJ> res = new ArrayList<>();
    private boolean done = false;
    private int page = 0;
    private int maxpage = 0;

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
                maxpage = res.size() / 10;
                done = true;
            }
        }
    }

    public String GetSong(int index) {
        return res.get(index).getID();
    }

    public SearchOBJ getRes(int a) {
        return res.get(a);
    }

    public boolean isDone() {
        return done;
    }

    public boolean nextPage() {
        if (page == maxpage)
            return false;
        page++;
        return true;
    }

    public boolean lastPage() {
        if (page == 0)
            return false;
        page--;
        return true;
    }

    public int getIndex() {
        int a = res.size() - page * 10;
        return Math.min(res.size(), 10);
    }

    public boolean haveNextPage() {
        return page < maxpage;
    }

    public boolean haveLastPage() {
        return page != 0;
    }

    public int getPage() {
        return page;
    }
}
