package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.meta;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxTypes;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.Utils;

import java.io.IOException;

public class RatingBox extends FullBox {

    private String languageCode, rating;

    public RatingBox() {
        super("Rating Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        //3gpp or iTunes
        if (parent.getType() == BoxTypes.USER_DATA_BOX) {
            super.decode(in);

            //TODO: what to do with both?
            final long entity = in.readBytes(4);
            final long criteria = in.readBytes(4);
            languageCode = Utils.getLanguageCode(in.readBytes(2));
            final byte[] b = in.readTerminated((int) getLeft(in), 0);
            rating = new String(b, MP4InputStream.UTF8);
        } else readChildren(in);
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getRating() {
        return rating;
    }
}
