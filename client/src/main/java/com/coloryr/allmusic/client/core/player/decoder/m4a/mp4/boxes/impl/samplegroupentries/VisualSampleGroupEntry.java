package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.samplegroupentries;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;

import java.io.IOException;

public class VisualSampleGroupEntry extends SampleGroupDescriptionEntry {

    public VisualSampleGroupEntry() {
        super("Video Sample Group Entry");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
    }

}
