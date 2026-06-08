package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.Utils;

import java.io.IOException;

/**
 * The movie header box defines overall information which is media-independent,
 * and relevant to the entire presentation considered as a whole.
 *
 * @author in-somnia
 */
public class MovieHeaderBox extends FullBox {

    private long creationTime, modificationTime, timeScale, duration;
    private double rate, volume;
    private double[] matrix;
    private long nextTrackID;

    public MovieHeaderBox() {
        super("Movie Header Box");
        matrix = new double[9];
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        final int len = (version == 1) ? 8 : 4;
        creationTime = in.readBytes(len);
        modificationTime = in.readBytes(len);
        timeScale = in.readBytes(4);
        duration = Utils.detectUndetermined(in.readBytes(len));

        rate = in.readFixedPoint(16, 16);
        volume = in.readFixedPoint(8, 8);

        in.skipBytes(10); //reserved

        for (int i = 0; i < 9; i++) {
            if (i < 6) matrix[i] = in.readFixedPoint(16, 16);
            else matrix[i] = in.readFixedPoint(2, 30);
        }

        in.skipBytes(24); //reserved

        nextTrackID = in.readBytes(4);
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
     * The time-scale is an integer that specifies the time-scale for the entire
     * presentation; this is the number of time units that pass in one second.
     * For example, a time coordinate system that measures time in sixtieths of
     * a second has a time scale of 60.
     *
     * @return the time-scale
     */
    public long getTimeScale() {
        return timeScale;
    }

    /**
     * The duration is an integer that declares length of the presentation (in
     * the indicated timescale). This property is derived from the
     * presentation's tracks: the value of this field corresponds to the
     * duration of the longest track in the presentation. If the duration cannot
     * be determined then duration is set to -1.
     *
     * @return the duration of the longest track
     */
    public long getDuration() {
        return duration;
    }

    /**
     * The rate is a floting point number that indicates the preferred rate
     * to play the presentation; 1.0 is normal forward playback
     *
     * @return the playback rate
     */
    public double getRate() {
        return rate;
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
     * Provides a transformation matrix for the video:
     * [A,B,U,C,D,V,X,Y,W]
     * A: width scale
     * B: width rotate
     * U: width angle
     * C: height rotate
     * D: height scale
     * V: height angle
     * X: position from left
     * Y: position from top
     * W: divider scale (restricted to 1.0)
     * <p>
     * The normal values for scale are 1.0 and for rotate 0.0.
     * The angles are restricted to 0.0.
     *
     * @return the transformation matrix for the video
     */
    public double[] getTransformationMatrix() {
        return matrix;
    }

    /**
     * The next-track-ID is a non-zero integer that indicates a value to use
     * for the track ID of the next track to be added to this presentation. Zero
     * is not a valid track ID value. The value shall be larger than the largest
     * track-ID in use. If this value is equal to all 1s (32-bit), and a new
     * media track is to be added, then a search must be made in the file for an
     * unused track identifier.
     *
     * @return the ID for the next track
     */
    public long getNextTrackID() {
        return nextTrackID;
    }
}
