package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * The item information box provides extra information about selected items,
 * including symbolic ('file') names. It may optionally occur, but if it does,
 * it must be interpreted, as item protection or content encoding may have
 * changed the format of the data in the item. If both content encoding and
 * protection are indicated for an item, a reader should first un-protect the
 * item, and then decode the item's content encoding. If more control is needed,
 * an IPMP sequence code may be used.
 * <p>
 * This box contains an array of entries, and each entry is formatted as a box.
 * This array is sorted by increasing item ID in the entry records.
 * <p>
 * Two versions of the item info entry are defined. Version 1 includes
 * additional information to version 0 as specified by an extension type. For
 * instance, it shall be used with extension type 'fdel' for items that are
 * referenced by the file partition box ('fpar'), which is defined for source
 * file partitionings and applies to file delivery transmissions.
 * <p>
 * If no extension is desired, the box may terminate without the extension type
 * field and the extension; if, in addition, content encoding is not desired,
 * that field also may be absent and the box terminate before it.
 *
 * @author in-somnia
 */
public class ItemInformationBox extends FullBox {

    public ItemInformationBox() {
        super("Item Information Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        final int protectionCount = (int) in.readBytes(2);

        readChildren(in, protectionCount);
    }
}
