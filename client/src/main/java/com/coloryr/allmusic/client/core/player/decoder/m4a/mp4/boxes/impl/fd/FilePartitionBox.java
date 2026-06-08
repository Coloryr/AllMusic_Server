package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.fd;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

public class FilePartitionBox extends FullBox {

    private int itemID, packetPayloadSize, fecEncodingID, fecInstanceID,
            maxSourceBlockLength, encodingSymbolLength, maxNumberOfEncodingSymbols;
    private String schemeSpecificInfo;
    private int[] blockCounts;
    private long[] blockSizes;

    public FilePartitionBox() {
        super("File Partition Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        itemID = (int) in.readBytes(2);
        packetPayloadSize = (int) in.readBytes(2);
        in.skipBytes(1); //reserved
        fecEncodingID = in.read();
        fecInstanceID = (int) in.readBytes(2);
        maxSourceBlockLength = (int) in.readBytes(2);
        encodingSymbolLength = (int) in.readBytes(2);
        maxNumberOfEncodingSymbols = (int) in.readBytes(2);
        schemeSpecificInfo = new String(Base64Decoder.decode(in.readTerminated((int) getLeft(in), 0)));

        final int entryCount = (int) in.readBytes(2);
        blockCounts = new int[entryCount];
        blockSizes = new long[entryCount];
        for (int i = 0; i < entryCount; i++) {
            blockCounts[i] = (int) in.readBytes(2);
            blockSizes[i] = (int) in.readBytes(4);
        }
    }

    /**
     * The item ID references the item in the item location box that the file
     * partitioning applies to.
     *
     * @return the item ID
     */
    public int getItemID() {
        return itemID;
    }

    /**
     * The packet payload size gives the target ALC/LCT or FLUTE packet payload
     * size of the partitioning algorithm. Note that UDP packet payloads are
     * larger, as they also contain ALC/LCT or FLUTE headers.
     *
     * @return the packet payload size
     */
    public int getPacketPayloadSize() {
        return packetPayloadSize;
    }

    /**
     * The FEC encoding ID is subject to IANA registration (see RFC 3452). Note
     * that
     * - value zero corresponds to the "Compact No-Code FEC scheme" also known
     * as "Null-FEC" (RFC 3695);
     * - value one corresponds to the "MBMS FEC" (3GPP TS 26.346);
     * - for values in the range of 0 to 127, inclusive, the FEC scheme is
     * Fully-Specified, whereas for values in the range of 128 to 255,
     * inclusive, the FEC scheme is Under-Specified.
     *
     * @return the FEC encoding ID
     */
    public int getFECEncodingID() {
        return fecEncodingID;
    }

    /**
     * The FEC instance ID provides a more specific identification of the FEC
     * encoder being used for an Under-Specified FEC scheme. This value should
     * be set to zero for Fully-Specified FEC schemes and shall be ignored when
     * parsing a file with an FEC encoding ID in the range of 0 to 127,
     * inclusive. The FEC instance ID is scoped by the FEC encoding ID. See RFC
     * 3452 for further details.
     *
     * @return the FEC instance ID
     */
    public int getFECInstanceID() {
        return fecInstanceID;
    }

    /**
     * The maximum source block length gives the maximum number of source
     * symbols per source block.
     *
     * @return the maximum source block length
     */
    public int getMaxSourceBlockLength() {
        return maxSourceBlockLength;
    }

    /**
     * The encoding symbol length gives the size (in bytes) of one encoding
     * symbol. All encoding symbols of one item have the same length, except the
     * last symbol which may be shorter.
     *
     * @return the encoding symbol length
     */
    public int getEncodingSymbolLength() {
        return encodingSymbolLength;
    }

    /**
     * The maximum number of encoding symbols  that can be generated for a
     * source block for those FEC schemes in which the maximum number of
     * encoding symbols is relevant, such as FEC encoding ID 129 defined in RFC
     * 3452. For those FEC schemes in which the maximum number of encoding
     * symbols is not relevant, the semantics of this field is unspecified.
     *
     * @return the maximum number of encoding symbols
     */
    public int getMaxNumberOfEncodingSymbols() {
        return maxNumberOfEncodingSymbols;
    }

    /**
     * The scheme specific info is a String of the scheme-specific object
     * transfer information (FEC-OTI-Scheme-Specific-Info). The definition of
     * the information depends on the EC encoding ID.
     *
     * @return the scheme specific info
     */
    public String getSchemeSpecificInfo() {
        return schemeSpecificInfo;
    }

    /**
     * A block count indicates the number of consecutive source blocks with a
     * specified size.
     *
     * @return all block counts
     */
    public int[] getBlockCounts() {
        return blockCounts;
    }

    /**
     * A block size indicates the size of a block (in bytes). A block_size that
     * is not a multiple of the encoding symbol length indicates with Compact
     * No-Code FEC that the last source symbols includes padding that is not
     * stored in the item. With MBMS FEC (3GPP TS 26.346) the padding may extend
     * across multiple symbols but the size of padding should never be more than
     * the encoding symbol length.
     *
     * @return all block sizes
     */
    public long[] getBlockSizes() {
        return blockSizes;
    }
}
