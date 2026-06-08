package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.Box;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxTypes;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.CopyrightBox;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.meta.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains the metadata for a movie. It parses different metadata
 * types (iTunes tags, ID3).
 * The fields can be read via the <code>get(Field)</code> method using one of
 * the predefined <code>Field</code>s.
 *
 * @author in-somnia
 */
public class MetaData {

    private static final String[] STANDARD_GENRES = {
            "undefined",
            //IDv1 standard
            "blues",
            "classic rock",
            "country",
            "dance",
            "disco",
            "funk",
            "grunge",
            "hip hop",
            "jazz",
            "metal",
            "new age",
            "oldies",
            "other",
            "pop",
            "r and b",
            "rap",
            "reggae",
            "rock",
            "techno",
            "industrial",
            "alternative",
            "ska",
            "death metal",
            "pranks",
            "soundtrack",
            "euro techno",
            "ambient",
            "trip hop",
            "vocal",
            "jazz funk",
            "fusion",
            "trance",
            "classical",
            "instrumental",
            "acid",
            "house",
            "game",
            "sound clip",
            "gospel",
            "noise",
            "alternrock",
            "bass",
            "soul",
            "punk",
            "space",
            "meditative",
            "instrumental pop",
            "instrumental rock",
            "ethnic",
            "gothic",
            "darkwave",
            "techno industrial",
            "electronic",
            "pop folk",
            "eurodance",
            "dream",
            "southern rock",
            "comedy",
            "cult",
            "gangsta",
            "top ",
            "christian rap",
            "pop funk",
            "jungle",
            "native american",
            "cabaret",
            "new wave",
            "psychedelic",
            "rave",
            "showtunes",
            "trailer",
            "lo fi",
            "tribal",
            "acid punk",
            "acid jazz",
            "polka",
            "retro",
            "musical",
            "rock and roll",
            //winamp extension
            "hard rock",
            "folk",
            "folk rock",
            "national folk",
            "swing",
            "fast fusion",
            "bebob",
            "latin",
            "revival",
            "celtic",
            "bluegrass",
            "avantgarde",
            "gothic rock",
            "progressive rock",
            "psychedelic rock",
            "symphonic rock",
            "slow rock",
            "big band",
            "chorus",
            "easy listening",
            "acoustic",
            "humour",
            "speech",
            "chanson",
            "opera",
            "chamber music",
            "sonata",
            "symphony",
            "booty bass",
            "primus",
            "porn groove",
            "satire",
            "slow jam",
            "club",
            "tango",
            "samba",
            "folklore",
            "ballad",
            "power ballad",
            "rhythmic soul",
            "freestyle",
            "duet",
            "punk rock",
            "drum solo",
            "a capella",
            "euro house",
            "dance hall"
    };
    private static final String[] NERO_TAGS = {
            "artist", "title", "album", "track", "totaltracks", "year", "genre",
            "disc", "totaldiscs", "url", "copyright", "comment", "lyrics",
            "credits", "rating", "label", "composer", "isrc", "mood", "tempo"
    };
    private Map<Field<?>, Object> contents;

    MetaData() {
        contents = new HashMap<Field<?>, Object>();
    }

    /*moov.udta:
     * -3gpp boxes
     * -meta
     * --ilst
     * --tags
     * --meta (no container!)
     * --tseg
     * ---tshd
     */
    void parse(Box udta, Box meta) {
        //standard boxes
        if (meta.hasChild(BoxTypes.COPYRIGHT_BOX)) {
            CopyrightBox cprt = (CopyrightBox) meta.getChild(BoxTypes.COPYRIGHT_BOX);
            put(Field.LANGUAGE, new Locale(cprt.getLanguageCode()));
            put(Field.COPYRIGHT, cprt.getNotice());
        }
        //3gpp user data
        if (udta != null) parse3GPPData(udta);
        //id3, TODO: can be present in different languages
        if (meta.hasChild(BoxTypes.ID3_TAG_BOX)) parseID3((ID3TagBox) meta.getChild(BoxTypes.ID3_TAG_BOX));
        //itunes
        if (meta.hasChild(BoxTypes.ITUNES_META_LIST_BOX))
            parseITunesMetaData(meta.getChild(BoxTypes.ITUNES_META_LIST_BOX));
        //nero tags
        if (meta.hasChild(BoxTypes.NERO_METADATA_TAGS_BOX))
            parseNeroTags((NeroMetadataTagsBox) meta.getChild(BoxTypes.NERO_METADATA_TAGS_BOX));
    }

