package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.oma;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * The ContentID box contains the unique identifier for the Content Object the
 * metadata are associated with. The value of the content-ID must be the value
 * of the content-ID stored in the Common Headers for this Content Object. There
 * must be exactly one ContentID sub-box per User-Data box, as the first sub-box
 * in the container.
 *
 * @author in-somnia
 */
public class OMAContentIDBox extends FullBox {

    private String contentID;

    public OMAContentIDBox() {
        super("OMA DRM Content ID Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        final int len = (int) in.readBytes(2);
        contentID = in.readString(len);
    }

    /**
     * Returns the content-ID string.
     *
     * @return the content-ID
     */
    public String getContentID() {
        return contentID;
    }
}
