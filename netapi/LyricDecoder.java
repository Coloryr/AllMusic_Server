package com.coloryr.allmusic.server.netapi;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.music.LyricItemObj;
import com.coloryr.allmusic.server.core.utils.Function;
import com.coloryr.allmusic.server.netapi.obj.music.lyric.WLyricObj;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LyricDecoder {
    private static final Pattern p = Pattern.compile("\\(([0-9]+),[0-9]+.[0-9]\\)");
    private final Map<Long, LyricItemObj> temp = new LinkedHashMap<>();
    public boolean isHave = false;
    public boolean isHaveK = false;
    public Map<Long, String> kly = new LinkedHashMap<>();

    public Map<Long, LyricItemObj> getTemp() {
        return temp;
    }

    public Map<Long, String> getKLyric() {
        return kly;
    }

    /**
     * 检查歌词
     *
     * @param obj 歌词
     * @return 结果
     */
    public boolean check(WLyricObj obj) {
        String[] lyric;

        boolean haveT = false;
        if (!obj.isOk())
            return true;
        else if (obj.isNone())
            return false;
        lyric = obj.getLyric().split("\n");

        Map<Long, String> temp = getTime(Arrays.asList(lyric));
        Map<Long, String> temp1 = new HashMap<>();

        if (obj.getTlyric() != null) {
            List<String> tlyric = Arrays.asList(obj.getTlyric().split("\n"));
            haveT = true;
            temp1 = getTime(tlyric);
        }

        String temp2 = obj.getYrc() == null ? obj.getKlyric() : obj.getYrc();

        if (temp2 != null && !temp2.isEmpty()) {
            String[] klyric = temp2.split("\n");
            for (String item : klyric) {
                Map<Long, String> temp4 = getKTime(item, obj.getVersion());
                if (temp4 != null) {
                    kly.putAll(temp4);
                }
            }
            isHaveK = true;
        }

        for (Map.Entry<Long, String> item : temp.entrySet()) {
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
    private Map<Long, String> getTime(List<String> lyric) {
        Map<Long, String> res = new LinkedHashMap<>();
        String min;
        String sec;
        String mil;
        long time;
        long milt;
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
            milt = Long.parseLong(mil);
            if (mil.length() == 3) {
                milt /= 10;
            }
            time = Long.parseLong(min) * 60 * 1000 + Long.parseLong(sec) * 1000 + milt * 10;
            if (time > 0 && time + AllMusic.getConfig().lyricDelay > 0)
                time += AllMusic.getConfig().lyricDelay / 10 * 10;
            res.put(time, Function.getString(s, "]", null));
        }
        return res;
    }

    private Map<Long, String> getKTime(String lyric, boolean yrc) {
        Map<Long, String> res = new LinkedHashMap<>();
        if (!lyric.startsWith("[") || !lyric.contains("]("))
            return null;

        String[] datas = lyric.split("\\(([0-9]+),[0-9]+.[0-9]\\)");
        Matcher m = p.matcher(lyric);
        List<String> temp1111 = new ArrayList<>();
        while (m.find()) {
            temp1111.add(m.group()
                    .replace("(", "")
                    .replace(")", ""));
        }
        if (datas.length == 1)
            return null;

        String temp = Function.getString(lyric, "[", "]");
        String[] temp11 = temp.split(",");
        long now = Integer.parseInt(temp11[0]) / 10 * 10;
        for (int a = 1; a < datas.length; a++) {
            String data = datas[a];
            String temp3 = temp1111.get(a - 1);
            String[] temp8 = temp3.split(",");
            long temp5;
            if (yrc) {
                temp5 = (Integer.parseInt(temp8[0]) / 10 * 10);
                if (temp5 > 0 && temp5 + AllMusic.getConfig().ktvLyricDelay > 0)
                    temp5 += (AllMusic.getConfig().ktvLyricDelay / 10 * 10);
                res.put(temp5, data);
            } else {
                try {
                    temp5 = (Integer.parseInt(temp8[1]) / 10 * 10);
                } catch (Exception e) {
                    AllMusic.log.warning("不支持的Ktv歌词");
                    AllMusic.log.warning("请带上音乐ID联系开发者");
                    return null;
                }

                if (temp5 > 0 && temp5 + AllMusic.getConfig().ktvLyricDelay > 0)
                    temp5 += (AllMusic.getConfig().ktvLyricDelay / 10 * 10);
                res.put(now, data);
                now += temp5;
            }
        }

        return res;
    }
}