    //parses specific children of 'udta': 3GPP
    //TODO: handle language codes
    private void parse3GPPData(Box udta) {
        if (udta.hasChild(BoxTypes.THREE_GPP_ALBUM_BOX)) {
            ThreeGPPAlbumBox albm = (ThreeGPPAlbumBox) udta.getChild(BoxTypes.THREE_GPP_ALBUM_BOX);
            put(Field.ALBUM, albm.getData());
            put(Field.TRACK_NUMBER, albm.getTrackNumber());
        }
        //if(udta.hasChild(BoxTypes.THREE_GPP_AUTHOR_BOX));
        //if(udta.hasChild(BoxTypes.THREE_GPP_CLASSIFICATION_BOX));
        if (udta.hasChild(BoxTypes.THREE_GPP_DESCRIPTION_BOX))
            put(Field.DESCRIPTION, ((ThreeGPPMetadataBox) udta.getChild(BoxTypes.THREE_GPP_DESCRIPTION_BOX)).getData());
        if (udta.hasChild(BoxTypes.THREE_GPP_KEYWORDS_BOX))
            put(Field.KEYWORDS, ((ThreeGPPMetadataBox) udta.getChild(BoxTypes.THREE_GPP_KEYWORDS_BOX)).getData());
        if (udta.hasChild(BoxTypes.THREE_GPP_LOCATION_INFORMATION_BOX))
            put(Field.LOCATION, ((ThreeGPPLocationBox) udta.getChild(BoxTypes.THREE_GPP_LOCATION_INFORMATION_BOX)).getPlaceName());
        if (udta.hasChild(BoxTypes.THREE_GPP_PERFORMER_BOX))
            put(Field.ARTIST, ((ThreeGPPMetadataBox) udta.getChild(BoxTypes.THREE_GPP_PERFORMER_BOX)).getData());
        if (udta.hasChild(BoxTypes.THREE_GPP_RECORDING_YEAR_BOX)) {
            final String value = ((ThreeGPPMetadataBox) udta.getChild(BoxTypes.THREE_GPP_RECORDING_YEAR_BOX)).getData();
            try {
                put(Field.RELEASE_DATE, new Date(Integer.parseInt(value)));
            } catch (NumberFormatException e) {
                Logger.getLogger("MP4 API").log(Level.INFO, "unable to parse 3GPP metadata: recording year value: {0}", value);
            }
        }
        if (udta.hasChild(BoxTypes.THREE_GPP_TITLE_BOX))
            put(Field.TITLE, ((ThreeGPPMetadataBox) udta.getChild(BoxTypes.THREE_GPP_TITLE_BOX)).getData());
    }

