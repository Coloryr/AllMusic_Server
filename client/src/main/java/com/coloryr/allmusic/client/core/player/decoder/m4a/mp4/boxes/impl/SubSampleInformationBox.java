package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * The Sub-Sample Information box is designed to contain sub-sample information.
 * A sub-sample is a contiguous range of bytes of a sample. The specific
 * definition of a sub-sample shall be supplied for a given coding system (e.g.
 * for ISO/IEC 14496-10, Advanced Video Coding). In the absence of such a
 * specific definition, this box shall not be applied to samples using that
 * coding system.
 * The table is sparsely coded; the table identifies which samples have
 * sub-sample structure by recording the difference in sample-number between
 * each entry. The first entry in the table records the sample number of the
 * first sample having sub-sample information.
 *
 * @author in-somnia
 */
public class SubSampleInformationBox extends FullBox {

    private long[] sampleDelta;
    private long[][] subsampleSize;
    private int[][] subsamplePriority;
    private boolean[][] discardable;

    public SubSampleInformationBox() {
        super("Sub Sample Information Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        final int len = (version == 1) ? 4 : 2;
        final int entryCount = (int) in.readBytes(4);
        sampleDelta = new long[entryCount];
        subsampleSize = new long[entryCount][];
        subsamplePriority = new int[entryCount][];
        discardable = new boolean[entryCount][];

        int j, subsampleCount;
        for (int i = 0; i < entryCount; i++) {
            sampleDelta[i] = in.readBytes(4);
            subsampleCount = (int) in.readBytes(2);
            subsampleSize[i] = new long[subsampleCount];
            subsamplePriority[i] = new int[subsampleCount];
            discardable[i] = new boolean[subsampleCount];

            for (j = 0; j < subsampleCount; j++) {
                subsampleSize[i][j] = in.readBytes(len);
                subsamplePriority[i][j] = in.read();
                discardable[i][j] = (in.read() & 1) == 1;
                in.skipBytes(4); //reserved
            }
        }
    }

    /**
     * The sample delta for each entry is an integer that specifies the sample
     * number of the sample having sub-sample structure. It is coded as the
     * difference between the desired sample number, and the sample number
     * indicated in the previous entry. If the current entry is the first entry,
     * the value indicates the sample number of the first sample having
     * sub-sample information, that is, the value is the difference between the
     * sample number and zero.
     *
     * @return the sample deltas for all entries
     */
    public long[] getSampleDelta() {
        return sampleDelta;
    }

    /**
     * The subsample size is an integer that specifies the size, in bytes, of a
     * specific sub-sample in a specific entry.
     *
     * @return the sizes of all subsamples
     */
    public long[][] getSubsampleSize() {
        return subsampleSize;
    }

    /**
     * The subsample priority is an integer specifying the degradation priority
     * for a specific sub-sample in a specific entry. Higher values indicate
     * sub-samples which are important to, and have a greater impact on, the
     * decoded quality.
     *
     * @return all subsample priorities
     */
    public int[][] getSubsamplePriority() {
        return subsamplePriority;
    }

    /**
     * If true, the sub-sample is required to decode the current sample, while
     * false means the sub-sample is not required to decode the current sample
     * but may be used for enhancements, e.g., the sub-sample consists of
     * supplemental enhancement information (SEI) messages.
     *
     * @return a list of flags indicating if a specific subsample is discardable
     */
    public boolean[][] getDiscardable() {
        return discardable;
    }
}
