package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.fd;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * The FD session group box is optional, although it is mandatory for files
 * containing more than one FD hint track. It contains a list of sessions as
 * well as all file groups and hint tracks that belong to each session. An FD
 * session sends simultaneously over all FD hint tracks (channels) that are
 * listed in the FD session group box for a particular FD session.
 * <p>
 * Only one session group should be processed at any time. The first listed
 * hint track in a session group specifies the base channel. If the server has
 * no preference between the session groups, the default choice should be the
 * first session group. The group IDs of all file groups containing the files
 * referenced by the hint tracks shall be included in the list of file groups.
 * The file group IDs can in turn be translated into file group names (using the
 * group ID to name box) that can be included by the server in FDTs.
 *
 * @author in-somnia
 */
public class FDSessionGroupBox extends FullBox {

    private long[][] groupIDs, hintTrackIDs;

    public FDSessionGroupBox() {
        super("FD Session Group Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        final int sessionGroups = (int) in.readBytes(2);
        groupIDs = new long[sessionGroups][];
        hintTrackIDs = new long[sessionGroups][];

        int j, entryCount, channelsInSessionGroup;
        for (int i = 0; i < sessionGroups; i++) {
            entryCount = in.read();
            groupIDs[i] = new long[entryCount];
            for (j = 0; j < entryCount; j++) {
                groupIDs[i][j] = in.readBytes(4);
            }

            channelsInSessionGroup = (int) in.readBytes(2);
            hintTrackIDs[i] = new long[channelsInSessionGroup];
            for (j = 0; j < channelsInSessionGroup; j++) {
                hintTrackIDs[i][j] = in.readBytes(4);
            }
        }
    }

    /**
     * A group ID indicates a file group that the session group complies with.
     *
     * @return all group IDs for all session groups
     */
    public long[][] getGroupIDs() {
        return groupIDs;
    }

    /**
     * A hint track ID specifies the track ID of the FD hint track belonging to
     * a particular session group. Note that one FD hint track corresponds to
     * one LCT channel.
     *
     * @return all hint track IDs for all session groups
     */
    public long[][] getHintTrackIDs() {
        return hintTrackIDs;
    }
}
