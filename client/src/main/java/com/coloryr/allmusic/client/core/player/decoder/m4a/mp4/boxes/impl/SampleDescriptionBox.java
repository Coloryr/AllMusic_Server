package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * The sample description table gives detailed information about the coding type
 * used, and any initialization information needed for that coding.
 *
 * @author in-somnia
 */
public class SampleDescriptionBox extends FullBox {

    public SampleDescriptionBox() {
        super("Sample Description Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        final int entryCount = (int) in.readBytes(4);

        readChildren(in, entryCount);
    }
}
