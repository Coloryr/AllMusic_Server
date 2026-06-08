package com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api.drm;

import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.api.Protection;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.Box;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.BoxTypes;
import com.coloryr.allmusic.client.core.player.decoder.m4a.mp4.boxes.impl.drm.FairPlayDataBox;

public class ITunesProtection extends Protection {

    private final String userID, userName, userKey;
    private final byte[] privateKey, initializationVector;

    public ITunesProtection(Box sinf) {
        super(sinf);

        final Box schi = sinf.getChild(BoxTypes.SCHEME_INFORMATION_BOX);
        userID = new String(((FairPlayDataBox) schi.getChild(BoxTypes.FAIRPLAY_USER_ID_BOX)).getData());

        //user name box is filled with 0
        final byte[] b = ((FairPlayDataBox) schi.getChild(BoxTypes.FAIRPLAY_USER_NAME_BOX)).getData();
        int i = 0;
        while (b[i] != 0) {
            i++;
        }
        userName = new String(b, 0, i - 1);

        userKey = new String(((FairPlayDataBox) schi.getChild(BoxTypes.FAIRPLAY_USER_KEY_BOX)).getData());
        privateKey = ((FairPlayDataBox) schi.getChild(BoxTypes.FAIRPLAY_PRIVATE_KEY_BOX)).getData();
        initializationVector = ((FairPlayDataBox) schi.getChild(BoxTypes.FAIRPLAY_IV_BOX)).getData();
    }

    @Override
    public Scheme getScheme() {
        return Scheme.ITUNES_FAIR_PLAY;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserKey() {
        return userKey;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public byte[] getInitializationVector() {
        return initializationVector;
    }
}
