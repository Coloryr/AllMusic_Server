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

public class VideoSampleEntry extends SampleEntry {

    private int width, height;
    private double horizontalResolution, verticalResolution;
    private int frameCount, depth;
    private String compressorName;

    public VideoSampleEntry(String name) {
        super(name);
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        in.skipBytes(2); //pre-defined: 0
        in.skipBytes(2); //reserved
        //3x32 pre_defined
        in.skipBytes(4); //pre-defined: 0
        in.skipBytes(4); //pre-defined: 0
        in.skipBytes(4); //pre-defined: 0

        width = (int) in.readBytes(2);
        height = (int) in.readBytes(2);
        horizontalResolution = in.readFixedPoint(16, 16);
        verticalResolution = in.readFixedPoint(16, 16);
        in.skipBytes(4); //reserved
        frameCount = (int) in.readBytes(2);

        final int len = in.read();
        compressorName = in.readString(len);
        in.skipBytes(31 - len);

        depth = (int) in.readBytes(2);
        in.skipBytes(2); //pre-defined: -1

        readChildren(in);
    }

    /**
     * The width is the maximum visual width of the stream described by this
     * sample description, in pixels.
     */
    public int getWidth() {
        return width;
    }

    /**
     * The height is the maximum visual height of the stream described by this
     * sample description, in pixels.
     */
    public int getHeight() {
        return height;
    }

    /**
     * The horizontal resolution of the image in pixels-per-inch, as a floating
     * point value.
     */
    public double getHorizontalResolution() {
        return horizontalResolution;
    }

    /**
     * The vertical resolution of the image in pixels-per-inch, as a floating
     * point value.
     */
    public double getVerticalResolution() {
        return verticalResolution;
    }

    /**
     * The frame count indicates how many frames of compressed video are stored
     * in each sample.
     */
    public int getFrameCount() {
        return frameCount;
    }

    /**
     * The compressor name, for informative purposes.
     */
    public String getCompressorName() {
        return compressorName;
    }

    /**
     * The depth takes one of the following values
     * DEFAULT_DEPTH (0x18) – images are in colour with no alpha
     */
    public int getDepth() {
        return depth;
    }
}
