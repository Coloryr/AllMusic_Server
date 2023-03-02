package coloryr.allmusic.core.objs.music;

import coloryr.allmusic.core.objs.SearchMusicObj;

import java.util.List;

public class SearchPageObj {
    private final List<SearchMusicObj> resData;
    private final int maxpage;
    private int page = 0;

    public SearchPageObj(List<SearchMusicObj> resData, int maxpage) {
        this.resData = resData;
        this.maxpage = maxpage;
    }

    public String getSong(int index) {
        return resData.get(index).id;
    }

    public SearchMusicObj getRes(int a) {
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
