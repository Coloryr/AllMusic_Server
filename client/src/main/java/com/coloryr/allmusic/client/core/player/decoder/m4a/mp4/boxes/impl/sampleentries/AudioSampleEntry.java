/*
 *  Copyright (C) 2011 in-somnia
 *
 *  This file is part of JAAD.
 *
 *  JAAD is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation; either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  JAAD is distributed in the hope that it will be useful, but WITHOUT

 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General
 *  Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library.
 *  If not, see <http://www.gnu.org/licenses/>.
 */
package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.sampleentries;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;

import java.io.IOException;

public class AudioSampleEntry extends SampleEntry {

    private int channelCount, sampleSize, sampleRate;

    public AudioSampleEntry(String name) {
        super(name);
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        in.skipBytes(8); //reserved
        channelCount = (int) in.readBytes(2);
        sampleSize = (int) in.readBytes(2);
        in.skipBytes(2); //pre-defined: 0
        in.skipBytes(2); //reserved
        sampleRate = (int) in.readBytes(2);
        in.skipBytes(2); //not used by samplerate

        readChildren(in);
    }

    public int getChannelCount() {
        return channelCount;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getSampleSize() {
        return sampleSize;
    }
}
