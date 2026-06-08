package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * This box provides a compact marking of the random access points within the
 * stream. The table is arranged in strictly increasing order of sample number.
 * <p>
 * If the sync sample box is not present, every sample is a random access point.
 *
 * @author in-somnia
 */
public class SyncSampleBox extends FullBox {

    private long[] sampleNumbers;

    public SyncSampleBox() {
        super("Sync Sample Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        final int entryCount = (int) in.readBytes(4);
        sampleNumbers = new long[entryCount];
        for (int i = 0; i < entryCount; i++) {
            sampleNumbers[i] = in.readBytes(4);
        }
    }

    /**
     * Gives the numbers of the samples for each entry that are random access
     * points in the stream.
     *
     * @return a list of sample numbers
     */
    public long[] getSampleNumbers() {
        return sampleNumbers;
    }
}
