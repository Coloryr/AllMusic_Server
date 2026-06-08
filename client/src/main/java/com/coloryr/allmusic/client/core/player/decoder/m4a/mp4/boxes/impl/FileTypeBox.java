package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxImpl;

import java.io.IOException;

//TODO: 3gpp brands
public class FileTypeBox extends BoxImpl {

    public static final String BRAND_ISO_BASE_MEDIA = "isom";
    public static final String BRAND_ISO_BASE_MEDIA_2 = "iso2";
    public static final String BRAND_ISO_BASE_MEDIA_3 = "iso3";
    public static final String BRAND_MP4_1 = "mp41";
    public static final String BRAND_MP4_2 = "mp42";
    public static final String BRAND_MOBILE_MP4 = "mmp4";
    public static final String BRAND_QUICKTIME = "qm  ";
    public static final String BRAND_AVC = "avc1";
    public static final String BRAND_AUDIO = "M4A ";
    public static final String BRAND_AUDIO_2 = "M4B ";
    public static final String BRAND_AUDIO_ENCRYPTED = "M4P ";
    public static final String BRAND_MP7 = "mp71";
    protected String majorBrand, minorVersion;
    protected String[] compatibleBrands;

    public FileTypeBox() {
        super("File Type Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        majorBrand = in.readString(4);
        minorVersion = in.readString(4);
        compatibleBrands = new String[(int) getLeft(in) / 4];
        for (int i = 0; i < compatibleBrands.length; i++) {
            compatibleBrands[i] = in.readString(4);
        }
    }

    public String getMajorBrand() {
        return majorBrand;
    }

    public String getMinorVersion() {
        return minorVersion;
    }

    public String[] getCompatibleBrands() {
        return compatibleBrands;
    }
}