    //parses children of 'ilst': iTunes
    private void parseITunesMetaData(Box ilst) {
        final List<Box> boxes = ilst.getChildren();
        long l;
        ITunesMetadataBox data;
        for (Box box : boxes) {
            l = box.getType();
            data = (ITunesMetadataBox) box.getChild(BoxTypes.ITUNES_METADATA_BOX);

            if (l == BoxTypes.ARTIST_NAME_BOX) put(Field.ARTIST, data.getText());
            else if (l == BoxTypes.TRACK_NAME_BOX) put(Field.TITLE, data.getText());
            else if (l == BoxTypes.ALBUM_ARTIST_NAME_BOX) put(Field.ALBUM_ARTIST, data.getText());
            else if (l == BoxTypes.ALBUM_NAME_BOX) put(Field.ALBUM, data.getText());
            else if (l == BoxTypes.TRACK_NUMBER_BOX) {
                byte[] b = data.getData();
                put(Field.TRACK_NUMBER, (int) b[3]);
                put(Field.TOTAL_TRACKS, (int) b[5]);
            } else if (l == BoxTypes.DISK_NUMBER_BOX) put(Field.DISK_NUMBER, data.getInteger());
            else if (l == BoxTypes.COMPOSER_NAME_BOX) put(Field.COMPOSER, data.getText());
            else if (l == BoxTypes.COMMENTS_BOX) put(Field.COMMENTS, data.getText());
            else if (l == BoxTypes.TEMPO_BOX) put(Field.TEMPO, data.getInteger());
            else if (l == BoxTypes.RELEASE_DATE_BOX) put(Field.RELEASE_DATE, data.getDate());
            else if (l == BoxTypes.GENRE_BOX || l == BoxTypes.CUSTOM_GENRE_BOX) {
                String s = null;
                if (data.getDataType() == ITunesMetadataBox.DataType.UTF8) s = data.getText();
                else {
                    final int i = data.getInteger();
                    if (i > 0 && i < STANDARD_GENRES.length) s = STANDARD_GENRES[data.getInteger()];
                }
                if (s != null) put(Field.GENRE, s);
            } else if (l == BoxTypes.ENCODER_NAME_BOX) put(Field.ENCODER_NAME, data.getText());
            else if (l == BoxTypes.ENCODER_TOOL_BOX) put(Field.ENCODER_TOOL, data.getText());
            else if (l == BoxTypes.COPYRIGHT_BOX) put(Field.COPYRIGHT, data.getText());
            else if (l == BoxTypes.COMPILATION_PART_BOX) put(Field.COMPILATION, data.getBoolean());
            else if (l == BoxTypes.COVER_BOX) {
                final Artwork aw = new Artwork(Artwork.Type.forDataType(data.getDataType()), data.getData());
                if (contents.containsKey(Field.COVER_ARTWORKS)) get(Field.COVER_ARTWORKS).add(aw);
                else {
                    final List<Artwork> list = new ArrayList<Artwork>();
                    list.add(aw);
                    put(Field.COVER_ARTWORKS, list);
                }
            } else if (l == BoxTypes.GROUPING_BOX) put(Field.GROUPING, data.getText());
            else if (l == BoxTypes.LYRICS_BOX) put(Field.LYRICS, data.getText());
            else if (l == BoxTypes.RATING_BOX) put(Field.RATING, data.getInteger());
            else if (l == BoxTypes.PODCAST_BOX) put(Field.PODCAST, data.getInteger());
            else if (l == BoxTypes.PODCAST_URL_BOX) put(Field.PODCAST_URL, data.getText());
            else if (l == BoxTypes.CATEGORY_BOX) put(Field.CATEGORY, data.getText());
            else if (l == BoxTypes.KEYWORD_BOX) put(Field.KEYWORDS, data.getText());
            else if (l == BoxTypes.DESCRIPTION_BOX) put(Field.DESCRIPTION, data.getText());
            else if (l == BoxTypes.LONG_DESCRIPTION_BOX) put(Field.DESCRIPTION, data.getText());
            else if (l == BoxTypes.TV_SHOW_BOX) put(Field.TV_SHOW, data.getText());
            else if (l == BoxTypes.TV_NETWORK_NAME_BOX) put(Field.TV_NETWORK, data.getText());
            else if (l == BoxTypes.TV_EPISODE_BOX) put(Field.TV_EPISODE, data.getText());
            else if (l == BoxTypes.TV_EPISODE_NUMBER_BOX) put(Field.TV_EPISODE_NUMBER, data.getInteger());
            else if (l == BoxTypes.TV_SEASON_BOX) put(Field.TV_SEASON, data.getInteger());
            else if (l == BoxTypes.PURCHASE_DATE_BOX) put(Field.PURCHASE_DATE, data.getText());
            else if (l == BoxTypes.GAPLESS_PLAYBACK_BOX) put(Field.GAPLESS_PLAYBACK, data.getText());
            else if (l == BoxTypes.HD_VIDEO_BOX) put(Field.HD_VIDEO, data.getBoolean());
            else if (l == BoxTypes.ARTIST_SORT_BOX) put(Field.ARTIST_SORT_TEXT, data.getText());
            else if (l == BoxTypes.TRACK_SORT_BOX) put(Field.TITLE_SORT_TEXT, data.getText());
            else if (l == BoxTypes.ALBUM_SORT_BOX) put(Field.ALBUM_SORT_TEXT, data.getText());
        }
    }

