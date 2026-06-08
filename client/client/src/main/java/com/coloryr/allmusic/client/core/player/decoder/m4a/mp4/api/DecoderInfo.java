package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api.codec.*;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxTypes;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.sampleentries.codec.CodecSpecificBox;

/**
 * The <code>DecoderInfo</code> object contains the neccessary data to
 * initialize a decoder. A track either contains a <code>DecoderInfo</code> or a
 * byte-Array called the 'DecoderSpecificInfo', which is e.g. used for AAC.
 * <p>
 * The <code>DecoderInfo</code> object received from a track is a subclass of
 * this class depending on the <code>Codec</code>.
 *
 * <code>
 * AudioTrack track = (AudioTrack) movie.getTrack(AudioCodec.AC3);
 * AC3DecoderInfo info = (AC3DecoderInfo) track.getDecoderInfo();
 * </code>
 *
 * @author in-somnia
 */
public abstract class DecoderInfo {

    static DecoderInfo parse(CodecSpecificBox css) {
        final long l = css.getType();

        final DecoderInfo info;
        if (l == BoxTypes.H263_SPECIFIC_BOX) info = new H263DecoderInfo(css);
        else if (l == BoxTypes.AMR_SPECIFIC_BOX) info = new AMRDecoderInfo(css);
        else if (l == BoxTypes.EVRC_SPECIFIC_BOX) info = new EVRCDecoderInfo(css);
        else if (l == BoxTypes.QCELP_SPECIFIC_BOX) info = new QCELPDecoderInfo(css);
        else if (l == BoxTypes.SMV_SPECIFIC_BOX) info = new SMVDecoderInfo(css);
        else if (l == BoxTypes.AVC_SPECIFIC_BOX) info = new AVCDecoderInfo(css);
        else if (l == BoxTypes.AC3_SPECIFIC_BOX) info = new AC3DecoderInfo(css);
        else if (l == BoxTypes.EAC3_SPECIFIC_BOX) info = new EAC3DecoderInfo(css);
        else info = new UnknownDecoderInfo();
        return info;
    }

    private static class UnknownDecoderInfo extends DecoderInfo {
    }
}
