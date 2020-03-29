package Color_yr.ALLMusic.MusicAPI.MusicAPI2.GetMusicLyric;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.MusicAPI.SongLyric.ShowOBJ;
import Color_yr.ALLMusic.Utils.Function;
import com.google.gson.Gson;

import java.util.*;

public class LyricCheck {

    private Map<Integer, ShowOBJ> temp = new HashMap<>();
    private LyricOBJ obj;

    public LyricCheck(String obj) {
        this.obj = new Gson().fromJson(obj, LyricOBJ.class);
    }

    public Map<Integer, ShowOBJ> getTemp() {
        return temp;
    }

    public boolean Check() {
        String[] lyric;

        boolean haveT = false;
        if (!obj.isok())
            return false;
        lyric = obj.getLyric().split("\n");
        List<String> Tlyric = new ArrayList<>();
        if (obj.getTlyric() != null) {
            Tlyric = Arrays.asList(obj.getTlyric().split("\n"));
            haveT = true;
        }
        Map<Integer, String> temp = GetTime(Arrays.asList(lyric));
        if (haveT) {
            Map<Integer, String> temp1 = GetTime(Tlyric);
            for (Map.Entry<Integer, String> item : temp.entrySet()) {
                this.temp.put(item.getKey(), new ShowOBJ(haveT,
                        item.getValue(), temp1.get(item.getKey())));
            }
        } else {
            for (Map.Entry<Integer, String> item : temp.entrySet()) {
                this.temp.put(item.getKey(), new ShowOBJ(haveT,
                        item.getValue(), null));
            }
        }
        return true;
    }

    private Map<Integer, String> GetTime(List<String> lyric) {
        Map<Integer, String> Lyric = new HashMap<>();
        String min;
        String sec;
        String mil;
        int time;
        int milt;
        for (String s : lyric) {
            if (Function.countChar(Function.getString(s, "[", "]"), ':') > 1) {
                String[] a = s.split(":");
                min = a[0].substring(1);
                sec = a[1];
                mil = a[2];
            } else {
                min = Function.getString(s, "[", ":");
                sec = Function.getString(s, ":", ".");
                mil = Function.getString(s, ".", "]");
            }
            if (!Function.isInteger(min))
                continue;
            if (!Function.isInteger(sec))
                continue;
            if (!Function.isInteger(mil))
                continue;
            if (min.isEmpty() || sec.isEmpty() || mil.isEmpty())
                continue;
            milt = Integer.parseInt(mil);
            if (mil.length() == 3) {
                milt /= 10;
            }
            time = Integer.parseInt(min) * 60 * 1000 + Integer.parseInt(sec) * 1000 + milt * 10;
            if (time > 0 && time + ALLMusic.getConfig().getDelay() > 0)
                time += ALLMusic.getConfig().getDelay() * 10;
            Lyric.put(time, Function.getString(s, "]", null));
        }
        return Lyric;
    }
}
