package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.Box;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxTypes;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.*;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.od.DecoderSpecificInfo;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.od.Descriptor;

import java.io.EOFException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents a track in a movie.
 * <p>
 * Each track contains either a decoder specific info as a byte array or a
 * <code>DecoderInfo</code> object that contains necessary information for the
 * decoder.
 *
 * @author in-somnia
 */
//TODO: expand javadoc; use generics for subclasses?
public abstract class Track {

    protected final TrackHeaderBox tkhd;
    private final MP4InputStream in;
    private final MediaHeaderBox mdhd;
    private final boolean inFile;
    private final List<Frame> frames;
    //info structures
    protected DecoderSpecificInfo decoderSpecificInfo;
    protected DecoderInfo decoderInfo;
    protected Protection protection;
    private URL location;
    private int currentFrame;

    Track(Box trak, MP4InputStream in) {
        this.in = in;

        tkhd = (TrackHeaderBox) trak.getChild(BoxTypes.TRACK_HEADER_BOX);

        final Box mdia = trak.getChild(BoxTypes.MEDIA_BOX);
        mdhd = (MediaHeaderBox) mdia.getChild(BoxTypes.MEDIA_HEADER_BOX);
        final Box minf = mdia.getChild(BoxTypes.MEDIA_INFORMATION_BOX);

        final Box dinf = minf.getChild(BoxTypes.DATA_INFORMATION_BOX);
        final DataReferenceBox dref = (DataReferenceBox) dinf.getChild(BoxTypes.DATA_REFERENCE_BOX);
        //TODO: support URNs
        if (dref.hasChild(BoxTypes.DATA_ENTRY_URL_BOX)) {
            DataEntryUrlBox url = (DataEntryUrlBox) dref.getChild(BoxTypes.DATA_ENTRY_URL_BOX);
            inFile = url.isInFile();
            if (!inFile) {
                try {
                    location = new URL(url.getLocation());
                } catch (MalformedURLException e) {
                    Logger.getLogger("MP4 API").log(Level.WARNING, "Parsing URL-Box failed: {0}, url: {1}", new String[]{e.toString(), url.getLocation()});
                    location = null;
                }
            }
        }
		/*else if(dref.containsChild(BoxTypes.DATA_ENTRY_URN_BOX)) {
		DataEntryUrnBox urn = (DataEntryUrnBox) dref.getChild(BoxTypes.DATA_ENTRY_URN_BOX);
		inFile = urn.isInFile();
		location = urn.getLocation();
		}*/
        else {
            inFile = true;
            location = null;
        }

        //sample table
        final Box stbl = minf.getChild(BoxTypes.SAMPLE_TABLE_BOX);
        if (stbl.hasChildren()) {
            frames = new ArrayList<Frame>();
            parseSampleTable(stbl);
        } else frames = Collections.emptyList();
        currentFrame = 0;
    }

