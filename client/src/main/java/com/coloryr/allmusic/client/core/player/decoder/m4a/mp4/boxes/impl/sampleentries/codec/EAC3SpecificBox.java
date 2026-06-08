package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.sampleentries.codec;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;

import java.io.IOException;

/**
 * This box contains parameters for Extended AC-3 decoders. For more information
 * see the AC-3 specification "<code>ETSI TS 102 366 V1.2.1 (2008-08)</code>" at
 * <a href="http://www.etsi.org/deliver/etsi_ts/102300_102399/102366/01.02.01_60/ts_102366v010201p.pdf>
 * http://www.etsi.org/deliver/etsi_ts/102300_102399/102366/01.02.01_60/ts_102366v010201p.pdf</a>.
 *
 * @author in-somnia
 */
public class EAC3SpecificBox extends CodecSpecificBox {

    private int dataRate, independentSubstreamCount;
    private int[] fscods, bsids, bsmods, acmods, dependentSubstreamCount, dependentSubstreamLocation;
    private boolean[] lfeons;

    public EAC3SpecificBox() {
        super("EAC-3 Specific Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        long l = in.readBytes(2);
        //13 bits dataRate
        dataRate = (int) ((l >> 3) & 0x1FFF);
        //3 bits number of independent substreams
        independentSubstreamCount = (int) (l & 0x7);

        for (int i = 0; i < independentSubstreamCount; i++) {
            l = in.readBytes(3);
            //2 bits fscod
            fscods[i] = (int) ((l >> 22) & 0x3);
            //5 bits bsid
            bsids[i] = (int) ((l >> 17) & 0x1F);
            //5 bits bsmod
            bsmods[i] = (int) ((l >> 12) & 0x1F);
            //3 bits acmod
            acmods[i] = (int) ((l >> 9) & 0x7);
            //3 bits reserved
            //1 bit lfeon
            lfeons[i] = ((l >> 5) & 0x1) == 1;
            //4 bits number of dependent substreams
            dependentSubstreamCount[i] = (int) ((l >> 1) & 0xF);
            if (dependentSubstreamCount[i] > 0) {
                //9 bits dependent substream location
                l = (l << 8) | in.read();
                dependentSubstreamLocation[i] = (int) (l & 0x1FF);
            }
            //else: 1 bit reserved
        }
    }

    /**
     * This value indicates the data rate of the Enhanced AC-3 bitstream in
     * kbit/s. If the Enhanced AC-3 stream is variable bit rate, then this value
     * indicates the maximum data rate of the stream.
     *
     * @return the data rate
     */
    public int getDataRate() {
        return dataRate;
    }

    /**
     * This field indicates the number of independent substreams that are
     * present in the Enhanced AC-3 bitstream.
     *
     * @return the number of independent substreams
     */
    public int getIndependentSubstreamCount() {
        return independentSubstreamCount;
    }

    /**
     * This field has the same meaning and is set to the same value as the fscod
     * field in the independent substream.
     *
     * @return the 'fscod' values for all independent substreams
     */
    public int[] getFscods() {
        return fscods;
    }

    /**
     * This field has the same meaning and is set to the same value as the bsid
     * field in the independent substream.
     *
     * @return the 'bsid' values for all independent substreams
     */
    public int[] getBsids() {
        return bsids;
    }

    /**
     * This field has the same meaning and is set to the same value as the bsmod
     * field in the independent substream. If the bsmod field is not present in
     * the independent substream, this field shall be set to 0.
     *
     * @return the 'bsmod' values for all independent substreams
     */
    public int[] getBsmods() {
        return bsmods;
    }

    /**
     * This field has the same meaning and is set to the same value as the acmod
     * field in the independent substream.
     *
     * @return the 'acmod' values for all independent substreams
     */
    public int[] getAcmods() {
        return acmods;
    }

    /**
     * This field has the same meaning and is set to the same value as the lfeon
     * field in the independent substream.
     *
     * @return the 'lfeon' values for all independent substreams
     */
    public boolean[] getLfeons() {
        return lfeons;
    }

    /**
     * This field indicates the number of dependent substreams that are
     * associated with an independent substream.
     *
     * @return the number of dependent substreams for all independent substreams
     */
    public int[] getDependentSubstreamCount() {
        return dependentSubstreamCount;
    }

    /**
     * If there are one or more dependent substreams associated with an
     * independent substream, this bit field is used to identify channel
     * locations beyond those identified using the 'acmod' field that are
     * present in the bitstream. The lowest 9 bits of the returned integer are
     * flags indicating if a channel location is present. The flags are used
     * according to the following table, where index 0 is the most significant
     * bit of all 9 used bits:
     * <table>
     * <tr><th>Bit</th><th>Location</th></tr>
     * <tr><td>0</td><td>Lc/Rc pair</td></tr>
     * <tr><td>1</td><td>Lrs/Rrs pair</td></tr>
     * <tr><td>2</td><td>Cs</td></tr>
     * <tr><td>3</td><td>Ts</td></tr>
     * <tr><td>4</td><td>Lsd/Rsd pair</td></tr>
     * <tr><td>5</td><td>Lw/Rw pair</td></tr>
     * <tr><td>6</td><td>Lvh/Rvh pair </td></tr>
     * <tr><td>7</td><td>Cvh</td></tr>
     * <tr><td>8</td><td>LFE2</td></tr>
     * </table>
     *
     * @return the dependent substream locations for all independent substreams
     */
    public int[] getDependentSubstreamLocation() {
        return dependentSubstreamLocation;
    }
}
