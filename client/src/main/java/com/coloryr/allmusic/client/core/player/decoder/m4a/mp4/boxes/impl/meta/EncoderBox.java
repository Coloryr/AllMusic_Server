package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.meta;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxTypes;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

public class EncoderBox extends FullBox {

    private String data;

    public EncoderBox() {
        super("Encoder Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        if (parent.getType() == BoxTypes.ITUNES_META_LIST_BOX) readChildren(in);
        else {
            super.decode(in);
            data = in.readString((int) getLeft(in));
        }
    }

    public String getData() {
        return data;
    }
}
