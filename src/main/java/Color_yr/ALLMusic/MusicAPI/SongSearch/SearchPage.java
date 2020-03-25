package Color_yr.ALLMusic.MusicAPI.SongSearch;

import java.util.List;

public class SearchPage {
    private List<SearchOBJ> resData;
    private int page = 0;
    private int maxpage;

    public SearchPage(List<SearchOBJ> resData, int maxpage) {
        this.resData = resData;
        this.maxpage = maxpage;
    }

    public String GetSong(int index) {
        return resData.get(index).getID();
    }

    public SearchOBJ getRes(int a) {
        return resData.get(a);
    }

    public boolean nextPage() {
        if (!haveNextPage())
            return false;
        page++;
        return true;
    }

    public boolean lastPage() {
        if (!haveLastPage())
            return false;
        page--;
        return true;
    }

    public int getIndex() {
        int a = resData.size() - page * 10;
        return Math.min(a, 10);
    }

    public boolean haveNextPage() {
        return page != (maxpage - 1);
    }

    public boolean haveLastPage() {
        return page != 0;
    }

    public int getPage() {
        return page;
    }
}
