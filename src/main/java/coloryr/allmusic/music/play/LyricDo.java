package coloryr.allmusic.music.play;

import coloryr.allmusic.AllMusic;
import coloryr.allmusic.objs.api.music.lyric.WLyricOBJ;
import coloryr.allmusic.objs.music.LyricItemObj;
import coloryr.allmusic.utils.Function;

import java.util.*;

public class LyricDo {
    private final Map<Integer, LyricItemObj> temp = new LinkedHashMap<>();
    public boolean isHave = false;
    public boolean isHaveK = false;

    public Map<Integer, LyricItemObj> getTemp() {
        return temp;
    }

    public Map<Integer, String> kly = new LinkedHashMap<>();

    public Map<Integer, String> getKLyric() {
        return kly;
    }

    /**
     * 检查歌词
     *
     * @param obj 歌词
     * @return 结果
     */
    public boolean check(WLyricOBJ obj) {
        String[] lyric;

        boolean haveT = false;
        if (!obj.isOk())
            return true;
        else if (obj.isNone())
            return false;
        lyric = obj.getLyric().split("\n");

        Map<Integer, String> temp = getTime(Arrays.asList(lyric));
        Map<Integer, String> temp1 = new HashMap<>();

        if (obj.getTlyric() != null) {
            List<String> tlyric = Arrays.asList(obj.getTlyric().split("\n"));
            haveT = true;
            temp1 = getTime(tlyric);
        }

        String temp2 = obj.getYrc() == null ? obj.getLyric() : obj.getYrc();

        if (temp2 != null && !temp2.isEmpty()) {
            String[] klyric = temp2.split("\n");
            for (String item : klyric) {
                Map<Integer, String> temp4 = getKTime(item);
                if (temp4 != null) {
                    kly.putAll(temp4);
                }
            }
            isHaveK = true;
        }

        for (Map.Entry<Integer, String> item : temp.entrySet()) {
            this.temp.put(item.getKey(), new LyricItemObj(item.getValue(),
                    haveT ? temp1.get(item.getKey()) : null));
        }


        isHave = true;
        return false;
    }

    /**
     * 将歌词转换成时间 字
     *
     * @param lyric 歌词
     * @return 结果
     */
    private Map<Integer, String> getTime(List<String> lyric) {
        Map<Integer, String> res = new LinkedHashMap<>();
        String min;
        String sec;
        String mil;
        int time;
        int milt;
        for (String s : lyric) {
            if (!s.startsWith("["))
                continue;
            String temp = Function.getString(s, "[", "]");
            if (!temp.contains(".") || !temp.contains(":"))
                continue;
            if (Function.countChar(temp, ':') > 1) {
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
            if (time > 0 && time + AllMusic.getConfig().Delay > 0)
                time += AllMusic.getConfig().Delay / 10 * 10;
            res.put(time, Function.getString(s, "]", null));
        }
        return res;
    }

    private Map<Integer, String> getKTime(String lyric) {
        Map<Integer, String> res = new LinkedHashMap<>();
        if (!lyric.startsWith("[") || !lyric.contains("]("))
            return null;

        String[] temp1 = lyric.split("\\(");
        if (temp1.length == 1)
            return null;

        String temp = Function.getString(lyric, "[", "]");
        String[] temp11 = temp.split(",");
        int now = Integer.parseInt(temp11[0]) / 10 * 10;
        for (int a = 1; a < temp1.length; a++) {
            String time = temp1[a];
            int temp2 = time.indexOf(')');
            String temp3 = time.substring(2, temp2);
            int temp7 = Function.countChar(temp3, ',');
            int temp5;
            if (temp7 == 2) {
                String[] temp8 = temp3.split(",");
                temp5 = (Integer.parseInt(temp8[1]) / 10 * 10);
            } else {
                temp5 = (Integer.parseInt(temp3) / 10 * 10);
            }
            if (temp5 > 0 && temp5 + AllMusic.getConfig().KDelay > 0)
                temp5 += (AllMusic.getConfig().KDelay / 10 * 10);
            res.put(now, time.substring(temp2 + 1));
            now += temp5;
        }

        return res;
    }
}
