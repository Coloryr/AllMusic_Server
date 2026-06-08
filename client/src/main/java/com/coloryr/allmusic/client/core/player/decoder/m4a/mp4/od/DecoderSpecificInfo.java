package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.od;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;

import java.io.IOException;

/**
 * The <code>DecoderSpecificInfo</code> constitutes an opaque container with
 * information for a specific media decoder. Depending on the required amout of
 * data, two classes with a maximum of 255 and 2<sup>32</sup>-1 bytes of data
 * are provided. The existence and semantics of the
 * <code>DecoderSpecificInfo</code> depends on the stream type and object
 * profile of the parent <code>DecoderConfigDescriptor</code>.
 *
 * @author in-somnia
 */
public class DecoderSpecificInfo extends Descriptor {

    private byte[] data;

    @Override
    void decode(MP4InputStream in) throws IOException {
        data = new byte[size];
        in.readBytes(data);
    }

    /**
     * A byte array containing the decoder specific information.
     *
     * @return the decoder specific information
     */
    public byte[] getData() {
        return data;
    }
}
