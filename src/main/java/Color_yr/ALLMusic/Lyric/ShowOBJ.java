package Color_yr.ALLMusic.Lyric;

public class ShowOBJ {
    private int time;
    private boolean haveT;
    private String lyric;
    private String tlyric;

    public ShowOBJ(int time, boolean haveT, String lyric, String tlyric) {
        this.time = time;
        this.haveT = haveT;
        this.lyric = lyric;
        this.tlyric = tlyric;
    }

    public String getLyric() {
        return lyric;
    }

    public boolean isHaveT() {
        return haveT;
    }

    public int getTime() {
        return time;
    }

    public String getTlyric() {
        return tlyric;
    }

    @Override
    public String toString() {
        if (lyric == null || lyric.isEmpty())
            return null;
        if (haveT) {
            if (tlyric == null || !tlyric.isEmpty())
                return lyric + (haveT ? "(" + tlyric + ")" : null);
        }
        return lyric;
    }
}
