package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api.codec;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api.DecoderInfo;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.sampleentries.codec.CodecSpecificBox;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.sampleentries.codec.EAC3SpecificBox;

import java.util.ArrayList;
import java.util.List;

public class EAC3DecoderInfo extends DecoderInfo {

    private EAC3SpecificBox box;
    private IndependentSubstream[] is;

    public EAC3DecoderInfo(CodecSpecificBox box) {
        this.box = (EAC3SpecificBox) box;
        is = new IndependentSubstream[this.box.getIndependentSubstreamCount()];
        for (int i = 0; i < is.length; i++) {
            is[i] = new IndependentSubstream(i);
        }
    }

    public int getDataRate() {
        return box.getDataRate();
    }

    public IndependentSubstream[] getIndependentSubstreams() {
        return is;
    }

    public enum DependentSubstream {

        LC_RC_PAIR,
        LRS_RRS_PAIR,
        CS,
        TS,
        LSD_RSD_PAIR,
        LW_RW_PAIR,
        LVH_RVH_PAIR,
        CVH,
        LFE2
    }

    public class IndependentSubstream {

        private final int index;
        private final DependentSubstream[] dependentSubstreams;

        private IndependentSubstream(int index) {
            this.index = index;

            final int loc = box.getDependentSubstreamLocation()[index];
            final List<DependentSubstream> list = new ArrayList<DependentSubstream>();
            for (int i = 0; i < 9; i++) {
                if (((loc >> (8 - i)) & 1) == 1) list.add(DependentSubstream.values()[i]);
            }
            dependentSubstreams = list.toArray(new DependentSubstream[list.size()]);
        }

        public int getFscod() {
            return box.getFscods()[index];
        }

        public int getBsid() {
            return box.getBsids()[index];
        }

        public int getBsmod() {
            return box.getBsmods()[index];
        }

        public int getAcmod() {
            return box.getAcmods()[index];
        }

        public boolean isLfeon() {
            return box.getLfeons()[index];
        }

        public DependentSubstream[] getDependentSubstreams() {
            return dependentSubstreams;
        }
    }
}
