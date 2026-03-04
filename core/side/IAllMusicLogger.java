package com.coloryr.allmusic.server.core.side;

import com.coloryr.allmusic.server.core.AllMusic;
import net.kyori.adventure.text.Component;

public interface IAllMusicLogger {
    default void data(String data) {
        data(AllMusic.miniMessage(data));
    }

    void data(Component data);
}
