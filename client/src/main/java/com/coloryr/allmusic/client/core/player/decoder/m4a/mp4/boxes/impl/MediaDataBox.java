package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxImpl;

import java.io.IOException;

/**
 * The Media Data Box contains the media data. In video tracks, this box would
 * contain video frames. A presentation may contain zero or more Media Data
 * Boxes. The actual media data follows the type field; its structure is
 * described by the metadata in the movie box.
 * There may be any number of these boxes in the file (including zero, if all
 * the media data is in other files). The metadata refers to media data by its
 * absolute offset within the file.
 *
 * @author in-somnia
 */
public class MediaDataBox extends BoxImpl {

    public MediaDataBox() {
        super("Media Data Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        //if random access: skip, else: do nothing
    }
}
