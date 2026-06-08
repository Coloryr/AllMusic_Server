package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.sampleentries.codec;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxImpl;

import java.io.IOException;

/**
 * The <code>CodecSpecificBox</code> can be used instead of an <code>ESDBox</code>
 * in a sample entry. It contains <code>DecoderSpecificInfo</code>s.
 *
 * @author in-somnia
 */
public abstract class CodecSpecificBox extends BoxImpl {

    private long vendor;
    private int decoderVersion;

    public CodecSpecificBox(String name) {
        super(name);
    }

    protected void decodeCommon(MP4InputStream in) throws IOException {
        vendor = in.readBytes(4);
        decoderVersion = in.read();
    }

    public long getVendor() {
        return vendor;
    }

    public int getDecoderVersion() {
        return decoderVersion;
    }
}
