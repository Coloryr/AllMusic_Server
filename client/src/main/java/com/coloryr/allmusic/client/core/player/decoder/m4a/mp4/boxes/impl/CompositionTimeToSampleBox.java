package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * This box provides the offset between decoding time and composition time.
 * Since decoding time must be less than the composition time, the offsets are
 * expressed as unsigned numbers such that
 * CT(n) = DT(n) + CTTS(n)
 * where CTTS(n) is the (uncompressed) table entry for sample n.
 * <p>
 * The composition time to sample table is optional and must only be present if
 * DT and CT differ for any samples.
 * <p>
 * Hint tracks do not use this box.
 *
 * @author in-somnia
 */
public class CompositionTimeToSampleBox extends FullBox {

    private long[] sampleCounts, sampleOffsets;

    public CompositionTimeToSampleBox() {
        super("Time To Sample Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        final int entryCount = (int) in.readBytes(4);
        sampleCounts = new long[entryCount];
        sampleOffsets = new long[entryCount];

        for (int i = 0; i < entryCount; i++) {
            sampleCounts[i] = in.readBytes(4);
            sampleOffsets[i] = in.readBytes(4);
        }
    }

    public long[] getSampleCounts() {
        return sampleCounts;
    }

    public long[] getSampleOffsets() {
        return sampleOffsets;
    }
}
