package com.coloryr.allmusic.server.core;

import com.coloryr.allmusic.server.core.music.LyricSave;
import com.coloryr.allmusic.server.core.objs.music.SearchPageObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;

public interface IMusicApi {
    String getId();

    SongInfoObj getMusic(String id, String player, boolean isList);

    SearchPageObj search(String[] args, boolean isDefault);

    void setList(String id, Object sender);

    LyricSave getLyric(String id);

    String getPlayUrl(String id);

    boolean isBusy();

    String getMusicId(String arg);

    boolean checkId(String id);
}
