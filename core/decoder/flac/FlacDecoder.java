/*
 * FLAC library (Java)
 * Copyright (c) Project Nayuki
 * https://www.nayuki.io/page/flac-library-java
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program (see COPYING.txt and COPYING.LESSER.txt).
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.coloryr.allmusic.server.core.decoder.flac;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Handles high-level decoding and seeking in FLAC files. Also returns metadata blocks.
 * Every object is stateful, not thread-safe, and needs to be closed. Sample usage:
 *
 * <pre>
 * // Create a decoder
 * FlacDecoder dec = new FlacDecoder(...);
 *
 * &#x2F;/ Make the decoder process all metadata blocks internally.
 * &#x2F;/ We could capture the returned data for extra processing.
 * &#x2F;/ We must read all metadata before reading audio data.
 * while (dec.readAndHandleMetadataBlock() != null);
 *
 * &#x2F;/ Read audio samples starting from beginning
 * int[][] samples = (...);
 * dec.readAudioBlock(samples, ...);
 * dec.readAudioBlock(samples, ...);
 * dec.readAudioBlock(samples, ...);
 *
 * &#x2F;/ Seek to some position and continue reading
 * dec.seekAndReadAudioBlock(..., samples, ...);
 * dec.readAudioBlock(samples, ...);
 * dec.readAudioBlock(samples, ...);
 *
 * &#x2F;/ Close underlying file stream
 * dec.close();
 * </pre>
 *
 * @see FlacLowLevelInput
 */
public final class FlacDecoder implements AutoCloseable {
    /*---- Fields ----*/
    public StreamInfo streamInfo;
    public SeekTable seekTable;
    private FlacLowLevelInput input;
    private long metadataEndPos;

    // Constructs a new FLAC decoder to read the given file.
    // This immediately reads the basic header but not metadata blocks.
    public FlacDecoder(BufferedInputStream stream) {
        // Initialize streams
        this.input = new SeekableFileFlacInput(stream);
    }

    // Reads, handles, and returns the next metadata block. Returns a pair (Integer type, byte[] data) if the
    // next metadata block exists, otherwise returns null if the final metadata block was previously read.
    // In addition to reading and returning data, this method also updates the internal state
    // of this object to reflect the new data seen, and throws exceptions for situations such as
    // not starting with a stream info metadata block or encountering duplicates of certain blocks.
    public Object readAndHandleMetadataBlock() throws IOException {
        if (metadataEndPos != -1) return null; // All metadata already consumed

        // Read entire block
        boolean last = input.readUint(1) != 0;
        int type = input.readUint(7);
        int length = input.readUint(24);
        byte[] data = new byte[length];
        input.readFully(data);

        // Handle recognized block
        if (type == 0) {
            if (streamInfo != null) throw new DataFormatException("Duplicate stream info metadata block");
            streamInfo = new StreamInfo(data);
            return streamInfo;
        } else {
            if (streamInfo == null) throw new DataFormatException("Expected stream info metadata block");
            if (type == 3) {
                if (seekTable != null) throw new DataFormatException("Duplicate seek table metadata block");
                seekTable = new SeekTable(data);
                return seekTable;
            }
        }

        if (last) {
            metadataEndPos = input.getPosition();
        }
        return null;
    }

    // Closes the underlying input streams and discards object data.
    // This decoder object becomes invalid for any method calls or field usages.
    public void close() throws IOException {
        if (input != null) {
            streamInfo = null;
            seekTable = null;
            input.close();
            input = null;
        }
    }
}
