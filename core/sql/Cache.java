package com.coloryr.allmusic.server.core.sql;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Cache {
    public static Set<String> banPlayers = new CopyOnWriteArraySet<>();
    public static Set<String> banMusic = new CopyOnWriteArraySet<>();

    public static boolean haveBan(String player) {
        return banPlayers.contains(player);
    }

    public static void updateData() {
        updatePlayer();
        updateMusic();
    }

    private static void updatePlayer() {
        banPlayers.clear();
        banPlayers.addAll(DataSql.getBanPlayerList());
    }

    private static void updateMusic() {
        banMusic.clear();
        banMusic.addAll(DataSql.getBanMusicList());
    }
}
