package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.sampleentries.codec;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;

import java.io.IOException;

public class QCELPSpecificBox extends CodecSpecificBox {

    private int framesPerSample;

    public QCELPSpecificBox() {
        super("QCELP Specific Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        decodeCommon(in);

        framesPerSample = in.read();
    }

    public int getFramesPerSample() {
        return framesPerSample;
    }
}
