package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * This box contains a compact version of a table that allows indexing from
 * decoding time to sample number. Other tables give sample sizes and pointers,
 * from the sample number. Each entry in the table gives the number of
 * consecutive samples with the same time delta, and the delta of those samples.
 * By adding the deltas a complete time-to-sample map may be built.
 * The Decoding Time to Sample Box contains decode time delta's:
 * DT(n+1) = DT(n) + STTS(n)
 * where STTS(n) is the (uncompressed) table entry for sample n.
 * The sample entries are ordered by decoding time stamps; therefore the deltas
 * are all non-negative.
 * The DT axis has a zero origin; DT(i) = SUM(for j=0 to i-1 of delta(j)), and
 * the sum of all deltas gives the length of the media in the track (not mapped
 * to the overall timescale, and not considering any edit list).
 * The Edit List Box provides the initial CT value if it is non-empty
 * (non-zero).
 *
 * @author in-somnia
 */
public class DecodingTimeToSampleBox extends FullBox {

    private long[] sampleCounts, sampleDeltas;

    public DecodingTimeToSampleBox() {
        super("Time To Sample Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        final int entryCount = (int) in.readBytes(4);
        sampleCounts = new long[entryCount];
        sampleDeltas = new long[entryCount];

        for (int i = 0; i < entryCount; i++) {
            sampleCounts[i] = in.readBytes(4);
            sampleDeltas[i] = in.readBytes(4);
        }
    }

    public long[] getSampleCounts() {
        return sampleCounts;
    }

    public long[] getSampleDeltas() {
        return sampleDeltas;
    }
}
