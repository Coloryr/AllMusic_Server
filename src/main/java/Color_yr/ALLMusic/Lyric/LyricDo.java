package Color_yr.ALLMusic.Lyric;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Utils.Function;
import com.google.gson.Gson;

import java.util.*;

public class LyricDo {

    private Map<Integer, ShowOBJ> Lyric = new HashMap<>();
    private LyricOBJ obj;

    public LyricDo(String obj) {
        this.obj = new Gson().fromJson(obj, LyricOBJ.class);
    }

    public LyricDo(Map<Integer, ShowOBJ> Lyric) {
        if (Lyric == null)
            return;
        this.Lyric = Lyric;
    }

    public void Check() {
        String[] lyric;

        boolean haveT = false;
        if (obj.getLyric() == null)
            return;
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
                Lyric.put(item.getKey(), new ShowOBJ(haveT,
                        item.getValue(), temp1.get(item.getKey())));
            }
        } else {
            for (Map.Entry<Integer, String> item : temp.entrySet()) {
                Lyric.put(item.getKey(), new ShowOBJ(haveT,
                        item.getValue(), null));
            }
        }
    }

    public ShowOBJ checkTime(int playNow) {
        if (Lyric.containsKey(playNow))
            return Lyric.get(playNow);
        return null;
    }

    public Map<Integer, ShowOBJ> getLyric() {
        return Lyric;
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
            if (time > 0 && time + ALLMusic.Config.getDelay() > 0)
                time += ALLMusic.Config.getDelay() * 10;
            Lyric.put(time, Function.getString(s, "]", null));
        }
        return Lyric;
    }
}
