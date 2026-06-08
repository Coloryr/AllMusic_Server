package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

public class ItemInformationEntry extends FullBox {

    private int itemID, itemProtectionIndex;
    private String itemName, contentType, contentEncoding;
    private long extensionType;
    private Extension extension;

    public ItemInformationEntry() {
        super("Item Information Entry");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        if ((version == 0) || (version == 1)) {
            itemID = (int) in.readBytes(2);
            itemProtectionIndex = (int) in.readBytes(2);
            itemName = in.readUTFString((int) getLeft(in), MP4InputStream.UTF8);
            contentType = in.readUTFString((int) getLeft(in), MP4InputStream.UTF8);
            contentEncoding = in.readUTFString((int) getLeft(in), MP4InputStream.UTF8); //optional
        }
        if (version == 1 && getLeft(in) > 0) {
            //optional
            extensionType = in.readBytes(4);
            if (getLeft(in) > 0) {
                extension = Extension.forType((int) extensionType);
                if (extension != null) extension.decode(in);
            }
        }
    }

    /**
     * The item ID contains either 0 for the primary resource (e.g., the XML
     * contained in an 'xml ' box) or the ID of the item for which the following
     * information is defined.
     *
     * @return the item ID
     */
    public int getItemID() {
        return itemID;
    }

    /**
     * The item protection index contains either 0 for an unprotected item, or
     * the one-based index into the item protection box defining the protection
     * applied to this item (the first box in the item protection box has the
     * index 1).
     *
     * @return the item protection index
     */
    public int getItemProtectionIndex() {
        return itemProtectionIndex;
    }

    /**
     * The item name is a String containing a symbolic name of the item (source
     * file for file delivery transmissions).
     *
     * @return the item name
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * The content type is a String with the MIME type of the item. If the item
     * is content encoded (see below), then the content type refers to the item
     * after content decoding.
     *
     * @return the content type
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * The content encoding is an optional String used to indicate that the
     * binary file is encoded and needs to be decoded before interpreted. The
     * values are as defined for Content-Encoding for HTTP/1.1. Some possible
     * values are "gzip", "compress" and "deflate". An empty string indicates no
     * content encoding. Note that the item is stored after the content encoding
     * has been applied.
     *
     * @return the content encoding
     */
    public String getContentEncoding() {
        return contentEncoding;
    }

    /**
     * The extension type is a printable four-character code that identifies the
     * extension fields of version 1 with respect to version 0 of the item
     * information entry.
     *
     * @return the extension type
     */
    public long getExtensionType() {
        return extensionType;
    }

    /**
     * Returns the extension.
     */
    public Extension getExtension() {
        return extension;
    }

    public abstract static class Extension {

        private static final int TYPE_FDEL = 1717855596; //fdel

        static Extension forType(int type) {
            final Extension ext;
            switch (type) {
                case Extension.TYPE_FDEL:
                    ext = new FDExtension();
                    break;
                default:
                    ext = null;
            }
            return ext;
        }

        abstract void decode(MP4InputStream in) throws IOException;
    }

    public static class FDExtension extends Extension {

        private String contentLocation, contentMD5;
        private long contentLength, transferLength;
        private long[] groupID;

        @Override
        void decode(MP4InputStream in) throws IOException {
            contentLocation = in.readUTFString(100, MP4InputStream.UTF8);
            contentMD5 = in.readUTFString(100, MP4InputStream.UTF8);

            contentLength = in.readBytes(8);
            transferLength = in.readBytes(8);

            final int entryCount = in.read();
            groupID = new long[entryCount];
            for (int i = 0; i < entryCount; i++) {
                groupID[i] = in.readBytes(4);
            }
        }

        /**
         * The content location is a String in containing the URI of the file as
         * defined in HTTP/1.1 (RFC 2616).
         *
         * @return the content location
         */
        public String getContentLocation() {
            return contentLocation;
        }

        /**
         * The content MD5 is a string containing an MD5 digest of the file. See
         * HTTP/1.1 (RFC 2616) and RFC 1864.
         *
         * @return the content MD5
         */
        public String getContentMD5() {
            return contentMD5;
        }

        /**
         * The total length (in bytes) of the (un-encoded) file.
         *
         * @return the content length
         */
        public long getContentLength() {
            return contentLength;
        }

        /**
         * The transfer length is the total length (in bytes) of the (encoded)
         * file. Note that transfer length is equal to content length if no
         * content encoding is applied (see above).
         *
         * @return the transfer length
         */
        public long getTransferLength() {
            return transferLength;
        }

        /**
         * The group ID indicates a file group to which the file item (source
         * file) belongs. See 3GPP TS 26.346 for more details on file groups.
         *
         * @return the group IDs
         */
        public long[] getGroupID() {
            return groupID;
        }
    }
}
