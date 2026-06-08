package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.sampleentries.codec;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;

import java.io.IOException;

//defined in ISO 14496-15 as 'AVC Configuration Record'
public class AVCSpecificBox extends CodecSpecificBox {

    private int configurationVersion, profile, level, lengthSize;
    private byte profileCompatibility;
    private byte[][] sequenceParameterSetNALUnit, pictureParameterSetNALUnit;

    public AVCSpecificBox() {
        super("AVC Specific Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        configurationVersion = in.read();
        profile = in.read();
        profileCompatibility = (byte) in.read();
        level = in.read();
        //6 bits reserved, 2 bits 'length size minus one'
        lengthSize = (in.read() & 3) + 1;

        int len;
        //3 bits reserved, 5 bits number of sequence parameter sets
        final int sequenceParameterSets = in.read() & 31;

        sequenceParameterSetNALUnit = new byte[sequenceParameterSets][];
        for (int i = 0; i < sequenceParameterSets; i++) {
            len = (int) in.readBytes(2);
            sequenceParameterSetNALUnit[i] = new byte[len];
            in.readBytes(sequenceParameterSetNALUnit[i]);
        }

        final int pictureParameterSets = in.read();

        pictureParameterSetNALUnit = new byte[pictureParameterSets][];
        for (int i = 0; i < pictureParameterSets; i++) {
            len = (int) in.readBytes(2);
            pictureParameterSetNALUnit[i] = new byte[len];
            in.readBytes(pictureParameterSetNALUnit[i]);
        }
    }

    public int getConfigurationVersion() {
        return configurationVersion;
    }

    /**
     * The AVC profile code as defined in ISO/IEC 14496-10.
     *
     * @return the AVC profile
     */
    public int getProfile() {
        return profile;
    }

    /**
     * The profileCompatibility is a byte defined exactly the same as the byte
     * which occurs between the profileIDC and levelIDC in a sequence parameter
     * set (SPS), as defined in ISO/IEC 14496-10.
     *
     * @return the profile compatibility byte
     */
    public byte getProfileCompatibility() {
        return profileCompatibility;
    }

    public int getLevel() {
        return level;
    }

    /**
     * The length in bytes of the NALUnitLength field in an AVC video sample or
     * AVC parameter set sample of the associated stream. The value of this
     * field 1, 2, or 4 bytes.
     *
     * @return the NALUnitLength length in bytes
     */
    public int getLengthSize() {
        return lengthSize;
    }

    /**
     * The SPS NAL units, as specified in ISO/IEC 14496-10. SPSs shall occur in
     * order of ascending parameter set identifier with gaps being allowed.
     *
     * @return all SPS NAL units
     */
    public byte[][] getSequenceParameterSetNALUnits() {
        return sequenceParameterSetNALUnit;
    }

    /**
     * The PPS NAL units, as specified in ISO/IEC 14496-10. PPSs shall occur in
     * order of ascending parameter set identifier with gaps being allowed.
     *
     * @return all PPS NAL units
     */
    public byte[][] getPictureParameterSetNALUnits() {
        return pictureParameterSetNALUnit;
    }
}
