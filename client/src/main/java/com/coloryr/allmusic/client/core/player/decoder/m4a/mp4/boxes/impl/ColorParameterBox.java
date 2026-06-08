package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

//TODO: check decoding, add get-methods
public class ColorParameterBox extends FullBox {

    private long colorParameterType;
    private int primariesIndex, transferFunctionIndex, matrixIndex;

    public ColorParameterBox() {
        super("Color Parameter Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        colorParameterType = in.readBytes(4);
        primariesIndex = (int) in.readBytes(2);
        transferFunctionIndex = (int) in.readBytes(2);
        matrixIndex = (int) in.readBytes(2);
    }
}
