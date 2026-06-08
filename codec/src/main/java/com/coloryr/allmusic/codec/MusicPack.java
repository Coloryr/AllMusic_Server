package com.coloryr.allmusic.codec;

public class MusicPack {
    public CommandType type;

    public MusicPack(CommandType type) {
        this.type = type;
    }

    public static class StringMusicPack extends MusicPack {
        public String data;

        public StringMusicPack(CommandType type, String data) {
            super(type);
            this.data = data;
        }
    }

    public static class IntMusicPack extends MusicPack {
        public int data;

        public IntMusicPack(CommandType type, int data) {
            super(type);
            this.data = data;
        }
    }

    public static class LyricMusicPack extends MusicPack {
        public String lyric;
        public String tlyric;
        public String klyric;

        public LyricMusicPack(String lyric, String tlyric, String klyric) {
            super(CommandType.LYRIC);
            this.lyric = lyric;
            this.tlyric = tlyric;
            this.klyric = klyric;
        }
    }

    public static class LyricKtvMusicPack extends MusicPack {
        public KtvLyricObj data;
        public long time;

        public LyricKtvMusicPack(long time, KtvLyricObj data) {
            super(CommandType.LYRIC_KTV);
            this.data = data;
            this.time = time;
        }
    }

    public static class TimeMusicPack extends MusicPack {
        public long now;
        public long time;

        public TimeMusicPack(long time, long now) {
            super(CommandType.TIME);
            this.now = now;
            this.time = time;
        }
    }
}
