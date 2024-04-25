package com.coloryr.allmusic.server.core;

import com.coloryr.allmusic.server.core.objs.config.SaveObj;
import com.coloryr.allmusic.server.core.objs.enums.HudType;
import com.coloryr.allmusic.server.core.utils.HudUtils;

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
     * 获取玩家Hud信息
     *
     * @param player 玩家
     * @return Hud信息
     */
    public static SaveObj getHud(String player) {
        return HudUtils.get(player);
    }

    /**
     * 设置玩家Hud
     *
     * @param player 玩家
     * @param hud    Hud信息
     */
    public static void setHud(String player, SaveObj hud) {
        HudUtils.set(player, hud);
    }
}
