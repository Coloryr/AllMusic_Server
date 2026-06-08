package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api;

public class Frame implements Comparable<Frame> {

    private final Type type;
    private final long offset, size;
    private final double time;
    private byte[] data;

    Frame(Type type, long offset, long size, double time) {
        this.type = type;
        this.offset = offset;
        this.size = size;
        this.time = time;
    }

    public Type getType() {
        return type;
    }

    public long getOffset() {
        return offset;
    }

    public long getSize() {
        return size;
    }

    public double getTime() {
        return time;
    }

    public int compareTo(Frame f) {
        final double d = time - f.time;
        //0 should not happen, since frames don't have the same timestamps
        return (d < 0) ? -1 : ((d > 0) ? 1 : 0);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
