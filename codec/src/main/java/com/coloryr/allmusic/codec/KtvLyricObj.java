package com.coloryr.allmusic.codec;

import java.util.ArrayList;
import java.util.List;

public class KtvLyricObj {
    public List<KtvItem> items = new ArrayList<>();
    public long start;
    public long time;

    public int charCount;

    public String build() {
        StringBuilder builder = new StringBuilder();
        for (KtvItem item : items) {
            builder.append("(").append(item.start).append(",").append(item.time).append(")")
                    .append(item.key);
        }

        return builder.toString();
    }

    public static class KtvItem {
        public long time;
        public long start;
        public String key;
    }
}
