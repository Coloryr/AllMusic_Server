package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

//needs to be defined, because readChildren() is not called by factory
/* TODO: this class shouldn't be needed. at least here, things become too
complicated. change this!!! */
public class MetaBox extends FullBox {

    public MetaBox() {
        super("Meta Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        readChildren(in);
    }
}
