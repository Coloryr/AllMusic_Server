package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

public class SampleToChunkBox extends FullBox {

    private long[] firstChunks, samplesPerChunk, sampleDescriptionIndex;

    public SampleToChunkBox() {
        super("Sample To Chunk Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        final int entryCount = (int) in.readBytes(4);
        firstChunks = new long[entryCount];
        samplesPerChunk = new long[entryCount];
        sampleDescriptionIndex = new long[entryCount];

        for (int i = 0; i < entryCount; i++) {
            firstChunks[i] = in.readBytes(4);
            samplesPerChunk[i] = in.readBytes(4);
            sampleDescriptionIndex[i] = in.readBytes(4);
        }
    }

    public long[] getFirstChunks() {
        return firstChunks;
    }

    public long[] getSamplesPerChunk() {
        return samplesPerChunk;
    }

    public long[] getSampleDescriptionIndex() {
        return sampleDescriptionIndex;
    }
}
