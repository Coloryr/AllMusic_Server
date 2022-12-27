package coloryr.allmusic.api;

import coloryr.allmusic.AllMusic;
import coloryr.allmusic.hud.obj.HudPos;

public class AllMusicAPI {
    public static void playMusic(String name, String url) {
        AllMusic.side.send(name, "[url]" + url, false);
    }

    public static void sendHud(String name, HudPos pos, String data) {

    }

    public static void sendPic(String name, String url) {

    }
}
