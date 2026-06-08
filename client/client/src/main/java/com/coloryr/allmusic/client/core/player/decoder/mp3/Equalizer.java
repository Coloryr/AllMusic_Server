/*
 * 11/19/04 1.0 moved to LGPL.
 * 12/12/99 Initial version. mdm@techie.com
 * -----------------------------------------------------------------------
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Library General Public License for more details.
 * You should have received a copy of the GNU Library General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * ----------------------------------------------------------------------
 */

package com.coloryr.allmusic.client.core.player.decoder.mp3;

/**
 * The <code>Equalizer</code> class can be used to specify
 * equalization settings for the MPEG audio decoder.
 * <p>
 * The equalizer consists of 32 band-pass filters.
 * Each band of the equalizer can take on a fractional value between
 * -1.0 and +1.0.
 * At -1.0, the input signal is attenuated by 6dB, at +1.0 the signal is
 * amplified by 6dB.
 *
 * @author MDM
 * @see Mp3Decoder
 */
public final class Equalizer {

    /**
     * Equalizer setting to denote that a given band will not be
     * present in the output signal.
     */
    static public final float BAND_NOT_PRESENT = Float.NEGATIVE_INFINITY;

    private static final int BANDS = 32;

    private final float[] settings = new float[BANDS];

    /**
     * Creates a new <code>Equalizer</code> instance.
     */
    public Equalizer() {
    }

    // private Equalizer(float b1, float b2, float b3, float b4, float b5,
    // float b6, float b7, float b8, float b9, float b10, float b11,
    // float b12, float b13, float b14, float b15, float b16,
    // float b17, float b18, float b19, float b20);

    public void setFrom(float[] eq) {
        reset();
        int max = Math.min(eq.length, BANDS);

        for (int i = 0; i < max; i++) {
            settings[i] = limit(eq[i]);
        }
    }

    public void setFrom(EQFunction eq) {
        reset();

        for (int i = 0; i < BANDS; i++) {
            settings[i] = limit(eq.getBand(i));
        }
    }

    /**
     * Sets the bands of this equalizer to the value the bands of
     * another equalizer. Bands that are not present in both equalizers are ignored.
     */
    public void setFrom(Equalizer eq) {
        if (eq != this) {
            setFrom(eq.settings);
        }
    }

    /**
     * Sets all bands to 0.0
     */
    public void reset() {
        for (int i = 0; i < BANDS; i++) {
            settings[i] = 0.0f;
        }
    }

    private float limit(float eq) {
        if (eq == BAND_NOT_PRESENT) return eq;
        if (eq > 1.0f) return 1.0f;
        return Math.max(eq, -1.0f);

    }

    static abstract public class EQFunction {

        /**
         * Returns the setting of a band in the equalizer.
         *
         * @param band The index of the band to retrieve the setting
         *             for.
         * @return the setting of the specified band. This is a value between
         * -1 and +1.
         */
        public float getBand(int band) {
            return 0.0f;
        }

    }

}
