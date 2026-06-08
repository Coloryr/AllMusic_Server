package com.coloryr.allmusic.client.core.player.decoder.m4a.adts;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class ADTSDemultiplexer {

    private static final int MAXIMUM_FRAME_SIZE = 6144;
    private PushbackInputStream in;
    private DataInputStream din;
    private boolean first;
    private ADTSFrame frame;

    public ADTSDemultiplexer(InputStream in) throws IOException {
        this.in = new PushbackInputStream(in);
        din = new DataInputStream(this.in);
        first = true;
        if (!findNextFrame()) throw new IOException("no ADTS header found");
    }

    public byte[] getDecoderSpecificInfo() {
        return frame.createDecoderSpecificInfo();
    }

    public byte[] readNextFrame() throws IOException {
        if (first) first = false;
        else findNextFrame();

        byte[] b = new byte[frame.getFrameLength()];
        din.readFully(b);
        return b;
    }

    private boolean findNextFrame() throws IOException {
        //find next ADTS ID
        boolean found = false;
        int left = MAXIMUM_FRAME_SIZE;
        int i;
        while (!found && left > 0) {
            i = in.read();
            left--;
            if (i == 0xFF) {
                i = in.read();
                if (((i >> 4) & 0xF) == 0xF) found = true;
                in.unread(i);
            }
        }

        if (found) frame = new ADTSFrame(din);
        return found;
    }

    public int getSampleFrequency() {
        return frame.getSampleFrequency();
    }

    public int getChannelCount() {
        return frame.getChannelCount();
    }
}
