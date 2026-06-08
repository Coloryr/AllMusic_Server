package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Arrays;

public class MP4InputStream {

    public static final int MASK8 = 0xFF;
    public static final int MASK16 = 0xFFFF;
    public static final String UTF8 = "UTF-8";
    public static final String UTF16 = "UTF-16";
    private static final int BYTE_ORDER_MASK = 0xFEFF;
    private final InputStream in;
    private final RandomAccessFile fin;
    private int peeked;
    private long offset; //only used with InputStream

    /**
     * Constructs an <code>MP4InputStream</code> that reads from an
     * <code>InputStream</code>. It will have no random access, thus seeking
     * will not be possible.
     *
     * @param in an <code>InputStream</code> to read from
     */
    MP4InputStream(InputStream in) {
        this.in = in;
        fin = null;
        peeked = -1;
        offset = 0;
    }

    /**
     * Constructs an <code>MP4InputStream</code> that reads from a
     * <code>RandomAccessFile</code>. It will have random access and seeking
     * will be possible.
     *
     * @param fin a <code>RandomAccessFile</code> to read from
     */
    MP4InputStream(RandomAccessFile fin) {
        this.fin = fin;
        in = null;
        peeked = -1;
    }

    /**
     * Reads the next byte of data from the input. The value byte is returned as
     * an int in the range 0 to 255. If no byte is available because the end of
     * the stream has been reached, an EOFException is thrown. This method
     * blocks until input data is available, the end of the stream is detected,
     * or an I/O error occurs.
     *
     * @return the next byte of data
     * @throws IOException If the end of the stream is detected or any I/O error occurs.
     */
    public int read() throws IOException {
        int i = 0;
        if (peeked >= 0) {
            i = peeked;
            peeked = -1;
        } else if (in != null) i = in.read();
        else if (fin != null) i = fin.read();

        if (i == -1) throw new EOFException();
        else if (in != null) offset++;
        return i;
    }

    /**
     * Reads <code>len</code> bytes of data from the input into the array
     * <code>b</code>. If len is zero, then no bytes are read.
     * <p>
     * This method blocks until all bytes could be read, the end of the stream
     * is detected, or an I/O error occurs.
     * <p>
     * If the stream ends before <code>len</code> bytes could be read an
     * EOFException is thrown.
     *
     * @param b   the buffer into which the data is read.
     * @param off the start offset in array <code>b</code> at which the data is written.
     * @param len the number of bytes to read.
     * @throws IOException If the end of the stream is detected, the input
     *                     stream has been closed, or if some other I/O error occurs.
     */
    public void read(final byte[] b, int off, int len) throws IOException {
        int read = 0;
        int i = 0;

        if (peeked >= 0 && len > 0) {
            b[off] = (byte) peeked;
            peeked = -1;
            read++;
        }

        while (read < len) {
            if (in != null) i = in.read(b, off + read, len - read);
            else if (fin != null) i = fin.read(b, off + read, len - read);
            if (i < 0) throw new EOFException();
            else read += i;
        }

        offset += read;
    }

    /**
     * Reads up to eight bytes as a long value. This method blocks until all
     * bytes could be read, the end of the stream is detected, or an I/O error
     * occurs.
     *
     * @param n the number of bytes to read >0 and <=8
     * @return the read bytes as a long value
     * @throws IOException               If the end of the stream is detected, the input
     *                                   stream has been closed, or if some other I/O error occurs.
     * @throws IndexOutOfBoundsException if <code>n</code> is not in the range
     *                                   [1...8] inclusive.
     */
    public long readBytes(int n) throws IOException {
        if (n < 1 || n > 8) throw new IndexOutOfBoundsException("invalid number of bytes to read: " + n);
        final byte[] b = new byte[n];
        read(b, 0, n);

        long result = 0;
        for (int i = 0; i < n; i++) {
            result = (result << 8) | (b[i] & 0xFF);
        }
        return result;
    }

