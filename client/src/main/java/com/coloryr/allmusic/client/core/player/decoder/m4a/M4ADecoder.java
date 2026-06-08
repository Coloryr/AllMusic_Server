package com.coloryr.allmusic.client.core.player.decoder.m4a;

import com.coloryr.allmusic.client.core.player.decoder.BuffPack;
import com.coloryr.allmusic.client.core.player.decoder.IDecoder;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.Decoder;
import com.coloryr.allmusic.client.core.player.decoder.m4a.aac.SampleBuffer;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4Container;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api.AudioTrack;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api.Frame;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api.Track;

import java.io.IOException;
import java.io.InputStream;

public class M4ADecoder implements IDecoder {

    private final MP4Container container;
    private Decoder decoder;
    private AudioTrack track;

    public M4ADecoder(InputStream stream) throws IOException {
        container = new MP4Container(stream);
        for (Track track : container.getMovie().getTracks()) {
            if (track instanceof AudioTrack) {
                this.track = (AudioTrack) track;
                decoder = new Decoder(track.getDecoderSpecificInfo());
                break;
            }
        }
    }

    @Override
    public BuffPack decodeFrame() throws Exception {
        Frame frame = track.readNextFrame();
        if (frame == null) {
            return null;
        }
        SampleBuffer buffer = new SampleBuffer();
        decoder.decodeFrame(frame.getData(), buffer);
        BuffPack pack = new BuffPack();
        pack.len = buffer.getData().length;
        pack.buff = buffer.getData();
        if (buffer.isBigEndian()) {
            for (int i = 0; i < pack.len; i += 2) {
                if (i + 1 < pack.len) {
                    byte temp = pack.buff[i + 1];
                    pack.buff[i + 1] = pack.buff[i];
                    pack.buff[i] = temp;
                }
            }
        }
        return pack;
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public boolean set() throws Exception {
        return decoder != null;
    }

    @Override
    public int getOutputFrequency() {
        return track.getSampleRate();
    }

    @Override
    public int getOutputChannels() {
        return track.getChannelCount();
    }

    @Override
    public void set(int time) {
        if (track != null) {
            track.seek(time / 1000d);
        }
    }
}
