package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxTypes;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.Utils;

import java.io.IOException;

/**
 * The Copyright box contains a copyright declaration which applies to the
 * entire presentation, when contained within the Movie Box, or, when contained
 * in a track, to that entire track. There may be multiple copyright boxes using
 * different language codes.
 */
public class CopyrightBox extends FullBox {

    private String languageCode, notice;

    public CopyrightBox() {
        super("Copyright Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        if (parent.getType() == BoxTypes.USER_DATA_BOX) {
            super.decode(in);
            //1 bit padding, 5*3 bits language code (ISO-639-2/T)
            languageCode = Utils.getLanguageCode(in.readBytes(2));

            notice = in.readUTFString((int) getLeft(in));
        } else if (parent.getType() == BoxTypes.ITUNES_META_LIST_BOX) readChildren(in);
    }

    /**
     * The language code for the following text. See ISO 639-2/T for the set of
     * three character codes.
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * The copyright notice.
     */
    public String getNotice() {
        return notice;
    }
}
