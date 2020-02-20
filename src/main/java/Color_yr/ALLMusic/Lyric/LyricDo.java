package Color_yr.ALLMusic.Lyric;

import Color_yr.ALLMusic.ALLMusic;
import com.google.gson.Gson;

import java.util.*;

public class LyricDo {

    private Map<Integer, ShowOBJ> Lyric = new HashMap<>();
    private LyricOBJ obj;

    private String GetString(String a, String b, String c) {
        int x = a.indexOf(b) + b.length();
        int y;
        if (c != null)
            y = a.indexOf(c);
        else y = a.length();
        return a.substring(x, y);
    }

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
            tlyric = Arrays.asList( obj.getTlyric().split("\n"));
            haveT = true;
        }
        String min;
        String sec;
        String Tlyric = null;
        String temp;
        int time;
        for (int now = 0; now < lyric.length; now++) {
            if (lyric[now].startsWith("[by"))
                continue;
            min = GetString(lyric[now], "[", ":");
            sec = GetString(lyric[now], ":", ".");
            time = Integer.parseInt(min) * 60 + Integer.parseInt(sec);
            if (time > 0)
                time -= ALLMusic.Config.getDelay();
            if (haveT) {
                temp = GetString(lyric[now], "[", "]");
                for(String item : tlyric) {
                    if (temp.equalsIgnoreCase(GetString(item, "[", "]"))) {
                        Tlyric = GetString(item, "]", null);
                        break;
                    }
                    else
                        Tlyric = null;
                }
            }
            Lyric.put(time, new ShowOBJ(time, haveT,
                    GetString(lyric[now], "]", null), Tlyric));
        }
    }

    public ShowOBJ checkTime(int playNow) {
        if (Lyric.containsKey(playNow))
            return Lyric.get(playNow);
        return null;
    }
}
