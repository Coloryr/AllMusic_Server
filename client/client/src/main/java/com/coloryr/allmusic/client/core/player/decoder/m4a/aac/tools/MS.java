package com.coloryr.allmusic.client.core.player.decoder.m4a.aac.tools;

import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.huffman.HCB;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.syntax.CPE;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.syntax.Constants;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.syntax.ICSInfo;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.syntax.ICStream;

/**
 * Mid/side stereo
 *
 * @author in-somnia
 */
public final class MS implements Constants, HCB {

    private MS() {
    }

    public static void process(CPE cpe, float[] specL, float[] specR) {
        final ICStream ics = cpe.getLeftChannel();
        final ICSInfo info = ics.getInfo();
        final int[] offsets = info.getSWBOffsets();
        final int windowGroups = info.getWindowGroupCount();
        final int maxSFB = info.getMaxSFB();
        final int[] sfbCBl = ics.getSfbCB();
        final int[] sfbCBr = cpe.getRightChannel().getSfbCB();
        int groupOff = 0;
        int g, i, w, j, idx = 0;

        for (g = 0; g < windowGroups; g++) {
            for (i = 0; i < maxSFB; i++, idx++) {
                if (cpe.isMSUsed(idx) && sfbCBl[idx] < NOISE_HCB && sfbCBr[idx] < NOISE_HCB) {
                    for (w = 0; w < info.getWindowGroupLength(g); w++) {
                        final int off = groupOff + w * 128 + offsets[i];
                        for (j = 0; j < offsets[i + 1] - offsets[i]; j++) {
                            float t = specL[off + j] - specR[off + j];
                            specL[off + j] += specR[off + j];
                            specR[off + j] = t;
                        }
                    }
                }
            }
            groupOff += info.getWindowGroupLength(g) * 128;
        }
    }
}
