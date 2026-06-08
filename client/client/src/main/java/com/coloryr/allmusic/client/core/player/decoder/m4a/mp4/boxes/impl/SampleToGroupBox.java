package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * This table can be used to find the group that a sample belongs to and the
 * associated description of that sample group. The table is compactly coded
 * with each entry giving the index of the first sample of a run of samples with
 * the same sample group descriptor. The sample group description ID is an index
 * that refers to a SampleGroupDescription box, which contains entries
 * describing the characteristics of each sample group.
 * <p>
 * There may be multiple instances of this box if there is more than one sample
 * grouping for the samples in a track. Each instance of the SampleToGroup box
 * has a type code that distinguishes different sample groupings. Within a
 * track, there shall be at most one instance of this box with a particular
 * grouping type. The associated SampleGroupDescription shall indicate the same
 * value for the grouping type.
 */
public class SampleToGroupBox extends FullBox {

    private long groupingType;
    private long[] sampleCount, groupDescriptionIndex;

    public SampleToGroupBox() {
        super("Sample To Group Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        groupingType = in.readBytes(4);
        final int entryCount = (int) in.readBytes(4);
        sampleCount = new long[entryCount];
        groupDescriptionIndex = new long[entryCount];

        for (int i = 0; i < entryCount; i++) {
            sampleCount[i] = in.readBytes(4);
            groupDescriptionIndex[i] = in.readBytes(4);
        }
    }

    /**
     * The grouping type is an integer that identifies the type (i.e. criterion
     * used to form the sample groups) of the sample grouping and links it to
     * its sample group description table with the same value for grouping type.
     * At most one occurrence of this box with the same value for 'grouping
     * type' shall exist for a track.
     */
    public long getGroupingType() {
        return groupingType;
    }

    /**
     * The sample count is an integer that gives the number of consecutive
     * samples with the same sample group descriptor for a specific entry. If
     * the sum of the sample count in this box is less than the total sample
     * count, then the reader should effectively extend it with an entry that
     * associates the remaining samples with no group.
     * It is an error for the total in this box to be greater than the sample
     * count documented elsewhere, and the reader behaviour would then be
     * undefined.
     */
    public long[] getSampleCount() {
        return sampleCount;
    }

    /**
     * The group description index is an integer that gives the index of the
     * sample group entry which describes the samples in this group for a
     * specific entry. The index ranges from 1 to the number of sample group
     * entries in the SampleGroupDescriptionBox, or takes the value 0 to
     * indicate that this sample is a member of no group of this type.
     */
    public long[] getGroupDescriptionIndex() {
        return groupDescriptionIndex;
    }
}
