package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxTypes;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * This box contains the sample dependencies for each switching sample. The
 * dependencies are stored in the table, one record for each sample. The size of
 * the table is taken from the the Sample Size Box ('stsz') or Compact Sample
 * Size Box ('stz2').
 *
 * @author in-somnia
 */
public class SampleDependencyBox extends FullBox {

    private int[] dependencyCount;
    private int[][] relativeSampleNumber;

    public SampleDependencyBox() {
        super("Sample Dependency Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        final int sampleCount = ((SampleSizeBox) parent.getChild(BoxTypes.SAMPLE_SIZE_BOX)).getSampleCount();

        int j;
        for (int i = 0; i < sampleCount; i++) {
            dependencyCount[i] = (int) in.readBytes(2);
            for (j = 0; j < dependencyCount[i]; j++) {
                relativeSampleNumber[i][j] = (int) in.readBytes(2);
            }
        }
    }

    /**
     * The dependency count is an integer that counts the number of samples
     * in the source track on which this switching sample directly depends.
     *
     * @return all dependency counts
     */
    public int[] getDependencyCount() {
        return dependencyCount;
    }

    /**
     * The relative sample number is an integer that identifies a sample in
     * the source track. The relative sample numbers are encoded as follows.
     * If there is a sample in the source track with the same decoding time,
     * it has a relative sample number of 0. Whether or not this sample
     * exists, the sample in the source track which immediately precedes the
     * decoding time of the switching sample has relative sample number –1,
     * the sample before that –2, and so on. Similarly, the sample in the
     * source track which immediately follows the decoding time of the
     * switching sample has relative sample number +1, the sample after that
     * +2, and so on.
     *
     * @return all relative sample numbers
     */
    public int[][] getRelativeSampleNumber() {
        return relativeSampleNumber;
    }
}
