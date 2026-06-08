package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.od;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;

import java.io.IOException;

//ISO 14496-1 - 10.2.3
//TODO: not working: reads too much! did the specification change?
public class SLConfigDescriptor extends Descriptor {

    private boolean useAccessUnitStart, useAccessUnitEnd, useRandomAccessPoint,
            usePadding, useTimeStamp, useWallClockTimeStamp, useIdle, duration;
    private long timeStampResolution, ocrResolution;
    private int timeStampLength, ocrLength, instantBitrateLength,
            degradationPriorityLength, seqNumberLength;
    private long timeScale;
    private int accessUnitDuration, compositionUnitDuration;
    private long wallClockTimeStamp, startDecodingTimeStamp, startCompositionTimeStamp;
    private boolean ocrStream;
    private int ocrES_ID;

    @Override
    void decode(MP4InputStream in) throws IOException {
        int tmp;

        final boolean predefined = in.read() == 1;
        if (!predefined) {
            //flags
            tmp = in.read();
            useAccessUnitStart = ((tmp >> 7) & 1) == 1;
            useAccessUnitEnd = ((tmp >> 6) & 1) == 1;
            useRandomAccessPoint = ((tmp >> 5) & 1) == 1;
            usePadding = ((tmp >> 4) & 1) == 1;
            useTimeStamp = ((tmp >> 3) & 1) == 1;
            useWallClockTimeStamp = ((tmp >> 2) & 1) == 1;
            useIdle = ((tmp >> 1) & 1) == 1;
            duration = (tmp & 1) == 1;

            timeStampResolution = in.readBytes(4);
            ocrResolution = in.readBytes(4);
            timeStampLength = in.read();
            ocrLength = in.read();
            instantBitrateLength = in.read();
            tmp = in.read();
            degradationPriorityLength = (tmp >> 4) & 15;
            seqNumberLength = tmp & 15;

            if (duration) {
                timeScale = in.readBytes(4);
                accessUnitDuration = (int) in.readBytes(2);
                compositionUnitDuration = (int) in.readBytes(2);
            }

            if (!useTimeStamp) {
                if (useWallClockTimeStamp) wallClockTimeStamp = in.readBytes(4);
                tmp = (int) Math.ceil((double) (2 * timeStampLength) / 8);
                final long tmp2 = in.readBytes(tmp);
                final long mask = ((1 << timeStampLength) - 1);
                startDecodingTimeStamp = (tmp2 >> timeStampLength) & mask;
                startCompositionTimeStamp = tmp2 & mask;
            }
        }

        tmp = in.read();
        ocrStream = ((tmp >> 7) & 1) == 1;
        if (ocrStream) ocrES_ID = (int) in.readBytes(2);
    }
}
