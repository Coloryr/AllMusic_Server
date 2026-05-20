package com.coloryr.allmusic.server.core.music;

import java.util.ArrayList;
import java.util.List;

public class KtvLyric {
    public List<KtvItem> items = new ArrayList<>();
    public long start;
    public long time;

    public static class KtvItem {
        public long time;
        public long start;
        public String key;
    }
}