    /**
     * Reads data from the input stream and stores them into the buffer array b.
     * This method blocks until all bytes could be read, the end of the stream
     * is detected, or an I/O error occurs.
     * If the length of b is zero, then no bytes are read.
     *
     * @param b the buffer into which the data is read.
     * @throws IOException If the end of the stream is detected, the input
     *                     stream has been closed, or if some other I/O error occurs.
     */
    public void readBytes(final byte[] b) throws IOException {
        read(b, 0, b.length);
    }

    /**
     * Reads <code>n</code> bytes from the input as a String. The bytes are
     * directly converted into characters. If not enough bytes could be read, an
     * EOFException is thrown.
     * This method blocks until all bytes could be read, the end of the stream
     * is detected, or an I/O error occurs.
     *
     * @param n the length of the String.
     * @return the String, that was read
     * @throws IOException If the end of the stream is detected, the input
     *                     stream has been closed, or if some other I/O error occurs.
     */
    public String readString(final int n) throws IOException {
        int i = -1;
        int pos = 0;
        char[] c = new char[n];
        while (pos < n) {
            i = read();
            c[pos] = (char) i;
            pos++;
        }
        return new String(c, 0, pos);
    }

    /**
     * Reads a null-terminated UTF-encoded String from the input. The maximum
     * number of bytes that can be read before the null must appear must be
     * specified.
     * Although the method is preferred for unicode, the encoding can be any
     * charset name, that is supported by the system.
     * <p>
     * This method blocks until all bytes could be read, the end of the stream
     * is detected, or an I/O error occurs.
     *
     * @param max      the maximum number of bytes to read, before the null-terminator
     *                 must appear.
     * @param encoding the charset used to encode the String
     * @return the decoded String
     * @throws IOException If the end of the stream is detected, the input
     *                     stream has been closed, or if some other I/O error occurs.
     */
    public String readUTFString(int max, String encoding) throws IOException {
        return new String(readTerminated(max, 0), Charset.forName(encoding));
    }

    /**
     * Reads a null-terminated UTF-encoded String from the input. The maximum
     * number of bytes that can be read before the null must appear must be
     * specified.
     * The encoding is detected automatically, it may be UTF-8 or UTF-16
     * (determined by a byte order mask at the beginning).
     * <p>
     * This method blocks until all bytes could be read, the end of the stream
     * is detected, or an I/O error occurs.
     *
     * @param max the maximum number of bytes to read, before the null-terminator
     *            must appear.
     * @return the decoded String
     * @throws IOException If the end of the stream is detected, the input
     *                     stream has been closed, or if some other I/O error occurs.
     */
    public String readUTFString(int max) throws IOException {
        //read byte order mask
        final byte[] bom = new byte[2];
        read(bom, 0, 2);
        if (bom[0] == 0 || bom[1] == 0) return new String();
        final int i = (bom[0] << 8) | bom[1];

        //read null-terminated
        final byte[] b = readTerminated(max - 2, 0);
        //copy bom
        byte[] b2 = new byte[b.length + bom.length];
        System.arraycopy(bom, 0, b2, 0, bom.length);
        System.arraycopy(b, 0, b2, bom.length, b.length);

        return new String(b2, Charset.forName((i == BYTE_ORDER_MASK) ? UTF16 : UTF8));
    }

    /**
     * Reads a byte array from the input that is terminated by a specific byte
     * (the 'terminator'). The maximum number of bytes that can be read before
     * the terminator must appear must be specified.
     * <p>
     * The terminator will not be included in the returned array.
     * <p>
     * This method blocks until all bytes could be read, the end of the stream
     * is detected, or an I/O error occurs.
     *
     * @param max        the maximum number of bytes to read, before the terminator
     *                   must appear.
     * @param terminator the byte that indicates the end of the array
     * @return the buffer into which the data is read.
     * @throws IOException If the end of the stream is detected, the input
     *                     stream has been closed, or if some other I/O error occurs.
     */
    public byte[] readTerminated(int max, int terminator) throws IOException {
        final byte[] b = new byte[max];
        int pos = 0;
        int i = 0;
        while (pos < max && i != -1) {
            i = read();
            if (i != -1) b[pos++] = (byte) i;
        }
        return Arrays.copyOf(b, pos);
    }

