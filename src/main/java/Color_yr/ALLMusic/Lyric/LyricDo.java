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

    public void Check() {
        String[] lyric;

        boolean haveT = false;
        if (obj.getLyric() == null)
            return;
        lyric = obj.getLyric().split("\n");
        List<String> tlyric = new ArrayList<>();
        if (obj.getTlyric() != null) {
            tlyric = Arrays.asList(obj.getTlyric().split("\n"));
            haveT = true;
        }
        String min;
        String sec;
        String Tlyric = null;
        String temp;
        String temp2;
        int time;
        for (int now = 0; now < lyric.length; now++) {
            if (Function.countChar(lyric[now], ':') > 1) {
                String[] a = lyric[now].split(":");
                min = a[0].substring(1);
                sec = a[1];
            } else {
                min = Function.getString(lyric[now], "[", ":");
                sec = Function.getString(lyric[now], ":", ".");
            }
            if (!Function.isInteger(min))
                continue;
            if (!Function.isInteger(sec))
                continue;
            time = Integer.parseInt(min) * 60 + Integer.parseInt(sec);
            if (time > 0)
                time -= ALLMusic.Config.getDelay();
            if (haveT) {
                temp = Function.getString(lyric[now], "[", "]");
                for (String item : tlyric) {
                    temp2 = Function.getString(item, "[", "]");
                    if (temp.startsWith(temp2) || temp2.startsWith(temp)) {
                        Tlyric = Function.getString(item, "]", null);
                        break;
                    } else
                        Tlyric = null;
                }
            }
            Lyric.put(time, new ShowOBJ(time, haveT,
                    Function.getString(lyric[now], "]", null), Tlyric));
        }
    }

    public ShowOBJ checkTime(int playNow) {
        if (Lyric.containsKey(playNow))
            return Lyric.get(playNow);
        return null;
    }
}
