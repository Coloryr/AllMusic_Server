package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.meta;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

public class ITunesMetadataMeanBox extends FullBox {

    private String domain;

    public ITunesMetadataMeanBox() {
        super("iTunes Metadata Mean Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        domain = in.readString((int) getLeft(in));
    }

    public String getDomain() {
        return domain;
    }
}
