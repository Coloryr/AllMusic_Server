package Color_yr.AllMusic.musicAPI.songSearch;

import java.util.List;

public class SearchPage {
    private final List<SearchOBJ> resData;
    private final int maxpage;
    private int page = 0;

    public SearchPage(List<SearchOBJ> resData, int maxpage) {
        this.resData = resData;
        this.maxpage = maxpage;
    }

    public String getSong(int index) {
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
