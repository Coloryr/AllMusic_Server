package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;

import java.io.IOException;

/**
 * Box implementation that is used for unknown types.
 *
 * @author in-somnia
 */
class UnknownBox extends BoxImpl {

    UnknownBox() {
        super("unknown");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        //no need to read, box will be skipped
    }
}
