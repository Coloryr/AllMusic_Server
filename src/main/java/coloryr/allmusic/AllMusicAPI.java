package coloryr.allmusic;

import coloryr.allmusic.hud.obj.HudPos;
import coloryr.allmusic.side.ComType;

public class AllMusicAPI {
    public static void playMusic(String name, String url) {
        AllMusic.side.send(name, ComType.play + url, null);
    }

    public static void sendHud(String name, HudPos pos, String data) {
        String temp = getType(pos);
        if (temp == null)
            return;
        AllMusic.side.send(name, temp + data, null);
    }

    public static void sendPic(String name, String url) {
        AllMusic.side.send(name, ComType.img + url, null);
    }

    public static void sendStop(String name) {
        AllMusic.side.sendStop(name);
    }

    private static String getType(HudPos pos) {
        switch (pos) {
            case info:
                return ComType.info;
            case lyric:
                return ComType.lyric;
            case list:
                return ComType.list;
        }

        return null;
    }
}
