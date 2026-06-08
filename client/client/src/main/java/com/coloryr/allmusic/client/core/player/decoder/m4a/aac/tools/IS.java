package com.coloryr.allmusic.client.core.player.decoder.m4a.aac.tools;

import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.huffman.HCB;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.syntax.CPE;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.syntax.Constants;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.syntax.ICSInfo;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.syntax.ICStream;

/**
 * Intensity stereo
 *
 * @author in-somnia
 */
public final class IS implements Constants, ISScaleTable, HCB {

    private IS() {
    }

    public static void process(CPE cpe, float[] specL, float[] specR) {
        final ICStream ics = cpe.getRightChannel();
        final ICSInfo info = ics.getInfo();
        final int[] offsets = info.getSWBOffsets();
        final int windowGroups = info.getWindowGroupCount();
        final int maxSFB = info.getMaxSFB();
        final int[] sfbCB = ics.getSfbCB();
        final int[] sectEnd = ics.getSectEnd();
        final float[] scaleFactors = ics.getScaleFactors();

        int w, i, j, c, end, off;
        int idx = 0, groupOff = 0;
        float scale;
        for (int g = 0; g < windowGroups; g++) {
            for (i = 0; i < maxSFB; ) {
                if (sfbCB[idx] == INTENSITY_HCB || sfbCB[idx] == INTENSITY_HCB2) {
                    end = sectEnd[idx];
                    for (; i < end; i++, idx++) {
                        c = sfbCB[idx] == INTENSITY_HCB ? 1 : -1;
                        if (cpe.isMSMaskPresent())
                            c *= cpe.isMSUsed(idx) ? -1 : 1;
                        scale = c * scaleFactors[idx];
                        for (w = 0; w < info.getWindowGroupLength(g); w++) {
                            off = groupOff + w * 128 + offsets[i];
                            for (j = 0; j < offsets[i + 1] - offsets[i]; j++) {
                                specR[off + j] = specL[off + j] * scale;
                            }
                        }
                    }
                } else {
                    end = sectEnd[idx];
                    idx += end - i;
                    i = end;
                }
            }
            groupOff += info.getWindowGroupLength(g) * 128;
        }
    }
}
