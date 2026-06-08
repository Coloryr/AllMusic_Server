package com.coloryr.allmusic.client.core.player.decoder.m4a.aac.syntax;

import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.AACException;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.DecoderConfig;

class SCE_LFE extends Element {

    private final ICStream ics;

    SCE_LFE(int frameLength) {
        super();
        ics = new ICStream(frameLength);
    }

    void decode(BitStream in, DecoderConfig conf) throws AACException {
        readElementInstanceTag(in);
        ics.decode(in, false, conf);
    }

    public ICStream getICStream() {
        return ics;
    }
}