    private void parseSampleTable(Box stbl) {
        final double timeScale = mdhd.getTimeScale();
        final Type type = getType();

        //sample sizes
        final long[] sampleSizes = ((SampleSizeBox) stbl.getChild(BoxTypes.SAMPLE_SIZE_BOX)).getSampleSizes();

        //chunk offsets
        final ChunkOffsetBox stco;
        if (stbl.hasChild(BoxTypes.CHUNK_OFFSET_BOX)) stco = (ChunkOffsetBox) stbl.getChild(BoxTypes.CHUNK_OFFSET_BOX);
        else stco = (ChunkOffsetBox) stbl.getChild(BoxTypes.CHUNK_LARGE_OFFSET_BOX);
        final long[] chunkOffsets = stco.getChunks();

        //samples to chunks
        final SampleToChunkBox stsc = ((SampleToChunkBox) stbl.getChild(BoxTypes.SAMPLE_TO_CHUNK_BOX));
        final long[] firstChunks = stsc.getFirstChunks();
        final long[] samplesPerChunk = stsc.getSamplesPerChunk();

        //sample durations/timestamps
        final DecodingTimeToSampleBox stts = (DecodingTimeToSampleBox) stbl.getChild(BoxTypes.DECODING_TIME_TO_SAMPLE_BOX);
        final long[] sampleCounts = stts.getSampleCounts();
        final long[] sampleDeltas = stts.getSampleDeltas();
        final long[] timeOffsets = new long[sampleSizes.length];
        long tmp = 0;
        int off = 0;
        for (int i = 0; i < sampleCounts.length; i++) {
            for (int j = 0; j < sampleCounts[i]; j++) {
                timeOffsets[off + j] = tmp;
                tmp += sampleDeltas[i];
            }
            off += sampleCounts[i];
        }

        //create samples
        int current = 0;
        int lastChunk;
        double timeStamp;
        long offset = 0;
        //iterate over all chunk groups
        for (int i = 0; i < firstChunks.length; i++) {
            if (i < firstChunks.length - 1) lastChunk = (int) firstChunks[i + 1] - 1;
            else lastChunk = chunkOffsets.length;

            //iterate over all chunks in current group
            for (int j = (int) firstChunks[i] - 1; j < lastChunk; j++) {
                offset = chunkOffsets[j];

                //iterate over all samples in current chunk
                for (int k = 0; k < samplesPerChunk[i]; k++) {
                    //create samples
                    timeStamp = ((double) timeOffsets[current]) / timeScale;
                    frames.add(new Frame(type, offset, sampleSizes[current], timeStamp));
                    offset += sampleSizes[current];
                    current++;
                }
            }
        }

        //frames need not to be time-ordered: sort by timestamp
        //TODO: is it possible to add them to the specific position?
        Collections.sort(frames);
    }

    //TODO: implement other entry descriptors
    protected void findDecoderSpecificInfo(ESDBox esds) {
        final Descriptor ed = esds.getEntryDescriptor();
        final List<Descriptor> children = ed.getChildren();
        List<Descriptor> children2;

        for (Descriptor e : children) {
            children2 = e.getChildren();
            for (Descriptor e2 : children2) {
                switch (e2.getType()) {
                    case Descriptor.TYPE_DECODER_SPECIFIC_INFO:
                        decoderSpecificInfo = (DecoderSpecificInfo) e2;
                        break;
                }
            }
        }
    }

