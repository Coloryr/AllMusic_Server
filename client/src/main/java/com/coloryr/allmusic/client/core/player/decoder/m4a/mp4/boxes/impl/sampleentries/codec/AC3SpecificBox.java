package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.sampleentries.codec;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;

import java.io.IOException;

/**
 * This box contains parameters for AC-3 decoders. For more information see the
 * AC-3 specification "<code>ETSI TS 102 366 V1.2.1 (2008-08)</code>" at
 * <a href="http://www.etsi.org/deliver/etsi_ts/102300_102399/102366/01.02.01_60/ts_102366v010201p.pdf>
 * http://www.etsi.org/deliver/etsi_ts/102300_102399/102366/01.02.01_60/ts_102366v010201p.pdf</a>.
 *
 * @author in-somnia
 */
public class AC3SpecificBox extends CodecSpecificBox {

    private int fscod, bsid, bsmod, acmod, bitRateCode;
    private boolean lfeon;

    public AC3SpecificBox() {
        super("AC-3 Specific Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        final long l = in.readBytes(3);

        //2 bits fscod
        fscod = (int) ((l >> 22) & 0x3);
        //5 bits bsid
        bsid = (int) ((l >> 17) & 0x1F);
        //3 bits bsmod
        bsmod = (int) ((l >> 14) & 0x7);
        //3 bits acmod
        acmod = (int) ((l >> 11) & 0x7);
        //1 bit lfeon
        lfeon = ((l >> 10) & 0x1) == 1;
        //5 bits bitRateCode
        bitRateCode = (int) ((l >> 5) & 0x1F);
        //5 bits reserved
    }

    /**
     * This field has the same meaning and is set to the same value as the fscod
     * field in the AC-3 bitstream.
     *
     * @return the value of the 'fscod' field
     */
    public int getFscod() {
        return fscod;
    }

    /**
     * This field has the same meaning and is set to the same value as the bsid
     * field in the AC-3 bitstream.
     *
     * @return the value of the 'bsid' field
     */
    public int getBsid() {
        return bsid;
    }

    /**
     * This field has the same meaning and is set to the same value as the bsmod
     * field in the AC-3 bitstream.
     *
     * @return the value of the 'acmod' field
     */
    public int getBsmod() {
        return bsmod;
    }

    /**
     * This field has the same meaning and is set to the same value as the acmod
     * field in the AC-3 bitstream.
     *
     * @return the value of the 'acmod' field
     */
    public int getAcmod() {
        return acmod;
    }

    /**
     * This field has the same meaning and is set to the same value as the lfeon
     * field in the AC-3 bitstream.
     *
     * @return the value of the 'lfeon' field
     */
    public boolean isLfeon() {
        return lfeon;
    }

    /**
     * This field indicates the data rate of the AC-3 bitstream in kbit/s, as
     * shown in the following table:
     * <table>
     * <tr><th>bit rate code</th><th>bit rate (kbit/s)</th></tr>
     * <tr><td>0</td><td>32</td></tr>
     * <tr><td>1</td><td>40</td></tr>
     * <tr><td>2</td><td>48</td></tr>
     * <tr><td>3</td><td>56</td></tr>
     * <tr><td>4</td><td>64</td></tr>
     * <tr><td>5</td><td>80</td></tr>
     * <tr><td>6</td><td>96</td></tr>
     * <tr><td>7</td><td>112</td></tr>
     * <tr><td>8</td><td>128</td></tr>
     * <tr><td>9</td><td>160</td></tr>
     * <tr><td>10</td><td>192</td></tr>
     * <tr><td>11</td><td>224</td></tr>
     * <tr><td>12</td><td>256</td></tr>
     * <tr><td>13</td><td>320</td></tr>
     * <tr><td>14</td><td>384</td></tr>
     * <tr><td>15</td><td>448</td></tr>
     * <tr><td>16</td><td>512</td></tr>
     * <tr><td>17</td><td>576</td></tr>
     * <tr><td>18</td><td>640</td></tr>
     * </table>
     *
     * @return the bit rate code
     */
    public int getBitRateCode() {
        return bitRateCode;
    }
}
