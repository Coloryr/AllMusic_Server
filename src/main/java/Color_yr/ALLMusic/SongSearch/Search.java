package Color_yr.ALLMusic.SongSearch;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Http.HttpGet;
import Color_yr.ALLMusic.Http.Res;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Search {
    private List<SearchOBJ> resData = new ArrayList<>();
    private boolean done = false;
    private int page = 0;
    private int maxpage = 0;

    public Search(String[] name) {
        StringBuilder name1 = new StringBuilder();
        for (int a = 1; a < name.length; a++) {
            name1.append(name[a]).append(" ");
        }
        String MusicName = name1.toString();
        MusicName = MusicName.substring(0, MusicName.length() - 1);
        Res res = HttpGet.realData(ALLMusic.Config.getNetease_Api() + "search?type=song&keyword=", MusicName);
        if (res != null && res.isOk()) {
            DataOBJ obj = new Gson().fromJson(res.getData(), DataOBJ.class);
            if (obj != null && obj.isok()) {
                List<songs> res1 = obj.getResult();
                SearchOBJ item;
                for (songs temp : res1) {
                    item = new SearchOBJ(String.valueOf(temp.getId()), temp.getName(), temp.getArtists(), temp.getAlbum());
                    resData.add(item);
                }
                maxpage = res1.size() / 10;
                done = true;
            } else {
                ALLMusic.log.warning("§d[ALLMusic]§c歌曲搜索出现错误");
            }
        }
    }

    public String GetSong(int index) {
        return resData.get(index).getID();
    }

    public SearchOBJ getRes(int a) {
        return resData.get(a);
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
        int a = resData.size() - page * 10;
        return Math.min(a, 10);
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
