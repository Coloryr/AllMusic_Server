package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * The sound media header contains general presentation information, independent
 * of the coding, for audio media. This header is used for all tracks containing
 * audio.
 *
 * @author in-somnia
 */
public class SoundMediaHeaderBox extends FullBox {

    private double balance;

    public SoundMediaHeaderBox() {
        super("Sound Media Header Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        balance = in.readFixedPoint(8, 8);
        in.skipBytes(2); //reserved
    }

    /**
     * The balance is a floating-point number that places mono audio tracks in a
     * stereo space: 0 is centre (the normal value), full left is -1.0 and full
     * right is 1.0.
     *
     * @return the stereo balance for a mono track
     */
    public double getBalance() {
        return balance;
    }
}
