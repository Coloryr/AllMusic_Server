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

public class XMLMetadataSampleEntry extends MetadataSampleEntry {

    private String namespace, schemaLocation;

    public XMLMetadataSampleEntry() {
        super("XML Metadata Sample Entry");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        namespace = in.readUTFString((int) getLeft(in), MP4InputStream.UTF8);
        schemaLocation = in.readUTFString((int) getLeft(in), MP4InputStream.UTF8);
    }

    /**
     * Gives the namespace of the schema for the timed XML metadata. This is
     * needed for identifying the type of metadata, e.g. gBSD or AQoS
     * (MPEG-21-7) and for decoding using XML aware encoding mechanisms such as
     * BiM.
     *
     * @return the namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Optionally provides an URL to find the schema corresponding to the
     * namespace. This is needed for decoding of the timed metadata by XML aware
     * encoding mechanisms such as BiM.
     *
     * @return the schema's URL
     */
    public String getSchemaLocation() {
        return schemaLocation;
    }
}
