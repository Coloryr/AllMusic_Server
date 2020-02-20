package Color_yr.ALLMusic.Lyric;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Utils;
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
            tlyric = Arrays.asList( obj.getTlyric().split("\n"));
            haveT = true;
        }
        String min;
        String sec;
        String Tlyric = null;
        String temp;
        int time;
        for (int now = 0; now < lyric.length; now++) {
            min = Utils.getString(lyric[now], "[", ":");
            if(!Utils.isInteger(min))
                continue;
            sec = Utils.getString(lyric[now], ":", ".");
            time = Integer.parseInt(min) * 60 + Integer.parseInt(sec);
            if (time > 0)
                time -= ALLMusic.Config.getDelay();
            if (haveT) {
                temp = Utils.getString(lyric[now], "[", "]");
                for(String item : tlyric) {
                    if (temp.equalsIgnoreCase(Utils.getString(item, "[", "]"))) {
                        Tlyric = Utils.getString(item, "]", null);
                        break;
                    }
                    else
                        Tlyric = null;
                }
            }
            Lyric.put(time, new ShowOBJ(time, haveT,
                    Utils.getString(lyric[now], "]", null), Tlyric));
        }
    }

    public ShowOBJ checkTime(int playNow) {
        if (Lyric.containsKey(playNow))
            return Lyric.get(playNow);
        return null;
    }
}
