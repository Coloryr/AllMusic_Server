package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.oma;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * The Discrete Media headers box includes fields specific to the DCF format and
 * the Common Headers box, followed by an optional user-data box. There must be
 * exactly one OMADiscreteHeaders box in a single OMA DRM Container box, as the
 * first box in the container.
 *
 * @author in-somnia
 */
public class OMADiscreteMediaHeadersBox extends FullBox {

    private String contentType;

    public OMADiscreteMediaHeadersBox() {
        super("OMA DRM Discrete Media Headers Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        final int len = in.read();
        contentType = in.readString(len);

        readChildren(in);
    }

    /**
     * The content type indicates the original MIME media type of the Content
     * Object i.e. what content type the result of a successful extraction of
     * the OMAContentBox represents.
     *
     * @return the content type
     */
    public String getContentType() {
        return contentType;
    }
}
