package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ID3Tag {

    private static final int ID3_TAG = 4801587; //'ID3'
    private static final int SUPPORTED_VERSION = 4; //id3v2.4
    private final List<ID3Frame> frames;
    private final int tag, flags, len;

    ID3Tag(DataInputStream in) throws IOException {
        frames = new ArrayList<ID3Frame>();

        //id3v2 header
        tag = (in.read() << 16) | (in.read() << 8) | in.read(); //'ID3'
        final int majorVersion = in.read();
        in.read(); //revision
        flags = in.read();
        len = readSynch(in);

        if (tag == ID3_TAG && majorVersion <= SUPPORTED_VERSION) {
            if ((flags & 0x40) == 0x40) {
                //extended header; TODO: parse
                final int extSize = readSynch(in);
                in.skipBytes(extSize - 6);
            }

            //read all id3 frames
            int left = len;
            ID3Frame frame;
            while (left > 0) {
                frame = new ID3Frame(in);
                frames.add(frame);
                left -= frame.getSize();
            }
        }
    }

    static int readSynch(DataInputStream in) throws IOException {
        int x = 0;
        for (int i = 0; i < 4; i++) {
            x |= (in.read() & 0x7F);
        }
        return x;
    }

    public List<ID3Frame> getFrames() {
        return Collections.unmodifiableList(frames);
    }
}
