package coloryr.allmusic.core;

import coloryr.allmusic.core.objs.enums.HudType;
import coloryr.allmusic.core.objs.enums.ComType;

public class AllMusicAPI {
    /**
     * 发送播放音乐
     *
     * @param name 用户名
     * @param url  链接
     */
    public static void playMusic(String name, String url) {
        AllMusic.side.sendMusic(name, url);
    }

    /**
     * 更新玩家Hud数据
     *
     * @param name 用户名
     * @param pos  位置
     * @param data 数据
     */
    public static void sendHud(String name, HudType pos, String data) {
        AllMusic.side.sendHud(name, pos, data);
    }

    /**
     * 发送图片
     *
     * @param name 用户名
     * @param url  图片地址
     */
    public static void sendPic(String name, String url) {
        AllMusic.side.sendPic(name, url);
    }

    /**
     * 停止播放
     *
     * @param name 用户名
     */
    public static void sendStop(String name) {
        AllMusic.side.sendStop(name);
    }

    /**
     * 获取位置字符串
     *
     * @param pos 位置
     * @return 字符串
     */
    private static ComType getType(HudType pos) {
        switch (pos) {
            case INFO:
                return ComType.INFO;
            case LYRIC:
                return ComType.LYRIC;
            case LIST:
                return ComType.LIST;
        }

        return null;
    }
}
