package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.sampleentries.codec;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;

import java.io.IOException;

public class AMRSpecificBox extends CodecSpecificBox {

    private int modeSet, modeChangePeriod, framesPerSample;

    public AMRSpecificBox() {
        super("AMR Specific Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        decodeCommon(in);

        modeSet = (int) in.readBytes(2);
        modeChangePeriod = in.read();
        framesPerSample = in.read();
    }

    public int getModeSet() {
        return modeSet;
    }

    public int getModeChangePeriod() {
        return modeChangePeriod;
    }

    public int getFramesPerSample() {
        return framesPerSample;
    }
}
