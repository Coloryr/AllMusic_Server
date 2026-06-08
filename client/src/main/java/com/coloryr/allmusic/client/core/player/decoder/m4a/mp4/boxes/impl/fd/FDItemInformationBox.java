package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.fd;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * The FD item information box is optional, although it is mandatory for files
 * using FD hint tracks. It provides information on the partitioning of source
 * files and how FD hint tracks are combined into FD sessions. Each partition
 * entry provides details on a particular file partitioning, FEC encoding and
 * associated FEC reservoirs. It is possible to provide multiple entries for one
 * source file (identified by its item ID) if alternative FEC encoding schemes
 * or partitionings are used in the file. All partition entries are implicitly
 * numbered and the first entry has number 1.
 *
 * @author in-somnia
 */
public class FDItemInformationBox extends FullBox {

    public FDItemInformationBox() {
        super("FD Item Information Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        final int entryCount = (int) in.readBytes(2);
        readChildren(in, entryCount); //partition entries

        readChildren(in); //FDSessionGroupBox and GroupIDToNameBox
    }
}
