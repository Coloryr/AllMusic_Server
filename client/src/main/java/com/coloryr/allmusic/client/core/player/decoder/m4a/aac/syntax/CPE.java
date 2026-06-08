package com.coloryr.allmusic.client.core.player.decoder.m4a.aac.syntax;

import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.AACException;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.DecoderConfig;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.Profile;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.SampleFrequency;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.tools.MSMask;

import java.util.Arrays;

public class CPE extends Element implements Constants {

    private final boolean[] msUsed;
    ICStream icsL, icsR;
    private MSMask msMask;
    private boolean commonWindow;

    CPE(int frameLength) {
        super();
        msUsed = new boolean[MAX_MS_MASK];
        icsL = new ICStream(frameLength);
        icsR = new ICStream(frameLength);
    }

    void decode(BitStream in, DecoderConfig conf) throws AACException {
        final Profile profile = conf.getProfile();
        final SampleFrequency sf = conf.getSampleFrequency();
        if (sf.equals(SampleFrequency.SAMPLE_FREQUENCY_NONE)) throw new AACException("invalid sample frequency");

        readElementInstanceTag(in);

        commonWindow = in.readBool();
        final ICSInfo info = icsL.getInfo();
        if (commonWindow) {
            info.decode(in, conf, commonWindow);
            icsR.getInfo().setData(info);

            msMask = MSMask.forInt(in.readBits(2));
            if (msMask.equals(MSMask.TYPE_USED)) {
                final int maxSFB = info.getMaxSFB();
                final int windowGroupCount = info.getWindowGroupCount();

                for (int idx = 0; idx < windowGroupCount * maxSFB; idx++) {
                    msUsed[idx] = in.readBool();
                }
            } else if (msMask.equals(MSMask.TYPE_ALL_1)) Arrays.fill(msUsed, true);
            else if (msMask.equals(MSMask.TYPE_ALL_0)) Arrays.fill(msUsed, false);
            else throw new AACException("reserved MS mask type used");
        } else {
            msMask = MSMask.TYPE_ALL_0;
            Arrays.fill(msUsed, false);
        }

        if (profile.isErrorResilientProfile() && (info.isLTPrediction1Present())) {
            if (info.ltpData2Present = in.readBool()) info.getLTPrediction2().decode(in, info, profile);
        }

        icsL.decode(in, commonWindow, conf);
        icsR.decode(in, commonWindow, conf);
    }

    public ICStream getLeftChannel() {
        return icsL;
    }

    public ICStream getRightChannel() {
        return icsR;
    }

    public MSMask getMSMask() {
        return msMask;
    }

    public boolean isMSUsed(int off) {
        return msUsed[off];
    }

    public boolean isMSMaskPresent() {
        return !msMask.equals(MSMask.TYPE_ALL_0);
    }

    public boolean isCommonWindow() {
        return commonWindow;
    }
}
