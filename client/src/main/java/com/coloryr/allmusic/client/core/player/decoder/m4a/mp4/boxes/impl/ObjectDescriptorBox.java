package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.od.Descriptor;

import java.io.IOException;

public class ObjectDescriptorBox extends FullBox {

    private Descriptor objectDescriptor;

    public ObjectDescriptorBox() {
        super("Object Descriptor Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        objectDescriptor = Descriptor.createDescriptor(in);
    }

    public Descriptor getObjectDescriptor() {
        return objectDescriptor;
    }
}
