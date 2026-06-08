package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.Box;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxTypes;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.HandlerBox;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.MovieHeaderBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Movie {

    private final MP4InputStream in;
    private final MovieHeaderBox mvhd;
    private final List<Track> tracks;
    private final MetaData metaData;
    private final List<Protection> protections;

    public Movie(Box moov, MP4InputStream in) {
        this.in = in;

        //create tracks
        mvhd = (MovieHeaderBox) moov.getChild(BoxTypes.MOVIE_HEADER_BOX);
        List<Box> trackBoxes = moov.getChildren(BoxTypes.TRACK_BOX);
        tracks = new ArrayList<Track>(trackBoxes.size());
        Track track;
        for (int i = 0; i < trackBoxes.size(); i++) {
            track = createTrack(trackBoxes.get(i));
            if (track != null) tracks.add(track);
        }

        //read metadata: moov.meta/moov.udta.meta
        metaData = new MetaData();
        if (moov.hasChild(BoxTypes.META_BOX)) metaData.parse(null, moov.getChild(BoxTypes.META_BOX));
        else if (moov.hasChild(BoxTypes.USER_DATA_BOX)) {
            final Box udta = moov.getChild(BoxTypes.USER_DATA_BOX);
            if (udta.hasChild(BoxTypes.META_BOX)) metaData.parse(udta, udta.getChild(BoxTypes.META_BOX));
        }

        //detect DRM
        protections = new ArrayList<Protection>();
        if (moov.hasChild(BoxTypes.ITEM_PROTECTION_BOX)) {
            Box ipro = moov.getChild(BoxTypes.ITEM_PROTECTION_BOX);
            for (Box sinf : ipro.getChildren(BoxTypes.PROTECTION_SCHEME_INFORMATION_BOX)) {
                protections.add(Protection.parse(sinf));
            }
        }
    }

    //TODO: support hint and meta
    private Track createTrack(Box trak) {
        final HandlerBox hdlr = (HandlerBox) trak.getChild(BoxTypes.MEDIA_BOX).getChild(BoxTypes.HANDLER_BOX);
        final Track track;
        switch ((int) hdlr.getHandlerType()) {
            case HandlerBox.TYPE_VIDEO:
                track = new VideoTrack(trak, in);
                break;
            case HandlerBox.TYPE_SOUND:
                track = new AudioTrack(trak, in);
                break;
            default:
                track = null;
        }
        return track;
    }

    /**
     * Returns an unmodifiable list of all tracks in this movie. The tracks are
     * ordered as they appeare in the file/stream.
     *
     * @return the tracks contained by this movie
     */
    public List<Track> getTracks() {
        return Collections.unmodifiableList(tracks);
    }

    /**
     * Returns an unmodifiable list of all tracks in this movie with the
     * specified type. The tracks are ordered as they appeare in the
     * file/stream.
     *
     * @return the tracks contained by this movie with the passed type
     */
    public List<Track> getTracks(Type type) {
        final List<Track> l = new ArrayList<Track>();
        for (Track t : tracks) {
            if (t.getType().equals(type)) l.add(t);
        }
        return Collections.unmodifiableList(l);
    }

    /**
     * Returns an unmodifiable list of all tracks in this movie whose samples
     * are encoded with the specified codec. The tracks are ordered as they
     * appeare in the file/stream.
     *
     * @return the tracks contained by this movie with the passed type
     */
    public List<Track> getTracks(Track.Codec codec) {
        final List<Track> l = new ArrayList<Track>();
        for (Track t : tracks) {
            if (t.getCodec().equals(codec)) l.add(t);
        }
        return Collections.unmodifiableList(l);
    }

    /**
     * Indicates if this movie contains metadata. If false the <code>MetaData</code>
     * object returned by <code>getMetaData()</code> will not contain any field.
     *
     * @return true if this movie contains any metadata
     */
    public boolean containsMetaData() {
        return metaData.containsMetaData();
    }

    /**
     * Returns the MetaData object for this movie.
     *
     * @return the MetaData for this movie
     */
    public MetaData getMetaData() {
        return metaData;
    }

    /**
     * Returns the <code>ProtectionInformation</code> objects that contains
     * details about the DRM systems used. If no protection is present the
     * returned list will be empty.
     *
     * @return a list of protection informations
     */
    public List<Protection> getProtections() {
        return Collections.unmodifiableList(protections);
    }

    //mvhd

    /**
     * Returns the time this movie was created.
     *
     * @return the creation time
     */
    public Date getCreationTime() {
        return Utils.getDate(mvhd.getCreationTime());
    }

    /**
     * Returns the last time this movie was modified.
     *
     * @return the modification time
     */
    public Date getModificationTime() {
        return Utils.getDate(mvhd.getModificationTime());
    }

    /**
     * Returns the duration in seconds.
     *
     * @return the duration
     */
    public double getDuration() {
        return (double) mvhd.getDuration() / (double) mvhd.getTimeScale();
    }

    /**
     * Indicates if there are more frames to be read in this movie.
     *
     * @return true if there is at least one track in this movie that has at least one more frame to read.
     */
    public boolean hasMoreFrames() {
        for (Track track : tracks) {
            if (track.hasMoreFrames()) return true;
        }
        return false;
    }

    /**
     * Reads the next frame from this movie (from one of the contained tracks).
     * The frame is the next in time-order, thus the next for playback. If none
     * of the tracks contains any more frames, null is returned.
     *
     * @return the next frame or null if there are no more frames to read from this movie.
     * @throws IOException if reading fails
     */
    public Frame readNextFrame() throws IOException {
        Track track = null;
        for (Track t : tracks) {
            if (t.hasMoreFrames() && (track == null || t.getNextTimeStamp() < track.getNextTimeStamp())) track = t;
        }

        return (track == null) ? null : track.readNextFrame();
    }
}
