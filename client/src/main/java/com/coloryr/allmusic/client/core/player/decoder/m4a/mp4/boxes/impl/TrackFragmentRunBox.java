package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * Within the Track Fragment Box, there are zero or more Track Run Boxes. If the
 * duration-is-empty flag is set in the track fragment box, there are no track
 * runs. A track run documents a contiguous set of samples for a track.
 * <p>
 * If the data-offset is not present, then the data for this run starts
 * immediately after the data of the previous run, or at the base-data-offset
 * defined by the track fragment header if this is the first run in a track
 * fragment.
 * If the data-offset is present, it is relative to the base-data-offset
 * established in the track fragment header.
 *
 * @author in-somnia
 */
public class TrackFragmentRunBox extends FullBox {

    private int sampleCount;
    private boolean dataOffsetPresent, firstSampleFlagsPresent;
    private long dataOffset, firstSampleFlags;
    private boolean sampleDurationPresent, sampleSizePresent, sampleFlagsPresent,
            sampleCompositionTimeOffsetPresent;
    private long[] sampleDuration, sampleSize, sampleFlags, sampleCompositionTimeOffset;

    public TrackFragmentRunBox() {
        super("Track Fragment Run Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        sampleCount = (int) in.readBytes(4);

        //optional fields
        dataOffsetPresent = ((flags & 1) == 1);
        if (dataOffsetPresent) dataOffset = in.readBytes(4);

        firstSampleFlagsPresent = ((flags & 4) == 4);
        if (firstSampleFlagsPresent) firstSampleFlags = in.readBytes(4);

        //all fields are optional
        sampleDurationPresent = ((flags & 0x100) == 0x100);
        if (sampleDurationPresent) sampleDuration = new long[sampleCount];
        sampleSizePresent = ((flags & 0x200) == 0x200);
        if (sampleSizePresent) sampleSize = new long[sampleCount];
        sampleFlagsPresent = ((flags & 0x400) == 0x400);
        if (sampleFlagsPresent) sampleFlags = new long[sampleCount];
        sampleCompositionTimeOffsetPresent = ((flags & 0x800) == 0x800);
        if (sampleCompositionTimeOffsetPresent) sampleCompositionTimeOffset = new long[sampleCount];

        for (int i = 0; i < sampleCount && getLeft(in) > 0; i++) {
            if (sampleDurationPresent) sampleDuration[i] = in.readBytes(4);
            if (sampleSizePresent) sampleSize[i] = in.readBytes(4);
            if (sampleFlagsPresent) sampleFlags[i] = in.readBytes(4);
            if (sampleCompositionTimeOffsetPresent) sampleCompositionTimeOffset[i] = in.readBytes(4);
        }
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public boolean isDataOffsetPresent() {
        return dataOffsetPresent;
    }

    public long getDataOffset() {
        return dataOffset;
    }

    public boolean isFirstSampleFlagsPresent() {
        return firstSampleFlagsPresent;
    }

    public long getFirstSampleFlags() {
        return firstSampleFlags;
    }

    public boolean isSampleDurationPresent() {
        return sampleDurationPresent;
    }

    public long[] getSampleDuration() {
        return sampleDuration;
    }

    public boolean isSampleSizePresent() {
        return sampleSizePresent;
    }

    public long[] getSampleSize() {
        return sampleSize;
    }

    public boolean isSampleFlagsPresent() {
        return sampleFlagsPresent;
    }

    public long[] getSampleFlags() {
        return sampleFlags;
    }

    public boolean isSampleCompositionTimeOffsetPresent() {
        return sampleCompositionTimeOffsetPresent;
    }

    public long[] getSampleCompositionTimeOffset() {
        return sampleCompositionTimeOffset;
    }
}
