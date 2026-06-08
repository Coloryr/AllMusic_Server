package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.meta;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * This box contains the data for a metadata tag. It is right below an
 * iTunes metadata box (e.g. '@nam') or a custom meta tag box ('----'). A custom
 * meta tag box also contains a 'name'-box declaring the tag's name.
 *
 * @author in-somnia
 */
/*TODO: use generics here? -> each DataType should return <T> corresponding to
its class (String/Integer/...)*/
public class ITunesMetadataBox extends FullBox {

    private static final String[] TIMESTAMPS = {"yyyy", "yyyy-MM", "yyyy-MM-dd"};
    private DataType dataType;
    private byte[] data;

    public ITunesMetadataBox() {
        super("iTunes Metadata Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        dataType = DataType.forInt(flags);

        in.skipBytes(4); //padding?

        data = new byte[(int) getLeft(in)];
        in.readBytes(data);
    }

    public DataType getDataType() {
        return dataType;
    }

    /**
     * Returns an unmodifiable array with the raw content, that can be present
     * in different formats.
     *
     * @return the raw metadata
     */
    public byte[] getData() {
        return Arrays.copyOf(data, data.length);
    }

    /**
     * Returns the content as a text string.
     *
     * @return the metadata as text
     */
    public String getText() {
        //first four bytes are padding (zero)
        return new String(data, 0, data.length, Charset.forName("UTF-8"));
    }

    /**
     * Returns the content as an unsigned 8-bit integer.
     *
     * @return the metadata as an integer
     */
    public long getNumber() {
        //first four bytes are padding (zero)
        long l = 0;
        for (int i = 0; i < data.length; i++) {
            l <<= 8;
            l |= (data[i] & 0xFF);
        }
        return l;
    }

    public int getInteger() {
        return (int) getNumber();
    }

    /**
     * Returns the content as a boolean (flag) value.
     *
     * @return the metadata as a boolean
     */
    public boolean getBoolean() {
        return getNumber() != 0;
    }

    public Date getDate() {
        //timestamp lengths: 4,7,9
        final int i = (int) Math.floor(data.length / 3) - 1;
        final Date date;
        if (i >= 0 && i < TIMESTAMPS.length) {
            final SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMPS[i]);
            date = sdf.parse(new String(data), new ParsePosition(0));
        } else date = null;
        return date;
    }

    public enum DataType {

        IMPLICIT(/*Object.class*/),
        UTF8(/*String.class*/),
        UTF16(/*String.class*/),
        HTML(/*String.class*/),
        XML(/*String.class*/),
        UUID(/*Long.class*/),
        ISRC(/*String.class*/),
        MI3P(/*String.class*/),
        GIF(/*byte[].class*/),
        JPEG(/*byte[].class*/),
        PNG(/*byte[].class*/),
        URL(/*String.class*/),
        DURATION(/*Long.class*/),
        DATETIME(/*Long.class*/),
        GENRE(/*Integer.class*/),
        INTEGER(/*Long.class*/),
        RIAA(/*Integer.class*/),
        UPC(/*String.class*/),
        BMP(/*byte[].class*/),
        UNDEFINED(/*byte[].class*/);
        private static final DataType[] TYPES = {
                IMPLICIT, UTF8, UTF16, null, null, null, HTML, XML, UUID, ISRC, MI3P, null,
                GIF, JPEG, PNG, URL, DURATION, DATETIME, GENRE, null, null, INTEGER,
                null, null, RIAA, UPC, null, BMP
        };

        private DataType() {
        }

        private static DataType forInt(int i) {
            DataType type = null;
            if (i >= 0 && i < TYPES.length) type = TYPES[i];

            if (type == null) type = UNDEFINED;
            return type;
        }
    }
}
