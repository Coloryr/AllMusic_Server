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
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxImpl;

import java.io.IOException;

public abstract class SampleEntry extends BoxImpl {

    private long dataReferenceIndex;

    protected SampleEntry(String name) {
        super(name);
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        in.skipBytes(6); //reserved
        dataReferenceIndex = in.readBytes(2);
    }

    /**
     * The data reference index is an integer that contains the index of the
     * data reference to use to retrieve data associated with samples that use
     * this sample description. Data references are stored in Data Reference
     * Boxes. The index ranges from 1 to the number of data references.
     */
    public long getDataReferenceIndex() {
        return dataReferenceIndex;
    }
}
