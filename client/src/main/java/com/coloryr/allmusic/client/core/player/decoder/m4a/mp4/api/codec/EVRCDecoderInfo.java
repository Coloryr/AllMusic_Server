package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api.codec;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api.DecoderInfo;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.sampleentries.codec.CodecSpecificBox;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.sampleentries.codec.EVRCSpecificBox;

public class EVRCDecoderInfo extends DecoderInfo {

    private EVRCSpecificBox box;

    public EVRCDecoderInfo(CodecSpecificBox box) {
        this.box = (EVRCSpecificBox) box;
    }

    public int getDecoderVersion() {
        return box.getDecoderVersion();
    }

    public long getVendor() {
        return box.getVendor();
    }

    public int getFramesPerSample() {
        return box.getFramesPerSample();
    }
}
