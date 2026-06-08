package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.od;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;

import java.io.IOException;

/**
 * The <code>ObjectDescriptor</code> consists of three different parts:
 * <p>
 * The first part uniquely labels the <code>ObjectDescriptor</code> within its
 * name scope by means of an ID. Media objects in the scene description use this
 * ID to refer to their object descriptor. An optional URL String indicates that
 * the actual object descriptor resides at a remote location.
 * <p>
 * The second part is a set of optional descriptors that support the inclusion
 * if future extensions as well as the transport of private data in a backward
 * compatible way.
 * <p>
 * The third part consists of a list of <code>ESDescriptors</code>, each
 * providing parameters for a single elementary stream that relates to the media
 * object as well as an optional set of object content information descriptors.
 *
 * @author in-somnia
 */
public class ObjectDescriptor extends Descriptor {

    private int objectDescriptorID;
    private boolean urlPresent;
    private String url;

    void decode(MP4InputStream in) throws IOException {
        //10 bits objectDescriptorID, 1 bit url flag, 5 bits reserved
        final int x = (int) in.readBytes(2);
        objectDescriptorID = (x >> 6) & 0x3FF;
        urlPresent = ((x >> 5) & 1) == 1;

        if (urlPresent) url = in.readString(size - 2);

        readChildren(in);
    }

    /**
     * The ID uniquely identifies this ObjectDescriptor within its name scope.
     * It should be within 0 and 1023 exclusively. The value 0 is forbidden and
     * the value 1023 is reserved.
     *
     * @return this ObjectDescriptor's ID
     */
    public int getObjectDescriptorID() {
        return objectDescriptorID;
    }

    /**
     * A flag that indicates the presence of a URL. If set, no profiles are
     * present.
     *
     * @return true if a URL is present
     */
    public boolean isURLPresent() {
        return urlPresent;
    }

    /**
     * A URL String that shall point to another InitialObjectDescriptor. If no
     * URL is present (if <code>isURLPresent()</code> returns false) this method
     * returns null.
     *
     * @return a URL String or null if none is present
     */
    public String getURL() {
        return url;
    }
}