    protected <T> void parseSampleEntry(Box sampleEntry, Class<T> clazz) {
        T type;
        try {
            type = clazz.newInstance();
            if (sampleEntry.getClass().isInstance(type)) {
                System.out.println("true");
            }
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public abstract Type getType();

    public abstract Codec getCodec();

    /**
     * Returns true if the track is enabled. A disabled track is treated as if
     * it were not present.
     *
     * @return true if the track is enabled
     */
    public boolean isEnabled() {
        return tkhd.isTrackEnabled();
    }

    //tkhd

    /**
     * Returns true if the track is used in the presentation.
     *
     * @return true if the track is used
     */
    public boolean isUsed() {
        return tkhd.isTrackInMovie();
    }

    /**
     * Returns true if the track is used in previews.
     *
     * @return true if the track is used in previews
     */
    public boolean isUsedForPreview() {
        return tkhd.isTrackInPreview();
    }

    /**
     * Returns the time this track was created.
     *
     * @return the creation time
     */
    public Date getCreationTime() {
        return Utils.getDate(tkhd.getCreationTime());
    }

    /**
     * Returns the last time this track was modified.
     *
     * @return the modification time
     */
    public Date getModificationTime() {
        return Utils.getDate(tkhd.getModificationTime());
    }

    /**
     * Returns the language for this media.
     *
     * @return the language
     */
    public Locale getLanguage() {
        return new Locale(mdhd.getLanguage());
    }

    //mdhd

    /**
     * Returns true if the data for this track is present in this file (stream).
     * If not, <code>getLocation()</code> returns the URL where the data can be
     * found.
     *
     * @return true if the data is in this file (stream), false otherwise
     */
    public boolean isInFile() {
        return inFile;
    }

    /**
     * If the data for this track is not present in this file (if
     * <code>isInFile</code> returns false), this method returns the data's
     * location. Else null is returned.
     *
     * @return the data's location or null if the data is in this file
     */
    public URL getLocation() {
        return location;
    }

    /**
     * Returns the decoder specific info, if present. It contains configuration
     * data for the decoder. If the decoder specific info is not present, the
     * track contains a <code>DecoderInfo</code>.
     *
     * @return the decoder specific info
     * @see #getDecoderInfo()
     */
    public byte[] getDecoderSpecificInfo() {
        return decoderSpecificInfo.getData();
    }

    //info structures

    /**
     * Returns the <code>DecoderInfo</code>, if present. It contains
     * configuration information for the decoder. If the structure is not
     * present, the track contains a decoder specific info.
     *
     * @return the codec specific structure
     * @see #getDecoderSpecificInfo()
     */
    public DecoderInfo getDecoderInfo() {
        return decoderInfo;
    }

    /**
     * Returns the <code>ProtectionInformation</code> object that contains
     * details about the DRM system used. If no protection is present this
     * method returns null.
     *
     * @return a <code>ProtectionInformation</code> object or null if no
     * protection is used
     */
    public Protection getProtection() {
        return protection;
    }

    /**
     * Indicates if there are more frames to be read in this track.
     *
     * @return true if there is at least one more frame to read.
     */
    public boolean hasMoreFrames() {
        return currentFrame < frames.size();
    }

    //reading

    /**
     * Reads the next frame from this track. If it contains no more frames to
     * read, null is returned.
     *
     * @return the next frame or null if there are no more frames to read
     * @throws IOException if reading fails
     */
    public Frame readNextFrame() throws IOException {
        Frame frame = null;
        if (hasMoreFrames()) {
            frame = frames.get(currentFrame);

            final long diff = frame.getOffset() - in.getOffset();
            if (diff > 0) in.skipBytes(diff);
            else if (diff < 0) {
                if (in.hasRandomAccess()) in.seek(frame.getOffset());
                else {
                    Logger.getLogger("MP4 API").log(Level.WARNING, "readNextFrame failed: frame {0} already skipped, offset:{1}, stream:{2}", new Object[]{currentFrame, frame.getOffset(), in.getOffset()});
                    throw new IOException("frame already skipped and no random access");
                }
            }

            final byte[] b = new byte[(int) frame.getSize()];
            try {
                in.readBytes(b);
            } catch (EOFException e) {
                Logger.getLogger("MP4 API").log(Level.WARNING, "readNextFrame failed: tried to read {0} bytes at {1}", new Long[]{frame.getSize(), in.getOffset()});
                throw e;
            }
            frame.setData(b);
            currentFrame++;
        }
        return frame;
    }

    /**
     * This method tries to seek to the frame that is nearest to the given
     * timestamp. It returns the timestamp of the frame it seeked to or -1 if
     * none was found.
     *
     * @param timestamp a timestamp to seek to
     * @return the frame's timestamp that the method seeked to
     */
    public double seek(double timestamp) {
        //find first frame > timestamp
        Frame frame = null;
        for (int i = 0; i < frames.size(); i++) {
            frame = frames.get(i++);
            if (frame.getTime() > timestamp) {
                currentFrame = i;
                break;
            }
        }
        return (frame == null) ? -1 : frame.getTime();
    }

    /**
     * Returns the timestamp of the next frame to be read. This is needed to
     * read frames from a movie that contains multiple tracks.
     *
     * @return the next frame's timestamp
     */
    double getNextTimeStamp() {
        return frames.get(currentFrame).getTime();
    }

    public interface Codec {
        //TODO: currently only marker interface
    }
}
