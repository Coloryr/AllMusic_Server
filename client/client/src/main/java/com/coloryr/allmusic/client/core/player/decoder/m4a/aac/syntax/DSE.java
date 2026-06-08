package com.coloryr.allmusic.client.core.player.decoder.m4a.aac.syntax;

import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.AACException;

class DSE extends Element {

    private byte[] dataStreamBytes;

    DSE() {
        super();
    }

    void decode(BitStream in) throws AACException {
        final boolean byteAlign = in.readBool();
        int count = in.readBits(8);
        if (count == 255) count += in.readBits(8);

        if (byteAlign) in.byteAlign();

        dataStreamBytes = new byte[count];
        for (int i = 0; i < count; i++) {
            dataStreamBytes[i] = (byte) in.readBits(8);
        }
    }
}