    //parses children of ID3
    private void parseID3(ID3TagBox box) {
        try {
            final DataInputStream in = new DataInputStream(new ByteArrayInputStream(box.getID3Data()));
            ID3Tag tag = new ID3Tag(in);
            int[] num;
            for (ID3Frame frame : tag.getFrames()) {
                switch (frame.getID()) {
                    case ID3Frame.TITLE:
                        put(Field.TITLE, frame.getEncodedText());
                        break;
                    case ID3Frame.ALBUM_TITLE:
                        put(Field.ALBUM, frame.getEncodedText());
                        break;
                    case ID3Frame.TRACK_NUMBER:
                        num = frame.getNumbers();
                        put(Field.TRACK_NUMBER, num[0]);
                        if (num.length > 1) put(Field.TOTAL_TRACKS, num[1]);
                        break;
                    case ID3Frame.ARTIST:
                        put(Field.ARTIST, frame.getEncodedText());
                        break;
                    case ID3Frame.COMPOSER:
                        put(Field.COMPOSER, frame.getEncodedText());
                        break;
                    case ID3Frame.BEATS_PER_MINUTE:
                        put(Field.TEMPO, frame.getNumber());
                        break;
                    case ID3Frame.LENGTH:
                        put(Field.LENGTH_IN_MILLISECONDS, frame.getNumber());
                        break;
                    case ID3Frame.LANGUAGES:
                        put(Field.LANGUAGE, frame.getLocale());
                        break;
                    case ID3Frame.COPYRIGHT_MESSAGE:
                        put(Field.COPYRIGHT, frame.getEncodedText());
                        break;
                    case ID3Frame.PUBLISHER:
                        put(Field.PUBLISHER, frame.getEncodedText());
                        break;
                    case ID3Frame.INTERNET_RADIO_STATION_NAME:
                        put(Field.INTERNET_RADIO_STATION, frame.getEncodedText());
                        break;
                    case ID3Frame.ENCODING_TIME:
                        put(Field.ENCODING_DATE, frame.getDate());
                        break;
                    case ID3Frame.RELEASE_TIME:
                        put(Field.RELEASE_DATE, frame.getDate());
                        break;
                    case ID3Frame.ENCODING_TOOLS_AND_SETTINGS:
                        put(Field.ENCODER_TOOL, frame.getEncodedText());
                        break;
                    case ID3Frame.PERFORMER_SORT_ORDER:
                        put(Field.ARTIST_SORT_TEXT, frame.getEncodedText());
                        break;
                    case ID3Frame.TITLE_SORT_ORDER:
                        put(Field.TITLE_SORT_TEXT, frame.getEncodedText());
                        break;
                    case ID3Frame.ALBUM_SORT_ORDER:
                        put(Field.ALBUM_SORT_TEXT, frame.getEncodedText());
                        break;
                }
            }
        } catch (IOException e) {
            Logger.getLogger("MP4 API").log(Level.SEVERE, "Exception in MetaData.parseID3: {0}", e.toString());
        }
    }

