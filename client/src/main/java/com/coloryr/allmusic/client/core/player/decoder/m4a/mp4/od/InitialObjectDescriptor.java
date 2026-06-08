package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.od;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;

import java.io.IOException;

/**
 * The <code>InitialObjectDescriptor</code> is a variation of the
 * <code>ObjectDescriptor</code> that shall be used to gain initial access to
 * content.
 *
 * @author in-somnia
 */
public class InitialObjectDescriptor extends Descriptor {

    private int objectDescriptorID;
    private boolean urlPresent, includeInlineProfiles;
    private String url;
    private int odProfile, sceneProfile, audioProfile, visualProfile, graphicsProfile;

    @Override
    void decode(MP4InputStream in) throws IOException {
        //10 bits objectDescriptorID, 1 bit url flag, 1 bit
        //includeInlineProfiles flag, 4 bits reserved
        final int x = (int) in.readBytes(2);
        objectDescriptorID = (x >> 6) & 0x3FF;
        urlPresent = ((x >> 5) & 1) == 1;
        includeInlineProfiles = ((x >> 4) & 1) == 1;

        if (urlPresent) url = in.readString(size - 2);
        else {
            odProfile = in.read();
            sceneProfile = in.read();
            audioProfile = in.read();
            visualProfile = in.read();
            graphicsProfile = in.read();
        }

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
     * A flag that, if set, indicates that the subsequent profile indications
     * take into account the resources needed to process any content that may
     * be inlined.
     *
     * @return true if this ObjectDescriptor includes inline profiles
     */
    public boolean includesInlineProfiles() {
        return includeInlineProfiles;
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

    /**
     * A flag that indicates the presence of profiles. If set, no URL is
     * present.
     *
     * @return true if profiles are present
     */
    public boolean areProfilesPresent() {
        return !urlPresent;
    }

    //TODO: javadoc
    public int getODProfile() {
        return odProfile;
    }

    /**
     * An indication of the scene description profile required to process the
     * content associated with this InitialObjectDescriptor.<br />
     * The value should be one of the following:
     * 0x00: reserved for ISO use
     * 0x01: ISO 14496-1 XXXX profile
     * 0x02-0x7F: reserved for ISO use
     * 0x80-0xFD: user private
     * 0xFE: no scene description profile specified
     * 0xFF: no scene description capability required
     *
     * @return the scene profile
     */
    public int getSceneProfile() {
        return sceneProfile;
    }

    /**
     * An indication of the audio profile required to process the content
     * associated with this InitialObjectDescriptor.<br />
     * The value should be one of the following:
     * 0x00: reserved for ISO use
     * 0x01: ISO 14496-3 XXXX profile
     * 0x02-0x7F: reserved for ISO use
     * 0x80-0xFD: user private
     * 0xFE: no audio profile specified
     * 0xFF: no audio capability required
     *
     * @return the audio profile
     */
    public int getAudioProfile() {
        return audioProfile;
    }

    /**
     * An indication of the visual profile required to process the content
     * associated with this InitialObjectDescriptor.<br />
     * The value should be one of the following:
     * 0x00: reserved for ISO use
     * 0x01: ISO 14496-2 XXXX profile
     * 0x02-0x7F: reserved for ISO use
     * 0x80-0xFD: user private
     * 0xFE: no visual profile specified
     * 0xFF: no visual capability required
     *
     * @return the visual profile
     */
    public int getVisualProfile() {
        return visualProfile;
    }

    /**
     * An indication of the graphics profile required to process the content
     * associated with this InitialObjectDescriptor.<br />
     * The value should be one of the following:
     * 0x00: reserved for ISO use
     * 0x01: ISO 14496-1 XXXX profile
     * 0x02-0x7F: reserved for ISO use
     * 0x80-0xFD: user private
     * 0xFE: no graphics profile specified
     * 0xFF: no graphics capability required
     *
     * @return the graphics profile
     */
    public int getGraphicsProfile() {
        return graphicsProfile;
    }
}
