package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.fd;

import java.io.*;

/**
 * A BASE64 character decoder.
 */
class Base64Decoder {

    /*private static final char[] CHAR_ARRAY = {
    //       0   1   2   3   4   5   6   7
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', // 0
    'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', // 1
    'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', // 2
    'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', // 3
    'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', // 4
    'o', 'p', 'q', 'r', 's', 't', 'u', 'v', // 5
    'w', 'x', 'y', 'z', '0', '1', '2', '3', // 6
    '4', '5', '6', '7', '8', '9', '+', '/' // 7
    };*/
    //CHAR_CONVERT_ARRAY[CHAR_ARRAY[i]] = i;
    private static final byte[] CHAR_CONVERT_ARRAY = {
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57,
            58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1,
            -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38,
            39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, 0
    };

    public static byte[] decode(byte[] b) {
        ByteArrayInputStream in = new ByteArrayInputStream(b);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int i;

        PushbackInputStream ps = new PushbackInputStream(in);
        try {
            while (true) {
                for (i = 0; (i + 4) < 72; i += 4) {
                    decodeAtom(ps, out, 4);
                }
                if ((i + 4) == 72) decodeAtom(ps, out, 4);
                else decodeAtom(ps, out, 72 - i);
            }
        } catch (IOException e) {
        }
        return out.toByteArray();
    }

    private static void decodeAtom(InputStream in, OutputStream out, int rem) throws IOException {
        if (rem < 2) throw new IOException();

        int i;
        do {
            i = in.read();
            if (i == -1) throw new IOException();
        }
        while (i == '\n' || i == '\r');

        final byte[] buf = new byte[4];
        buf[0] = (byte) i;

        i = readFully(in, buf, 1, rem - 1);
        if (i == -1) throw new IOException();

        if (rem > 3 && buf[3] == '=') rem = 3;
        if (rem > 2 && buf[2] == '=') rem = 2;

        byte a = -1, b = -1, c = -1, d = -1;
        switch (rem) {
            case 4:
                d = CHAR_CONVERT_ARRAY[buf[3] & 0xff];
            case 3:
                c = CHAR_CONVERT_ARRAY[buf[2] & 0xff];
            case 2:
                b = CHAR_CONVERT_ARRAY[buf[1] & 0xff];
                a = CHAR_CONVERT_ARRAY[buf[0] & 0xff];
                break;
        }

        switch (rem) {
            case 2:
                out.write((byte) (((a << 2) & 0xfc) | ((b >>> 4) & 3)));
                break;
            case 3:
                out.write((byte) (((a << 2) & 0xfc) | ((b >>> 4) & 3)));
                out.write((byte) (((b << 4) & 0xf0) | ((c >>> 2) & 0xf)));
                break;
            case 4:
                out.write((byte) (((a << 2) & 0xfc) | ((b >>> 4) & 3)));
                out.write((byte) (((b << 4) & 0xf0) | ((c >>> 2) & 0xf)));
                out.write((byte) (((c << 6) & 0xc0) | (d & 0x3f)));
                break;
        }
        return;
    }

    private static int readFully(InputStream in, byte[] b, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            int q = in.read();
            if (q == -1) return ((i == 0) ? -1 : i);
            b[i + off] = (byte) q;
        }
        return len;
    }
}
