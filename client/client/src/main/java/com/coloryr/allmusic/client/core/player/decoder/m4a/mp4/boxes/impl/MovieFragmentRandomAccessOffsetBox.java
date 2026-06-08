package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * The Movie Fragment Random Access Offset Box provides a copy of the length
 * field from the enclosing Movie Fragment Random Access Box. It is placed last
 * within that box, so that the size field is also last in the enclosing Movie
 * Fragment Random Access Box. When the Movie Fragment Random Access Box is also
 * last in the file this permits its easy location. The size field here must be
 * correct. However, neither the presence of the Movie Fragment Random Access
 * Box, nor its placement last in the file, are assured.
 *
 * @author in-somnia
 */
public class MovieFragmentRandomAccessOffsetBox extends FullBox {

    private long byteSize;

    public MovieFragmentRandomAccessOffsetBox() {
        super("Movie Fragment Random Access Offset Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        byteSize = in.readBytes(4);
    }

    public long getByteSize() {
        return byteSize;
    }
}
