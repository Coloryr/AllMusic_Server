package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The chapter box allows to specify individual chapters along the main timeline
 * of a movie. The chapter box occurs within a movie box.
 * Defined in "Adobe Video File Format Specification v10".
 *
 * @author in-somnia
 */
public class ChapterBox extends FullBox {

    private final Map<Long, String> chapters;

    public ChapterBox() {
        super("Chapter Box");
        chapters = new HashMap<Long, String>();
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        in.skipBytes(4); //??

        final int count = in.read();

        long timestamp;
        int len;
        String name;
        for (int i = 0; i < count; i++) {
            timestamp = in.readBytes(8);
            len = in.read();
            name = in.readString(len);
            chapters.put(timestamp, name);
        }
    }

    /**
     * Returns a map that maps the timestamp of each chapter to its name.
     *
     * @return the chapters
     */
    public Map<Long, String> getChapters() {
        return chapters;
    }
}
