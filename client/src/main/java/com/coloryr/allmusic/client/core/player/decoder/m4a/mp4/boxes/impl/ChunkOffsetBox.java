package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxTypes;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

public class ChunkOffsetBox extends FullBox {

    private long[] chunks;

    public ChunkOffsetBox() {
        super("Chunk Offset Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        final int len = (type == BoxTypes.CHUNK_LARGE_OFFSET_BOX) ? 8 : 4;
        final int entryCount = (int) in.readBytes(4);
        chunks = new long[entryCount];

        for (int i = 0; i < entryCount; i++) {
            chunks[i] = in.readBytes(len);
        }
    }

    public long[] getChunks() {
        return chunks;
    }
}
