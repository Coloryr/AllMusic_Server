package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;

import java.io.IOException;

/**
 * When the primary data is in XML format and it is desired that the XML be
 * stored directly in the meta-box, either the XMLBox or the BinaryXMLBox is
 * used. The Binary XML Box may only be used when there is a single well-defined
 * binarization of the XML for that defined format as identified by the handler.
 *
 * @author in-somnia
 * @see BinaryXMLBox
 */
public class XMLBox extends FullBox {

    private String content;

    public XMLBox() {
        super("XML Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        content = in.readUTFString((int) getLeft(in));
    }

    /**
     * The XML content.
     */
    public String getContent() {
        return content;
    }
}
