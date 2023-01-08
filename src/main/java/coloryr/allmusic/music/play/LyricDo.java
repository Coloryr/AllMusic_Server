package coloryr.allmusic.music.play;

import coloryr.allmusic.AllMusic;
import coloryr.allmusic.objs.api.music.lyric.WLyricOBJ;
import coloryr.allmusic.objs.music.LyricItemObj;
import coloryr.allmusic.utils.Function;

import java.util.*;

public class LyricDo {
    private static class KL {
        public char k;
        public Map<Integer, Character> v;

        public KL(char k, Map<Integer, Character> v) {
            this.k = k;
            this.v = v;
        }

        @Override
        public String toString() {
            return String.valueOf(k);
        }
    }

    private final Map<Integer, LyricItemObj> temp = new HashMap<>();
    public boolean isHave = false;

    public Map<Integer, LyricItemObj> getTemp() {
        return temp;
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
        boolean haveK = false;
        if (!obj.isOk())
            return true;
        else if (obj.isNone())
            return false;
        lyric = obj.getLyric().split("\n");

        List<KL> temp2 = new ArrayList<>();
        Map<Integer, String> temp = getTime(Arrays.asList(lyric));
        Map<Integer, String> temp1 = new HashMap<>();

        if (obj.getTlyric() != null) {
            List<String> tlyric = Arrays.asList(obj.getTlyric().split("\n"));
            haveT = true;
            temp1 = getTime(tlyric);
        }

        if (obj.getKlyric() != null && !obj.getKlyric().isEmpty()) {
            String[] klyric = obj.getKlyric().split("\n");
            char[] temp3 = new char[1];
            for (String item : klyric) {
                Map<Integer, Character> temp4 = getKTime(item, temp3);
                if (temp4 != null) {
                    temp2.add(new KL(temp3[0], temp4));
                }
            }
            haveK = true;
        }

        int index = 0;
        for (Map.Entry<Integer, String> item : temp.entrySet()) {
            Map<Integer, Character> temp6 = null;
            if (haveK) {
                char temp7 = item.getValue().charAt(0);
                if (index == temp2.size()) {
                    index = 0;
                }
                while (index < temp2.size()) {
                    KL temp5 = temp2.get(index);
                    if (temp5.k == temp7) {
                        temp6 = temp5.v;
                        index++;
                        break;
                    }
                    index++;
                }
            }

            this.temp.put(item.getKey(), new LyricItemObj(item.getKey(), haveT,
                    item.getValue(), temp1.get(item.getKey()), temp6, temp6 != null));
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
                time += AllMusic.getConfig().Delay * 10;
            res.put(time, Function.getString(s, "]", null));
        }
        return res;
    }

    private Map<Integer, Character> getKTime(String lyric, char[] time1) {
        Map<Integer, Character> res = new HashMap<>();
        if (!lyric.startsWith("[") || !lyric.contains("]("))
            return null;

        String[] temp1 = lyric.split("\\(");
        if (temp1.length == 1)
            return null;

        int now = 0;
        for (int a = 1; a < temp1.length; a++) {
            String time = temp1[a];
            int temp2 = time.indexOf(')');
            String temp3 = time.substring(2, temp2);
            int temp5 = Integer.parseInt(temp3) / 10 * 10;
            char temp6 = time.charAt(temp2 + 1);
            if (a == 1) {
                time1[0] = temp6;
            }
            res.put(now, temp6);
            now += temp5;
        }

        return res;
    }
}
