package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * The Scheme Type Box identifies the protection scheme.
 *
 * @author in-somnia
 */
public class SchemeTypeBox extends FullBox {

    public static final long ITUNES_SCHEME = 1769239918; //itun
    private long schemeType, schemeVersion;
    private String schemeURI;

    public SchemeTypeBox() {
        super("Scheme Type Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        schemeType = in.readBytes(4);
        schemeVersion = in.readBytes(4);
        schemeURI = ((flags & 1) == 1) ? in.readUTFString((int) getLeft(in), MP4InputStream.UTF8) : null;
    }

    /**
     * The scheme type is the code defining the protection scheme.
     *
     * @return the scheme type
     */
    public long getSchemeType() {
        return schemeType;
    }

    /**
     * The scheme version is the version of the scheme used to create the
     * content.
     *
     * @return the scheme version
     */
    public long getSchemeVersion() {
        return schemeVersion;
    }

    /**
     * The optional scheme URI allows for the option of directing the user to a
     * web-page if they do not have the scheme installed on their system. It is
     * an absolute URI.
     * If the scheme URI is not present, this method returns null.
     *
     * @return the scheme URI or null, if no URI is present
     */
    public String getSchemeURI() {
        return schemeURI;
    }
}
