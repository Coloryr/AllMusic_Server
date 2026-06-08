package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A typical presentation stored in a file contains one alternate group per
 * media type: one for video, one for audio, etc. Such a file may include
 * several video tracks, although, at any point in time, only one of them should
 * be played or streamed. This is achieved by assigning all video tracks to the
 * same alternate group. (See subclause 8.3.2 for the definition of alternate
 * groups.)
 * <p>
 * All tracks in an alternate group are candidates for media selection, but it
 * may not make sense to switch between some of those tracks during a session.
 * One may for instance allow switching between video tracks at different
 * bitrates and keep frame size but not allow switching between tracks of
 * different frame size. In the same manner it may be desirable to enable
 * selection – but not switching – between tracks of different video codecs or
 * different audio languages.
 * <p>
 * The distinction between tracks for selection and switching is addressed by
 * assigning tracks to switch groups in addition to alternate groups. One
 * alternate group may contain one or more switch groups. All tracks in an
 * alternate group are candidates for media selection, while tracks in a switch
 * group are also available for switching during a session. Different switch
 * groups represent different operation points, such as different frame size,
 * high/low quality, etc.
 * <p>
 * For the case of non-scalable bitstreams, several tracks may be included in a
 * switch group. The same also applies to non-layered scalable bitstreams, such
 * as traditional AVC streams.
 * <p>
 * By labelling tracks with attributes it is possible to characterize them. Each
 * track can be labelled with a list of attributes which can be used to describe
 * tracks in a particular switch group or differentiate tracks that belong to
 * different switch groups.
 *
 * @author in-somnia
 */
public class TrackSelectionBox extends FullBox {

    private final List<Long> attributes;
    private long switchGroup;

    public TrackSelectionBox() {
        super("Track Selection Box");
        attributes = new ArrayList<Long>();
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        switchGroup = in.readBytes(4);

        while (getLeft(in) > 3) {
            attributes.add(in.readBytes(4));
        }
    }

    /**
     * The switch group is an integer that specifies a group or collection of
     * tracks. If this field is 0 (default value) or if the Track Selection box
     * is absent there is no information on whether the track can be used for
     * switching during playing or streaming. If this integer is not 0 it shall
     * be the same for tracks that can be used for switching between each other.
     * Tracks that belong to the same switch group shall belong to the same
     * alternate group. A switch group may have only one member.
     */
    public long getSwitchGroup() {
        return switchGroup;
    }

    /**
     * <p>A list of attributes, that should be used as descriptions of tracks or
     * differentiation criteria for tracks in the same alternate or switch
     * group. Each differentiating attribute is associated with a pointer to the
     * field or information that distinguishes the track.</p>
     *
     * <p>The following attributes are descriptive:
     * <table>
     * <tr><th>Name</th><th>Attribute</th><th>Description</th></tr>
     * <tr><td>Temporal scalability</td><td>'tesc'</td><td>The track can be
     * temporally scaled.</td></tr>
     * <tr><td>Fine-grain SNR scalability</td><td>'fgsc'</td><td>The track can
     * be fine-grain scaled.</td></tr>
     * <tr><td>Coarse-grain SNR scalability</td><td>'cgsc'</td><td>The track can
     * be coarse-grain scaled.</td></tr>
     * <tr><td>Spatial scalability</td><td>'spsc'</td><td>The track can be
     * spatially scaled.</td></tr>
     * <tr><td>Region-of-interest scalability</td><td>'resc'</td><td>The track
     * can be region-of-interest scaled.</td></tr>
     * </table></p>
     *
     * <p>The following attributes are differentiating:
     * <table><tr><th>Name</th><th>Attribute</th><th>Pointer</th></tr>
     * <tr><td>Codec</td><td>'cdec'</td><td>Sample Entry (in Sample Description
     * box of media track)</td></tr>
     * <tr><td>Screen size</td><td>'scsz'</td><td>Width and height fields of
     * Visual Sample Entries.</td></tr>
     * <tr><td>Max packet size</td><td>'mpsz'</td><td>Maxpacketsize field in RTP
     * Hint Sample Entry</td></tr>
     * <tr><td>Media type</td><td>'mtyp'</td><td>Handlertype in Handler box (of
     * media track)</td></tr>
     * <tr><td>Media language</td><td>'mela'</td><td>Language field in Media
     * Header box</td></tr>
     * <tr><td>Bitrate</td><td>'bitr'</td><td>Total size of the samples in the
     * track divided by the duration in the track header box</td></tr>
     * <tr><td>Frame rate</td><td>'frar'</td><td>Number of samples in the track
     * divided by duration in the track header box</td></tr>
     * </table></p>
     *
     * <p>Descriptive attributes characterize the tracks they modify, whereas
     * differentiating attributes differentiate between tracks that belong to
     * the same alternate or switch groups. The pointer of a differentiating
     * attribute indicates the location of the information that differentiates
     * the track from other tracks with the same attribute.</p>
     */
    public List<Long> getAttributes() {
        return Collections.unmodifiableList(attributes);
    }
}