    //parses children of 'tags': Nero
    private void parseNeroTags(NeroMetadataTagsBox tags) {
        final Map<String, String> pairs = tags.getPairs();
        String val;
        for (String key : pairs.keySet()) {
            val = pairs.get(key);
            try {
                if (key.equals(NERO_TAGS[0])) put(Field.ARTIST, val);
                if (key.equals(NERO_TAGS[1])) put(Field.TITLE, val);
                if (key.equals(NERO_TAGS[2])) put(Field.ALBUM, val);
                if (key.equals(NERO_TAGS[3])) put(Field.TRACK_NUMBER, Integer.parseInt(val));
                if (key.equals(NERO_TAGS[4])) put(Field.TOTAL_TRACKS, Integer.parseInt(val));
                if (key.equals(NERO_TAGS[5])) {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, Integer.parseInt(val));
                    put(Field.RELEASE_DATE, c.getTime());
                }
                if (key.equals(NERO_TAGS[6])) put(Field.GENRE, val);
                if (key.equals(NERO_TAGS[7])) put(Field.DISK_NUMBER, Integer.parseInt(val));
                if (key.equals(NERO_TAGS[8])) put(Field.TOTAL_DISKS, Integer.parseInt(val));
                if (key.equals(NERO_TAGS[9])) ; //url
                if (key.equals(NERO_TAGS[10])) put(Field.COPYRIGHT, val);
                if (key.equals(NERO_TAGS[11])) put(Field.COMMENTS, val);
                if (key.equals(NERO_TAGS[12])) put(Field.LYRICS, val);
                if (key.equals(NERO_TAGS[13])) ; //credits
                if (key.equals(NERO_TAGS[14])) put(Field.RATING, Integer.parseInt(val));
                if (key.equals(NERO_TAGS[15])) put(Field.PUBLISHER, val);
                if (key.equals(NERO_TAGS[16])) put(Field.COMPOSER, val);
                if (key.equals(NERO_TAGS[17])) ; //isrc
                if (key.equals(NERO_TAGS[18])) ; //mood
                if (key.equals(NERO_TAGS[19])) put(Field.TEMPO, Integer.parseInt(val));
            } catch (NumberFormatException e) {
                Logger.getLogger("MP4 API").log(Level.SEVERE, "Exception in MetaData.parseNeroTags: {0}", e.toString());
            }
        }
    }

    private <T> void put(Field<T> field, T value) {
        contents.put(field, value);
    }

    boolean containsMetaData() {
        return !contents.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Field<T> field) {
        return (T) contents.get(field);
    }

    public Map<Field<?>, Object> getAll() {
        return Collections.unmodifiableMap(contents);
    }

    public static class Field<T> {

        public static final Field<String> ARTIST = new Field<String>("Artist");
        public static final Field<String> TITLE = new Field<String>("Title");
        public static final Field<String> ALBUM_ARTIST = new Field<String>("Album Artist");
        public static final Field<String> ALBUM = new Field<String>("Album");
        public static final Field<Integer> TRACK_NUMBER = new Field<Integer>("Track Number");
        public static final Field<Integer> TOTAL_TRACKS = new Field<Integer>("Total Tracks");
        public static final Field<Integer> DISK_NUMBER = new Field<Integer>("Disk Number");
        public static final Field<Integer> TOTAL_DISKS = new Field<Integer>("Total disks");
        public static final Field<String> COMPOSER = new Field<String>("Composer");
        public static final Field<String> COMMENTS = new Field<String>("Comments");
        public static final Field<Integer> TEMPO = new Field<Integer>("Tempo");
        public static final Field<Integer> LENGTH_IN_MILLISECONDS = new Field<Integer>("Length in milliseconds");
        public static final Field<Date> RELEASE_DATE = new Field<Date>("Release Date");
        public static final Field<String> GENRE = new Field<String>("Genre");
        public static final Field<String> ENCODER_NAME = new Field<String>("Encoder Name");
        public static final Field<String> ENCODER_TOOL = new Field<String>("Encoder Tool");
        public static final Field<Date> ENCODING_DATE = new Field<Date>("Encoding Date");
        public static final Field<String> COPYRIGHT = new Field<String>("Copyright");
        public static final Field<String> PUBLISHER = new Field<String>("Publisher");
        public static final Field<Boolean> COMPILATION = new Field<Boolean>("Part of compilation");
        public static final Field<List<Artwork>> COVER_ARTWORKS = new Field<List<Artwork>>("Cover Artworks");
        public static final Field<String> GROUPING = new Field<String>("Grouping");
        public static final Field<String> LOCATION = new Field<String>("Location");
        public static final Field<String> LYRICS = new Field<String>("Lyrics");
        public static final Field<Integer> RATING = new Field<Integer>("Rating");
        public static final Field<Integer> PODCAST = new Field<Integer>("Podcast");
        public static final Field<String> PODCAST_URL = new Field<String>("Podcast URL");
        public static final Field<String> CATEGORY = new Field<String>("Category");
        public static final Field<String> KEYWORDS = new Field<String>("Keywords");
        public static final Field<Integer> EPISODE_GLOBAL_UNIQUE_ID = new Field<Integer>("Episode Global Unique ID");
        public static final Field<String> DESCRIPTION = new Field<String>("Description");
        public static final Field<String> TV_SHOW = new Field<String>("TV Show");
        public static final Field<String> TV_NETWORK = new Field<String>("TV Network");
        public static final Field<String> TV_EPISODE = new Field<String>("TV Episode");
        public static final Field<Integer> TV_EPISODE_NUMBER = new Field<Integer>("TV Episode Number");
        public static final Field<Integer> TV_SEASON = new Field<Integer>("TV Season");
        public static final Field<String> INTERNET_RADIO_STATION = new Field<String>("Internet Radio Station");
        public static final Field<String> PURCHASE_DATE = new Field<String>("Purchase Date");
        public static final Field<String> GAPLESS_PLAYBACK = new Field<String>("Gapless Playback");
        public static final Field<Boolean> HD_VIDEO = new Field<Boolean>("HD Video");
        public static final Field<Locale> LANGUAGE = new Field<Locale>("Language");
        //sorting
        public static final Field<String> ARTIST_SORT_TEXT = new Field<String>("Artist Sort Text");
        public static final Field<String> TITLE_SORT_TEXT = new Field<String>("Title Sort Text");
        public static final Field<String> ALBUM_SORT_TEXT = new Field<String>("Album Sort Text");
        private String name;

        private Field(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
