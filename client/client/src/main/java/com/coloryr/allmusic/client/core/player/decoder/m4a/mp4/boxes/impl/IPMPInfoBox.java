package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.MP4InputStream;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.FullBox;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.od.Descriptor;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.od.ObjectDescriptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The IPMPInfoBox contains IPMP Descriptors which document the protection
 * applied to the stream.
 * <p>
 * The IPMP Descriptor is defined in ISO/IEC 14496-1. This is a part of the
 * MPEG-4 object descriptors (OD) that describe how an object can be accessed
 * and decoded. In the ISO Base Media File Format, IPMP Descriptor can be
 * carried directly in IPMPInfoBox without the need for OD stream.
 * <p>
 * The presence of IPMP Descriptor in this IPMPInfoBox indicates the associated
 * media stream is protected by the IPMP Tool described in the IPMP Descriptor.
 * <p>
 * Each IPMP Descriptor has an IPMP-toolID, which identifies the required IPMP
 * tool for protection. An independent registration authority (RA) is used so
 * any party can register its own IPMP Tool and identify this without
 * collisions.
 * <p>
 * The IPMP Descriptor carries IPMP information for one or more IPMP Tool
 * instances, it includes but not limited to IPMP Rights Data, IPMP Key Data,
 * Tool Configuration Data, etc.
 * <p>
 * More than one IPMP Descriptors can be carried in this IPMPInfoBox if this
 * media stream is protected by more than one IPMP Tools.
 *
 * @author in-somnia
 */
public class IPMPInfoBox extends FullBox {

    private List</*IPMP*/Descriptor> ipmpDescriptors;

    public IPMPInfoBox() {
        super("IPMP Info Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);

        ipmpDescriptors = new ArrayList</*IPMP*/Descriptor>();
        /*IPMP*/
        Descriptor desc;
        while (getLeft(in) > 0) {
            desc = (/*IPMP*/Descriptor) ObjectDescriptor.createDescriptor(in);
            ipmpDescriptors.add(desc);
        }
    }

    /**
     * The contained list of IPMP descriptors.
     *
     * @return the IPMP descriptors
     */
    public List</*IPMP*/Descriptor> getIPMPDescriptors() {
        return Collections.unmodifiableList(ipmpDescriptors);
    }
}
