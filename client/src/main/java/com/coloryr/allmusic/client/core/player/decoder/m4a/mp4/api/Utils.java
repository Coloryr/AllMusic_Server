package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api;

import java.util.Date;

class Utils {

    private static final long DATE_OFFSET = 2082850791998l;

    static Date getDate(long time) {
        return new Date(time * 1000 - DATE_OFFSET);
    }
}
