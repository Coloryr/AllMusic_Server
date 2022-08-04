package coloryr.allmusic.music.lyric;

import coloryr.allmusic.AllMusic;
import coloryr.allmusic.music.api.obj.music.lyric.WLyricOBJ;
import coloryr.allmusic.utils.Function;

import java.util.*;

public class LyricDo {

    private final Map<Integer, LyricItem> temp = new HashMap<>();
    public boolean isHave = false;

    public Map<Integer, LyricItem> getTemp() {
        return temp;
    }

    public boolean check(WLyricOBJ obj) {
        String[] lyric;

        boolean haveT = false;
        if (!obj.isok())
            return true;
        else if (obj.isNone())
            return false;
        lyric = obj.getLyric().split("\n");
        List<String> Tlyric = new ArrayList<>();
        if (obj.getTlyric() != null) {
            Tlyric = Arrays.asList(obj.getTlyric().split("\n"));
            haveT = true;
        }
        Map<Integer, String> temp = getTime(Arrays.asList(lyric));
        if (haveT) {
            Map<Integer, String> temp1 = getTime(Tlyric);
            for (Map.Entry<Integer, String> item : temp.entrySet()) {
                this.temp.put(item.getKey(), new LyricItem(haveT,
                        item.getValue(), temp1.get(item.getKey())));
            }
        } else {
            for (Map.Entry<Integer, String> item : temp.entrySet()) {
                this.temp.put(item.getKey(), new LyricItem(haveT,
                        item.getValue(), null));
            }
        }
        isHave = true;
        return false;
    }

    private Map<Integer, String> getTime(List<String> lyric) {
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
            if (time > 0 && time + AllMusic.getConfig().getDelay() > 0)
                time += AllMusic.getConfig().getDelay() * 10;
            Lyric.put(time, Function.getString(s, "]", null));
        }
        return Lyric;
    }
}
