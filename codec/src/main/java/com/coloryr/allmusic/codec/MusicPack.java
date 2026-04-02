package com.coloryr.allmusic.codec;

public class MusicPack {
    public CommandType type;
    public String data;
    public int data1;

    public MusicPack(CommandType type, String data, int data1) {
        this.type = type;
        this.data = data;
        this.data1 = data1;
    }
}
