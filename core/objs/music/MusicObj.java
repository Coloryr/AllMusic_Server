package com.coloryr.allmusic.server.core.objs.music;

import org.jspecify.annotations.NonNull;

public class MusicObj implements Comparable {
    /**
     * 音乐编号
     */
    public String id;
    /**
     * 音乐API
     */
    public String api;

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof MusicObj) {
            MusicObj obj = (MusicObj) o;
            int result = this.id.compareTo(obj.id);
            if (result != 0) {
                return result;
            }
            return this.api.compareTo(obj.api);
        }
        return 0;
    }
}
