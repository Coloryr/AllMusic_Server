package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.Utils;

import java.io.IOException;

/**
 * This box specifies the characteristics of a single track. Exactly one Track
 * Header Box is contained in a track. In the absence of an edit list, the
 * presentation of a track starts at the beginning of the overall presentation.
 * An empty edit is used to offset the start time of a track.
 * If in a presentation all tracks have neither trackInMovie nor trackInPreview
 * set, then all tracks shall be treated as if both flags were set on all
 * tracks. Hint tracks should not have the track header flags set, so that they
 * are ignored for local playback and preview.
 * The width and height in the track header are measured on a notional 'square'
 * (uniform) grid. Track video data is normalized to these dimensions
 * (logically) before any transformation or placement caused by a layup or
 * composition system. Track (and movie) matrices, if used, also operate in this
 * uniformly-scaled space.
 *
 * @author in-somnia
 */
public class TrackHeaderBox extends FullBox {

    private boolean enabled, inMovie, inPreview;
    private long creationTime, modificationTime, duration;
    private int trackID, layer, alternateGroup;
    private double volume, width, height;
    private double[] matrix;

    public TrackHeaderBox() {
        super("Track Header Box");
        matrix = new double[9];
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        enabled = (flags & 1) == 1;
        inMovie = (flags & 2) == 2;
        inPreview = (flags & 4) == 4;

        final int len = (version == 1) ? 8 : 4;
        creationTime = in.readBytes(len);
        modificationTime = in.readBytes(len);
        trackID = (int) in.readBytes(4);
        in.skipBytes(4); //reserved
        duration = Utils.detectUndetermined(in.readBytes(len));

        in.skipBytes(8); //reserved

        layer = (int) in.readBytes(2);
        alternateGroup = (int) in.readBytes(2);
        volume = in.readFixedPoint(8, 8);

        in.skipBytes(2); //reserved

        for (int i = 0; i < 9; i++) {
            if (i < 6) matrix[i] = in.readFixedPoint(16, 16);
            else matrix[i] = in.readFixedPoint(2, 30);
        }

        width = in.readFixedPoint(16, 16);
        height = in.readFixedPoint(16, 16);
    }

    /**
     * A flag indicating that the track is enabled. A disabled track is treated
     * as if it were not present.
     *
     * @return true if the track is enabled
     */
    public boolean isTrackEnabled() {
        return enabled;
    }

    /**
     * A flag indicating that the track is used in the presentation.
     *
     * @return true if the track is used
     */
    public boolean isTrackInMovie() {
        return inMovie;
    }

    /**
     * A flag indicating that the track is used when previewing the
     * presentation.
     *
     * @return true if the track is used in previews
     */
    public boolean isTrackInPreview() {
        return inPreview;
    }

    /**
     * The creation time is an integer that declares the creation time of the
     * presentation in seconds since midnight, Jan. 1, 1904, in UTC time.
     *
     * @return the creation time
     */
    public long getCreationTime() {
        return creationTime;
    }

    /**
     * The modification time is an integer that declares the most recent time
     * the presentation was modified in seconds since midnight, Jan. 1, 1904,
     * in UTC time.
     */
    public long getModificationTime() {
        return modificationTime;
    }

    /**
     * The track ID is an integer that uniquely identifies this track over the
     * entire life-time of this presentation. Track IDs are never re-used and
     * cannot be zero.
     *
     * @return the track's ID
     */
    public int getTrackID() {
        return trackID;
    }

    /**
     * The duration is an integer that indicates the duration of this track (in
     * the timescale indicated in the Movie Header Box). The value of this field
     * is equal to the sum of the durations of all of the track's edits. If
     * there is no edit list, then the duration is the sum of the sample
     * durations, converted into the timescale in the Movie Header Box. If the
     * duration of this track cannot be determined then this value is -1.
     *
     * @return the duration this track
     */
    public long getDuration() {
        return duration;
    }

    /**
     * The layer specifies the front-to-back ordering of video tracks; tracks
     * with lower numbers are closer to the viewer. 0 is the normal value, and
     * -1 would be in front of track 0, and so on.
     *
     * @return the layer
     */
    public int getLayer() {
        return layer;
    }

    /**
     * The alternate group is an integer that specifies a group or collection
     * of tracks. If this field is 0 there is no information on possible
     * relations to other tracks. If this field is not 0, it should be the same
     * for tracks that contain alternate data for one another and different for
     * tracks belonging to different such groups. Only one track within an
     * alternate group should be played or streamed at any one time, and must be
     * distinguishable from other tracks in the group via attributes such as
     * bitrate, codec, language, packet size etc. A group may have only one
     * member.
     *
     * @return the alternate group
     */
    public int getAlternateGroup() {
        return alternateGroup;
    }

    /**
     * The volume is a floating point number that indicates the preferred
     * playback volume: 0.0 is mute, 1.0 is normal volume.
     *
     * @return the volume
     */
    public double getVolume() {
        return volume;
    }

    /**
     * The width specifies the track's visual presentation width as a floating
     * point values. This needs not be the same as the pixel width of the
     * images, which is documented in the sample description(s); all images in
     * the sequence are scaled to this width, before any overall transformation
     * of the track represented by the matrix. The pixel dimensions of the
     * images are the default values.
     *
     * @return the image width
     */
    public double getWidth() {
        return width;
    }

    /**
     * The height specifies the track's visual presentation height as a floating
     * point value. This needs not be the same as the pixel height of the
     * images, which is documented in the sample description(s); all images in
     * the sequence are scaled to this height, before any overall transformation
     * of the track represented by the matrix. The pixel dimensions of the
     * images are the default values.
     *
     * @return the image height
     */
    public double getHeight() {
        return height;
    }

    public double[] getMatrix() {
        return matrix;
    }
}
