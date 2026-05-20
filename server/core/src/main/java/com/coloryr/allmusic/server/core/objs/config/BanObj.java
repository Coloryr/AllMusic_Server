package com.coloryr.allmusic.server.core.objs.config;

import java.util.*;

public class BanObj {
    public Set<String> banPlayers;
    public Set<String> banServer;
    public Set<String> mutePlayers;
    public Set<String> muteListPlayers;
    public Map<String, List<String>> banMusics;

    public boolean check() {
        return banPlayers == null || banServer == null || banMusics == null || mutePlayers == null
                || muteListPlayers == null;
    }

    public static BanObj make() {
        BanObj obj = new BanObj();
        obj.banMusics = new HashMap<>();
        obj.banPlayers = new HashSet<>();
        obj.banServer = new HashSet<>();
        obj.mutePlayers = new HashSet<>();
        obj.muteListPlayers = new HashSet<>();

        return obj;
    }


}