    /**
     * Reads a fixed point number from the input. The number is read as a
     * <code>m.n</code> value, that results from deviding an integer by
     * 2<sup>n</sup>.
     *
     * @param m the number of bits before the point
     * @param n the number of bits after the point
     * @return a floating point number with the same value
     * @throws IOException              If the end of the stream is detected, the input
     *                                  stream has been closed, or if some other I/O error occurs.
     * @throws IllegalArgumentException if the total number of bits (m+n) is not
     *                                  a multiple of eight
     */
    public double readFixedPoint(int m, int n) throws IOException {
        final int bits = m + n;
        if ((bits % 8) != 0) throw new IllegalArgumentException("number of bits is not a multiple of 8: " + (m + n));
        final long l = readBytes(bits / 8);
        final double x = Math.pow(2, n);
        double d = ((double) l) / x;
        return d;
    }

    /**
     * Skips <code>n</code> bytes in the input. This method blocks until all
     * bytes could be skipped, the end of the stream is detected, or an I/O
     * error occurs.
     *
     * @param n the number of bytes to skip
     * @throws IOException If the end of the stream is detected, the input
     *                     stream has been closed, or if some other I/O error occurs.
     */
    public void skipBytes(final long n) throws IOException {
        long l = 0;
        if (peeked >= 0 && n > 0) {
            peeked = -1;
            l++;
        }

        while (l < n) {
            if (in != null) l += in.skip((n - l));
            else if (fin != null) l += fin.skipBytes((int) (n - l));
        }

        offset += l;
    }

    /**
     * Returns the current offset in the stream.
     *
     * @return the current offset
     * @throws IOException if an I/O error occurs (only when using a RandomAccessFile)
     */
    public long getOffset() throws IOException {
        long l = -1;
        if (in != null) l = offset;
        else if (fin != null) l = fin.getFilePointer();
        return l;
    }

    /**
     * Seeks to a specific offset in the stream. This is only possible when
     * using a RandomAccessFile. If an InputStream is used, this method throws
     * an IOException.
     *
     * @param pos the offset position, measured in bytes from the beginning of the
     *            stream
     * @throws IOException if an InputStream is used, pos is less than 0 or an
     *                     I/O error occurs
     */
    public void seek(long pos) throws IOException {
        if (fin != null) fin.seek(pos);
        else throw new IOException("could not seek: no random access");
    }

    /**
     * Indicates, if random access is available. That is, if this
     * <code>MP4InputStream</code> was constructed with a RandomAccessFile. If
     * this method returns false, seeking is not possible.
     *
     * @return true if random access is available
     */
    public boolean hasRandomAccess() {
        return fin != null;
    }

    /**
     * Indicates, if the input has some data left.
     *
     * @return true if there is at least one byte left
     * @throws IOException if an I/O error occurs
     */
    public boolean hasLeft() throws IOException {
        final boolean b;
        if (fin != null) b = fin.getFilePointer() < (fin.length() - 1);
        else if (peeked >= 0) b = true;
        else {
            final int i = in.read();
            b = (i != -1);
            if (b) peeked = i;
        }
        return b;
    }

    /**
     * Closes the input and releases any system resources associated with it.
     * Once the stream has been closed, further reading or skipping will throw
     * an IOException. Closing a previously closed stream has no effect.
     *
     * @throws IOException if an I/O error occurs
     */
    void close() throws IOException {
        peeked = -1;
        if (in != null) in.close();
        else if (fin != null) fin.close();
    }
}
