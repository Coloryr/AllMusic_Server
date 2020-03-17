package Color_yr.ALLMusic.MusicAPI.SongLyric;

public class ShowOBJ {
    private boolean haveT;
    private String lyric;
    private String tlyric;

    public ShowOBJ(boolean haveT, String lyric, String tlyric) {
        this.haveT = haveT;
        this.lyric = lyric;
        this.tlyric = tlyric;
    }

    public boolean isHaveT() {
        return haveT;
    }

    public String getTlyric() {
        return tlyric;
    }

    public String getLyric() {
        return lyric;
    }

    @Override
    public String toString() {
        if (lyric == null || lyric.isEmpty())
            return null;
        if (haveT) {
            if (tlyric != null && !tlyric.isEmpty())
                return lyric + "(" + tlyric + ")";
        }
        return lyric;
    }
}
