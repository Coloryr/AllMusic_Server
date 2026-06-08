package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api.Track.Codec;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api.drm.ITunesProtection;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.Box;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxTypes;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.OriginalFormatBox;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.SchemeTypeBox;

/**
 * This class contains information about a DRM system.
 */
public abstract class Protection {

    private final Codec originalFormat;

    protected Protection(Box sinf) {
        //original format
        final long type = ((OriginalFormatBox) sinf.getChild(BoxTypes.ORIGINAL_FORMAT_BOX)).getOriginalFormat();
        Codec c;
        //TODO: currently it tests for audio and video codec, can do this any other way?
        if (!(c = AudioTrack.AudioCodec.forType(type)).equals(AudioTrack.AudioCodec.UNKNOWN_AUDIO_CODEC))
            originalFormat = c;
        else if (!(c = VideoTrack.VideoCodec.forType(type)).equals(VideoTrack.VideoCodec.UNKNOWN_VIDEO_CODEC))
            originalFormat = c;
        else originalFormat = null;
    }

    static Protection parse(Box sinf) {
        Protection p = null;
        if (sinf.hasChild(BoxTypes.SCHEME_TYPE_BOX)) {
            final SchemeTypeBox schm = (SchemeTypeBox) sinf.getChild(BoxTypes.SCHEME_TYPE_BOX);
            final long l = schm.getSchemeType();
            if (l == Scheme.ITUNES_FAIR_PLAY.type) p = new ITunesProtection(sinf);
        }

        if (p == null) p = new UnknownProtection(sinf);
        return p;
    }

    Codec getOriginalFormat() {
        return originalFormat;
    }

    public abstract Scheme getScheme();

    public static enum Scheme {

        ITUNES_FAIR_PLAY(1769239918),
        UNKNOWN(-1);
        private long type;

        private Scheme(long type) {
            this.type = type;
        }
    }

    //default implementation for unknown protection schemes
    private static class UnknownProtection extends Protection {

        UnknownProtection(Box sinf) {
            super(sinf);
        }

        @Override
        public Scheme getScheme() {
            return Scheme.UNKNOWN;
        }
    }
}
