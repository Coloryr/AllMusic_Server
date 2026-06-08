package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes;

public final class Utils {

    private static final long UNDETERMINED = 4294967295l;

    public static String getLanguageCode(long l) {
        //1 bit padding, 5*3 bits language code (ISO-639-2/T)
        char[] c = new char[3];
        c[0] = (char) (((l >> 10) & 31) + 0x60);
        c[1] = (char) (((l >> 5) & 31) + 0x60);
        c[2] = (char) ((l & 31) + 0x60);
        return new String(c);
    }

    public static long detectUndetermined(long l) {
        final long x;
        if (l == UNDETERMINED) x = -1;
        else x = l;
        return x;
    }
}
