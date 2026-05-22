
package com.coloryr.allmusic.server.core.music;

import com.coloryr.allmusic.server.core.objs.music.LyricItemObj;

public class TopLyricSave extends LyricSave {

    public void setTlyric(String data) {
        if (now == null) {
            now = new LyricItemObj(data, "", 0);
        }
        now.lyric = data;
    }

    public void setLyric(String data) {
        if (now == null) {
            now = new LyricItemObj("", data, 0);
        }
        now.lyric = data;
    }

    public void setHaveT(boolean data) {
        if (!data) {
            now = null;
        }
    }
}
