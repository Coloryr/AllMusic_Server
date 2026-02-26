package com.coloryr.allmusic.server.core;

import com.coloryr.allmusic.codec.HudPosObj;
import com.coloryr.allmusic.codec.HudType;
import com.coloryr.allmusic.server.core.utils.HudUtils;

public class AllMusicApi {

    /**
     * 注册音乐API
     * @param api 音乐API
     * @return 返回序号
     */
    public static int registerApi(IMusicApi api) {
        AllMusic.MUSIC_APIS.put(api.getId(), api);
        return AllMusic.MUSIC_APIS.size() - 1;
    }

    /**
     * 获取默认音乐API
     * @return 音乐API
     */
    public static IMusicApi getApiMusic() {
        return AllMusic.MUSIC_APIS.get(AllMusic.getConfig().defaultApi);
    }

    /**
     * 获取某个音乐API
     * @param api 音乐API
     * @return 音乐API
     */
    public static IMusicApi getApiMusic(String api) {
        return AllMusic.MUSIC_APIS.get(api);
    }

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
     * 获取玩家Hud信息，这个操作会读取数据库，首次读取会有一点卡
     *
     * @param player 玩家
     * @return Hud信息
     */
    public static HudPosObj getHud(String player) {
        return HudUtils.get(player);
    }

    /**
     * 设置玩家Hud
     *
     * @param player 玩家
     * @param hud    Hud信息
     */
    public static void setHud(String player, HudPosObj hud) {
        HudUtils.set(player, hud);
    }
}
