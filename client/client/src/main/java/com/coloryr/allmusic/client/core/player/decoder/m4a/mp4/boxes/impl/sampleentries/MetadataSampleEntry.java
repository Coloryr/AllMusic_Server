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

abstract class MetadataSampleEntry extends SampleEntry {

    private String contentEncoding;

    MetadataSampleEntry(String name) {
        super(name);
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        contentEncoding = in.readUTFString((int) getLeft(in), MP4InputStream.UTF8);
    }

    /**
     * A string providing a MIME type which identifies the content encoding of
     * the timed metadata. If not present (an empty string is supplied) the
     * timed metadata is not encoded.
     * An example for this field is 'application/zip'.
     *
     * @return the encoding's MIME-type
     */
    public String getContentEncoding() {
        return contentEncoding;
    }
}
