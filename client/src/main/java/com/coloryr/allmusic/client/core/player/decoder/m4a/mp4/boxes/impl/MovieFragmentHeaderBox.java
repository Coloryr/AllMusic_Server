package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * The movie fragment header contains a sequence number, as a safety check. The
 * sequence number usually starts at 1 and must increase for each movie fragment
 * in the file, in the order in which they occur. This allows readers to verify
 * integrity of the sequence; it is an error to construct a file where the
 * fragments are out of sequence.
 */
public class MovieFragmentHeaderBox extends FullBox {

    private long sequenceNumber;

    public MovieFragmentHeaderBox() {
        super("Movie Fragment Header Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        sequenceNumber = in.readBytes(4);
    }

    /**
     * The ordinal number of this fragment, in increasing order.
     */
    public long getSequenceNumber() {
        return sequenceNumber;
    }
}
