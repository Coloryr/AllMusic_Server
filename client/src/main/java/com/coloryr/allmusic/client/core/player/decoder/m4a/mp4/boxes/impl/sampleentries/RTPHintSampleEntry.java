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

public class RTPHintSampleEntry extends SampleEntry {

    private int hintTrackVersion, highestCompatibleVersion;
    private long maxPacketSize;

    public RTPHintSampleEntry() {
        super("RTP Hint Sample Entry");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        hintTrackVersion = (int) in.readBytes(2);
        highestCompatibleVersion = (int) in.readBytes(2);
        maxPacketSize = in.readBytes(4);
    }

    /**
     * The maximum packet size indicates the size of the largest packet that
     * this track will generate.
     *
     * @return the maximum packet size
     */
    public long getMaxPacketSize() {
        return maxPacketSize;
    }
}
