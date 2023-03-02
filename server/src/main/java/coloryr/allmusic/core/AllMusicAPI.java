package coloryr.allmusic.core;

import coloryr.allmusic.core.enums.HudPos;
import coloryr.allmusic.core.side.ComType;

public class AllMusicAPI {
    /**
     * 发送播放音乐
     *
     * @param name 用户名
     * @param url  链接
     */
    public static void playMusic(String name, String url) {
        AllMusic.side.send(name, ComType.play + url);
    }

    /**
     * 更新玩家Hud数据
     *
     * @param name 用户名
     * @param pos  位置
     * @param data 数据
     */
    public static void sendHud(String name, HudPos pos, String data) {
        String temp = getType(pos);
        if (temp == null)
            return;
        AllMusic.side.send(name, temp + data);
    }

    /**
     * 发送图片
     *
     * @param name 用户名
     * @param url  图片地址
     */
    public static void sendPic(String name, String url) {
        AllMusic.side.send(name, ComType.img + url);
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
