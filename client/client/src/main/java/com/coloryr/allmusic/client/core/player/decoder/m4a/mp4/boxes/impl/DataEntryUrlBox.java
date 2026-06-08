package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

public class DataEntryUrlBox extends FullBox {

    private boolean inFile;
    private String location;

    public DataEntryUrlBox() {
        super("Data Entry Url Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        inFile = (flags & 1) == 1;
        if (!inFile) location = in.readUTFString((int) getLeft(in), MP4InputStream.UTF8);
    }

    public boolean isInFile() {
        return inFile;
    }

    public String getLocation() {
        return location;
    }
}
