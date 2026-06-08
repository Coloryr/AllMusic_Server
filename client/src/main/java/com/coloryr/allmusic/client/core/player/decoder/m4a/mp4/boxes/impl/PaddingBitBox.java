package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * In some streams the media samples do not occupy all bits of the bytes given
 * by the sample size, and are padded at the end to a byte boundary. In some
 * cases, it is necessary to record externally the number of padding bits used.
 * This table supplies that information.
 *
 * @author in-somnia
 */
public class PaddingBitBox extends FullBox {

    private int[] pad1, pad2;

    public PaddingBitBox() {
        super("Padding Bit Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        final int sampleCount = (int) (in.readBytes(4) + 1) / 2;
        pad1 = new int[sampleCount];
        pad2 = new int[sampleCount];

        byte b;
        for (int i = 0; i < sampleCount; i++) {
            b = (byte) in.read();
            //1 bit reserved
            //3 bits pad1
            pad1[i] = (b >> 4) & 7;
            //1 bit reserved
            //3 bits pad2
            pad2[i] = b & 7;
        }
    }

    /**
     * Integer values from 0 to 7, indicating the number of bits at the end of
     * sample (i*2)+1.
     */
    public int[] getPad1() {
        return pad1;
    }

    /**
     * Integer values from 0 to 7, indicating the number of bits at the end of
     * sample (i*2)+2.
     */
    public int[] getPad2() {
        return pad2;
    }
}
