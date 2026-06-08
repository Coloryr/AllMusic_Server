package com.coloryr.allmusic.client.core.player.decoder.m4a.aac.tools;

import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.AACException;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.SampleFrequency;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.syntax.BitStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.syntax.Constants;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.syntax.ICSInfo;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.syntax.ICStream;

/**
 * Temporal Noise Shaping
 *
 * @author in-somnia
 */
public class TNS implements Constants, TNSTables {

    private static final int TNS_MAX_ORDER = 20;
    private static final int[] SHORT_BITS = {1, 4, 3}, LONG_BITS = {2, 6, 5};
    //bitstream
    private final int[] nFilt;
    private final int[][] length;
    private final int[][] order;
    private final boolean[][] direction;
    private final float[][][] coef;

    public TNS() {
        nFilt = new int[8];
        length = new int[8][4];
        direction = new boolean[8][4];
        order = new int[8][4];
        coef = new float[8][4][TNS_MAX_ORDER];
    }

    public void decode(BitStream in, ICSInfo info) throws AACException {
        final int windowCount = info.getWindowCount();
        final int[] bits = info.isEightShortFrame() ? SHORT_BITS : LONG_BITS;

        int w, i, filt, coefLen, coefRes, coefCompress, tmp;
        for (w = 0; w < windowCount; w++) {
            if ((nFilt[w] = in.readBits(bits[0])) != 0) {
                coefRes = in.readBit();

                for (filt = 0; filt < nFilt[w]; filt++) {
                    length[w][filt] = in.readBits(bits[1]);

                    if ((order[w][filt] = in.readBits(bits[2])) > 20)
                        throw new AACException("TNS filter out of range: " + order[w][filt]);
                    else if (order[w][filt] != 0) {
                        direction[w][filt] = in.readBool();
                        coefCompress = in.readBit();
                        coefLen = coefRes + 3 - coefCompress;
                        tmp = 2 * coefCompress + coefRes;

                        for (i = 0; i < order[w][filt]; i++) {
                            coef[w][filt][i] = TNS_TABLES[tmp][in.readBits(coefLen)];
                        }
                    }
                }
            }
        }
    }

    public void process(ICStream ics, float[] spec, SampleFrequency sf, boolean decode) {
        //TODO...
    }
}
