package com.coloryr.allmusic.server.side.bukkit;

import com.coloryr.allmusic.server.core.AllMusic;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class FunCore {
    public static void addMusic() {
        if (AllMusic.getConfig().funConfig.rain) {
            int rate = AllMusic.getConfig().funConfig.rainRate;
            boolean rain;
            if (rate <= 1) {
                rain = true;
            } else {
                rain = AllMusic.random.nextInt(rate) == (rate / 2);
            }
            if (rain) {
                for (World item : Bukkit.getWorlds()) {
                    item.setStorm(true);
                }
                AllMusic.side.broadcast(AllMusic.getMessage().fun.rain);
            }
        }
    }
}
