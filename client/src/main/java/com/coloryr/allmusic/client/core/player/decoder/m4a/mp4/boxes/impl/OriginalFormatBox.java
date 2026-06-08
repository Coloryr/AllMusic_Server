package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxImpl;

import java.io.IOException;

/**
 * The Original Format Box contains the four-character-code of the original
 * un-transformed sample description.
 *
 * @author in-somnia
 */
public class OriginalFormatBox extends BoxImpl {

    private long originalFormat;

    public OriginalFormatBox() {
        super("Original Format Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        originalFormat = in.readBytes(4);
    }

    /**
     * The original format is the four-character-code of the original
     * un-transformed sample entry (e.g. 'mp4v' if the stream contains protected
     * MPEG-4 visual material).
     *
     * @return the stream's original format
     */
    public long getOriginalFormat() {
        return originalFormat;
    }
}
