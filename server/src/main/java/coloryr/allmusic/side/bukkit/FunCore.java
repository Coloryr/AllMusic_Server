package coloryr.allmusic.side.bukkit;

import coloryr.allmusic.core.AllMusic;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Random;

public class FunCore {
    private static final Random random = new Random();

    public static void addMusic() {
        if (AllMusic.getConfig().FunConfig.Rain) {
            int rate = AllMusic.getConfig().FunConfig.RainRate;
            boolean rain;
            if (rate <= 1) {
                rain = true;
            } else {
                rain = random.nextInt(rate) == (rate / 2);
            }
            if (rain) {
                for (World item : Bukkit.getWorlds()) {
                    item.setStorm(true);
                }
                AllMusic.side.bq(AllMusic.getMessage().Fun.Rain);
            }
        }
    }
}
