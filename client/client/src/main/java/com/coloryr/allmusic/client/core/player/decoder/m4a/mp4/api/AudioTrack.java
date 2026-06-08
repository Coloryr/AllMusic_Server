package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.Box;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxTypes;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.ESDBox;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.SampleDescriptionBox;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.SoundMediaHeaderBox;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.sampleentries.AudioSampleEntry;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.sampleentries.codec.CodecSpecificBox;

public class AudioTrack extends Track {

    private final SoundMediaHeaderBox smhd;
    private final AudioSampleEntry sampleEntry;
    private Codec codec;

    public AudioTrack(Box trak, MP4InputStream in) {
        super(trak, in);

        final Box mdia = trak.getChild(BoxTypes.MEDIA_BOX);
        final Box minf = mdia.getChild(BoxTypes.MEDIA_INFORMATION_BOX);
        smhd = (SoundMediaHeaderBox) minf.getChild(BoxTypes.SOUND_MEDIA_HEADER_BOX);

        final Box stbl = minf.getChild(BoxTypes.SAMPLE_TABLE_BOX);

        //sample descriptions: 'mp4a' and 'enca' have an ESDBox, all others have a CodecSpecificBox
        final SampleDescriptionBox stsd = (SampleDescriptionBox) stbl.getChild(BoxTypes.SAMPLE_DESCRIPTION_BOX);
        if (stsd.getChildren().get(0) instanceof AudioSampleEntry) {
            sampleEntry = (AudioSampleEntry) stsd.getChildren().get(0);
            final long type = sampleEntry.getType();
            if (sampleEntry.hasChild(BoxTypes.ESD_BOX))
                findDecoderSpecificInfo((ESDBox) sampleEntry.getChild(BoxTypes.ESD_BOX));
            else decoderInfo = DecoderInfo.parse((CodecSpecificBox) sampleEntry.getChildren().get(0));

            if (type == BoxTypes.ENCRYPTED_AUDIO_SAMPLE_ENTRY || type == BoxTypes.DRMS_SAMPLE_ENTRY) {
                findDecoderSpecificInfo((ESDBox) sampleEntry.getChild(BoxTypes.ESD_BOX));
                protection = Protection.parse(sampleEntry.getChild(BoxTypes.PROTECTION_SCHEME_INFORMATION_BOX));
                codec = protection.getOriginalFormat();
            } else codec = AudioCodec.forType(sampleEntry.getType());
        } else {
            sampleEntry = null;
            codec = AudioCodec.UNKNOWN_AUDIO_CODEC;
        }
    }

    @Override
    public Type getType() {
        return Type.AUDIO;
    }

    @Override
    public Codec getCodec() {
        return codec;
    }

    /**
     * The balance is a floating-point number that places mono audio tracks in a
     * stereo space: 0 is centre (the normal value), full left is -1.0 and full
     * right is 1.0.
     *
     * @return the stereo balance for a this track
     */
    public double getBalance() {
        return smhd.getBalance();
    }

    /**
     * Returns the number of channels in this audio track.
     *
     * @return the number of channels
     */
    public int getChannelCount() {
        return sampleEntry.getChannelCount();
    }

    /**
     * Returns the sample rate of this audio track.
     *
     * @return the sample rate
     */
    public int getSampleRate() {
        return sampleEntry.getSampleRate();
    }

    /**
     * Returns the sample size in bits for this track.
     *
     * @return the sample size
     */
    public int getSampleSize() {
        return sampleEntry.getSampleSize();
    }

    public double getVolume() {
        return tkhd.getVolume();
    }

    public enum AudioCodec implements Codec {

        AAC,
        AC3,
        AMR,
        AMR_WIDE_BAND,
        EVRC,
        EXTENDED_AC3,
        QCELP,
        SMV,
        UNKNOWN_AUDIO_CODEC;

        static Codec forType(long type) {
            final Codec ac;
            if (type == BoxTypes.MP4A_SAMPLE_ENTRY) ac = AAC;
            else if (type == BoxTypes.AC3_SAMPLE_ENTRY) ac = AC3;
            else if (type == BoxTypes.AMR_SAMPLE_ENTRY) ac = AMR;
            else if (type == BoxTypes.AMR_WB_SAMPLE_ENTRY) ac = AMR_WIDE_BAND;
            else if (type == BoxTypes.EVRC_SAMPLE_ENTRY) ac = EVRC;
            else if (type == BoxTypes.EAC3_SAMPLE_ENTRY) ac = EXTENDED_AC3;
            else if (type == BoxTypes.QCELP_SAMPLE_ENTRY) ac = QCELP;
            else if (type == BoxTypes.SMV_SAMPLE_ENTRY) ac = SMV;
            else ac = UNKNOWN_AUDIO_CODEC;
            return ac;
        }
    }
}
