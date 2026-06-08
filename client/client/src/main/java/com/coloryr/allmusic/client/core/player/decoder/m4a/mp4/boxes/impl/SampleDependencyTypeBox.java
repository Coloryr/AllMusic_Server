package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxTypes;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * This optional table answers three questions about sample dependency:
 * <ul>
 * <li>does this sample depend on others (is it an I-picture)?</li>
 * <li>do no other samples depend on this one?</li>
 * <li>does this sample contain multiple (redundant) encodings of the data at
 * this time-instant (possibly with different dependencies)?</li>
 * </ul>
 * <p>
 * In the absence of this table:
 * <ul>
 * <li>the sync sample table answers the first question; in most video codecs,
 * I-pictures are also sync points</li>
 * <li>the dependency of other samples on this one is unknown</li>
 * <li>the existence of redundant coding is unknown</li>
 * </ul>
 * <p>
 * When performing 'trick' modes, such as fast-forward, it is possible to use
 * the first piece of information to locate independently decodable samples.
 * Similarly, when performing random access, it may be necessary to locate the
 * previous sync point or random access recovery point, and roll-forward from
 * the sync point or the pre-roll starting point of the random access recovery
 * point to the desired point. While rolling forward, samples on which no others
 * depend need not be retrieved or decoded.
 * The value of 'sample is depended on' is independent of the existence of
 * redundant codings. However, a redundant coding may have different
 * dependencies from the primary coding; if redundant codings are available, the
 * value of 'sample depends on' documents only the primary coding.
 * <p>
 * A Sample Dependency Box may also occur in the Track Fragment Box.
 *
 * @author in-somnia
 */
public class SampleDependencyTypeBox extends FullBox {

    private int[] sampleDependsOn, sampleIsDependedOn, sampleHasRedundancy;

    public SampleDependencyTypeBox() {
        super("Sample Dependency Type Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        //get number of samples from SampleSizeBox
        long sampleCount = -1;
        if (parent.hasChild(BoxTypes.SAMPLE_SIZE_BOX))
            sampleCount = ((SampleSizeBox) parent.getChild(BoxTypes.SAMPLE_SIZE_BOX)).getSampleCount();
        //TODO: uncomment when CompactSampleSizeBox is implemented
        //else if(parent.containsChild(BoxTypes.COMPACT_SAMPLE_SIZE_BOX)) sampleCount = ((CompactSampleSizeBox)parent.getChild(BoxTypes.SAMPLE_SIZE_BOX)).getSampleSize();
        sampleHasRedundancy = new int[(int) sampleCount];
        sampleIsDependedOn = new int[(int) sampleCount];
        sampleDependsOn = new int[(int) sampleCount];

        byte b;
        for (int i = 0; i < sampleCount; i++) {
            b = (byte) in.read();
            /* 2 bits reserved
             * 2 bits sampleDependsOn
             * 2 bits sampleIsDependedOn
             * 2 bits sampleHasRedundancy
             */
            sampleHasRedundancy[i] = b & 3;
            sampleIsDependedOn[i] = (b >> 2) & 3;
            sampleDependsOn[i] = (b >> 4) & 3;
        }
    }

    /**
     * The 'sample depends on' field takes one of the following four values:
     * 0: the dependency of this sample is unknown
     * 1: this sample does depend on others (not an I picture)
     * 2: this sample does not depend on others (I picture)
     * 3: reserved
     *
     * @return a list of 'sample depends on' values for all samples
     */
    public int[] getSampleDependsOn() {
        return sampleDependsOn;
    }

    /**
     * The 'sample is depended on' field takes one of the following four values:
     * 0: the dependency of other samples on this sample is unknown
     * 1: other samples may depend on this one (not disposable)
     * 2: no other sample depends on this one (disposable)
     * 3: reserved
     *
     * @return a list of 'sample is depended on' values for all samples
     */
    public int[] getSampleIsDependedOn() {
        return sampleIsDependedOn;
    }

    /**
     * The 'sample has redundancy' field takes one of the following four values:
     * 0: it is unknown whether there is redundant coding in this sample
     * 1: there is redundant coding in this sample
     * 2: there is no redundant coding in this sample
     * 3: reserved
     *
     * @return a list of 'sample has redundancy' values for all samples
     */
    public int[] getSampleHasRedundancy() {
        return sampleHasRedundancy;
    }
}
