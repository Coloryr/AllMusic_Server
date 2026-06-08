package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.meta;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NeroMetadataTagsBox extends BoxImpl {

    private final Map<String, String> pairs;

    public NeroMetadataTagsBox() {
        super("Nero Metadata Tags Box");
        pairs = new HashMap<String, String>();
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        in.skipBytes(12); //meta box

        String key, val;
        int len;
        //TODO: what are the other skipped fields for?
        while (getLeft(in) > 0 && in.read() == 0x80) {
            in.skipBytes(2); //x80 x00 x06/x05
            key = in.readUTFString((int) getLeft(in), MP4InputStream.UTF8);
            in.skipBytes(4); //0x00 0x01 0x00 0x00 0x00
            len = in.read();
            val = in.readString(len);
            pairs.put(key, val);
        }
    }

    public Map<String, String> getPairs() {
        return pairs;
    }
}
