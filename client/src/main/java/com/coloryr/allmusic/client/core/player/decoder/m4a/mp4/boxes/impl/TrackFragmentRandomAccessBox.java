package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

public class TrackFragmentRandomAccessBox extends FullBox {

    private long trackID;
    private int entryCount;
    private long[] times, moofOffsets, trafNumbers, trunNumbers, sampleNumbers;

    public TrackFragmentRandomAccessBox() {
        super("Track Fragment Random Access Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        trackID = in.readBytes(4);
        //26 bits reserved, 2 bits trafSizeLen, 2 bits trunSizeLen, 2 bits sampleSizeLen
        final long l = in.readBytes(4);
        final int trafNumberLen = (int) ((l >> 4) & 0x3) + 1;
        final int trunNumberLen = (int) ((l >> 2) & 0x3) + 1;
        final int sampleNumberLen = (int) (l & 0x3) + 1;
        entryCount = (int) in.readBytes(4);

        final int len = (version == 1) ? 8 : 4;

        for (int i = 0; i < entryCount; i++) {
            times[i] = in.readBytes(len);
            moofOffsets[i] = in.readBytes(len);
            trafNumbers[i] = in.readBytes(trafNumberLen);
            trunNumbers[i] = in.readBytes(trunNumberLen);
            sampleNumbers[i] = in.readBytes(sampleNumberLen);
        }
    }

    /**
     * The track ID is an integer identifying the associated track.
     *
     * @return the track ID
     */
    public long getTrackID() {
        return trackID;
    }

    public int getEntryCount() {
        return entryCount;
    }

    /**
     * The time is an integer that indicates the presentation time of the random
     * access sample in units defined in the 'mdhd' of the associated track.
     *
     * @return the times of all entries
     */
    public long[] getTimes() {
        return times;
    }

    /**
     * The moof-Offset is an integer that gives the offset of the 'moof' used in
     * the an entry. Offset is the byte-offset between the beginning of the file
     * and the beginning of the 'moof'.
     *
     * @return the offsets for all entries
     */
    public long[] getMoofOffsets() {
        return moofOffsets;
    }

    /**
     * The 'traf' number that contains the random accessible sample. The number
     * ranges from 1 (the first 'traf' is numbered 1) in each 'moof'.
     *
     * @return the 'traf' numbers for all entries
     */
    public long[] getTrafNumbers() {
        return trafNumbers;
    }

    /**
     * The 'trun' number that contains the random accessible sample. The number
     * ranges from 1 in each 'traf'.
     *
     * @return the 'trun' numbers for all entries
     */
    public long[] getTrunNumbers() {
        return trunNumbers;
    }

    /**
     * The sample number that contains the random accessible sample. The number
     * ranges from 1 in each 'trun'.
     *
     * @return the sample numbers for all entries
     */
    public long[] getSampleNumbers() {
        return sampleNumbers;
    }
}
