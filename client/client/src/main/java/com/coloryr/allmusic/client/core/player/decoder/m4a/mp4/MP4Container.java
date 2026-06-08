package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api.Brand;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api.Movie;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.Box;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxFactory;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxTypes;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.FileTypeBox;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.ProgressiveDownloadInformationBox;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The MP4Container is the central class for the MP4 demultiplexer. It reads the
 * container and gives access to the containing data.
 * <p>
 * The data source can be either an <code>InputStream</code> or a
 * <code>RandomAccessFile</code>. Since the specification does not decree a
 * specific order of the content, the data needed for parsing (the sample
 * tables) may be at the end of the stream. In this case, random access is
 * needed and reading from an <code>InputSteam</code> will cause an exception.
 * Thus, whenever possible, a <code>RandomAccessFile</code> should be used for
 * local files. Parsing from an <code>InputStream</code> is useful when reading
 * from a network stream.
 * <p>
 * Each <code>MP4Container</code> can return the used file brand (file format
 * version). Optionally, the following data may be present:
 * <ul>
 * <li>progressive download informations: pairs of download rate and playback
 * delay, see {@link #getDownloadInformationPairs() getDownloadInformationPairs()}</li>
 * <li>a <code>Movie</code></li>
 * </ul>
 * <p>
 * Additionally it gives access to the underlying MP4 boxes, that can be
 * retrieved by <code>getBoxes()</code>. However, it is not recommended to
 * access the boxes directly.
 *
 * @author in-somnia
 */
public class MP4Container {

    static {
        Logger log = Logger.getLogger("MP4 API");
        for (Handler h : log.getHandlers()) {
            log.removeHandler(h);
        }
        log.setLevel(Level.WARNING);

        final ConsoleHandler h = new ConsoleHandler();
        h.setLevel(Level.ALL);
        log.addHandler(h);
    }

    private final MP4InputStream in;
    private final List<Box> boxes;
    private Brand major, minor;
    private Brand[] compatible;
    private FileTypeBox ftyp;
    private ProgressiveDownloadInformationBox pdin;
    private Box moov;
    private Movie movie;

    public MP4Container(InputStream in) throws IOException {
        this.in = new MP4InputStream(in);
        boxes = new ArrayList<Box>();

        readContent();
    }

    public MP4Container(RandomAccessFile in) throws IOException {
        this.in = new MP4InputStream(in);
        boxes = new ArrayList<Box>();

        readContent();
    }

    private void readContent() throws IOException {
        //read all boxes
        Box box = null;
        long type;
        boolean moovFound = false;
        while (in.hasLeft()) {
            box = BoxFactory.parseBox(null, in);
            if (boxes.isEmpty() && box.getType() != BoxTypes.FILE_TYPE_BOX)
                throw new MP4Exception("no MP4 signature found");
            boxes.add(box);

            type = box.getType();
            if (type == BoxTypes.FILE_TYPE_BOX) {
                if (ftyp == null) ftyp = (FileTypeBox) box;
            } else if (type == BoxTypes.MOVIE_BOX) {
                if (movie == null) moov = box;
                moovFound = true;
            } else if (type == BoxTypes.PROGRESSIVE_DOWNLOAD_INFORMATION_BOX) {
                if (pdin == null) pdin = (ProgressiveDownloadInformationBox) box;
            } else if (type == BoxTypes.MEDIA_DATA_BOX) {
                if (moovFound) break;
                else if (!in.hasRandomAccess()) throw new MP4Exception("movie box at end of file, need random access");
            }
        }
    }

    public Brand getMajorBrand() {
        if (major == null) major = Brand.forID(ftyp.getMajorBrand());
        return major;
    }

    public Brand getMinorBrand() {
        if (minor == null) minor = Brand.forID(ftyp.getMajorBrand());
        return minor;
    }

    public Brand[] getCompatibleBrands() {
        if (compatible == null) {
            final String[] s = ftyp.getCompatibleBrands();
            compatible = new Brand[s.length];
            for (int i = 0; i < s.length; i++) {
                compatible[i] = Brand.forID(s[i]);
            }
        }
        return compatible;
    }

    //TODO: pdin, movie fragments??
    public Movie getMovie() {
        if (moov == null) return null;
        else if (movie == null) movie = new Movie(moov, in);
        return movie;
    }

    public List<Box> getBoxes() {
        return Collections.unmodifiableList(boxes);
    }
}
