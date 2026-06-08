package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;

import java.io.IOException;

public class FullBox extends BoxImpl {

    protected int version, flags;

    public FullBox(String name) {
        super(name);
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        version = in.read();
        flags = (int) in.readBytes(3);
    }
}
