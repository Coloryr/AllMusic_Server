package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.awt.*;
import java.io.IOException;

/**
 * The video media header contains general presentation information, independent
 * of the coding, for video media
 *
 * @author in-somnia
 */
public class VideoMediaHeaderBox extends FullBox {

    private long graphicsMode;
    private Color color;

    public VideoMediaHeaderBox() {
        super("Video Media Header Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        graphicsMode = in.readBytes(2);
        //6 byte RGB color
        final int[] c = new int[3];
        for (int i = 0; i < 3; i++) {
            c[i] = (in.read() & 0xFF) | ((in.read() << 8) & 0xFF);
        }
        color = new Color(c[0], c[1], c[2]);
    }

    /**
     * The graphics mode specifies a composition mode for this video track.
     * Currently, only one mode is defined:
     * '0': copy over the existing image
     */
    public long getGraphicsMode() {
        return graphicsMode;
    }

    /**
     * A color available for use by graphics modes.
     */
    public Color getColor() {
        return color;
    }
}
