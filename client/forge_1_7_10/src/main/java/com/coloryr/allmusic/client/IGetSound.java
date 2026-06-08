package com.coloryr.allmusic.client;

import paulscode.sound.Channel;

import java.util.List;

public interface IGetSound {
    List<Channel> allMusic_Client$getNormalChannels();
    List<Channel> allMusic_Client$getStreamingChannels();
}
