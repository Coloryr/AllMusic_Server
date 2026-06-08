package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.drm;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxImpl;

import java.io.IOException;

public class FairPlayDataBox extends BoxImpl {

    private byte[] data;

    public FairPlayDataBox() {
        super("iTunes FairPlay Data Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        data = new byte[(int) getLeft(in)];
        in.readBytes(data);
    }

    public byte[] getData() {
        return data;
    }
}
