package com.coloryr.allmusic.server.core.objs.config;

import com.coloryr.allmusic.server.core.AllMusic;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * 配置文件对象
 */
public class ConfigObj {
    /**
     * 最大歌曲数
     */
    public int maxPlayList;
    /**
     * 一个玩家最大可点数量
     */
    public int maxPlayerList;
    /**
     * 最小通过投票数
     */
    public int minVote;
    /**
     * 投票时间
     */
    public int voteTime;
    /**
     * 歌曲延迟
     */
    public int lyricDelay;
    /**
     * 默认添加歌曲方式
     */
    public int defaultAddMusic;
    /**
     * KTV模式歌词加时
     */
    public int ktvLyricDelay;
    /**
     * 管理员ID列表
     */
    public Set<String> adminList;
    /**
     * 不参与点歌的服务器列表
     */
    public Set<String> muteServer;
    /**
     * 不参与点歌的玩家列表
     */
    public Set<String> mutePlayer;
    /**
     * 禁止点歌ID列表
     */
    public Set<String> banMusic;
    /**
     * 禁止玩家点歌列表
     */
    public Set<String> banPlayer;
    /**
     * 玩家点歌后是否直接从空闲歌单切换至玩家歌曲
     */
    public boolean playListSwitch;
    /**
     * 空闲歌单随机播放
     */
    public boolean playListRandom;
    /**
     * 多少首歌不重复
     */
    public int playListEscapeDeep;
    /**
     * 显示歌词
     */
    public boolean sendLyric;
    /**
     * 指令需要权限
     */
    public boolean needPermission;
    /**
     * 顶层模式，用于和BC交换数据
     */
    public boolean topPAPI;
    /**
     * 不发送播放信息
     */
    public boolean mutePlayMessage;
    /**
     * 不发送点歌信息
     */
    public boolean muteAddMessage;
    /**
     * 将信息显示在bar处
     */
    public boolean showInBar;
    /**
     * K歌歌词
     */
    public boolean ktvMode;
    /**
     * 歌曲音质
     */
    public String musicBR;
    /**
     * 配置文件版本号
     */
    public String version;
    /**
     * 默认的Hud配置
     */
    public SaveObj defaultHud;
    /**
     * 经济插件挂钩
     */
    public EconomyObj economy;
    /**
     * 娱乐选项
     */
    public FunConfigObj funConfig;
    /**
     * 限制设置
     */
    public LimitObj limit;
    /**
     * 花费点歌
     */
    public CostObj cost;
    /**
     * 信息更新延迟
     */
    public int sendDelay;

    public static ConfigObj make() {
        ConfigObj config = new ConfigObj();
        config.init();

        return config;
    }

    public void addBanMusic(String id) {
        banMusic.add(id);
        AllMusic.saveConfig();
    }

    public void addBanPlayer(String name) {
        name = name.toLowerCase(Locale.ROOT);
        banPlayer.add(name);
        AllMusic.saveConfig();
    }

    public void addNoMusicPlayer(String name) {
        name = name.toLowerCase(Locale.ROOT);
        mutePlayer.add(name);
        AllMusic.saveConfig();
    }

    public void removeNoMusicPlayer(String name) {
        name = name.toLowerCase(Locale.ROOT);
        mutePlayer.remove(name);
        AllMusic.saveConfig();
    }

    public boolean check() {
        boolean saveConfig = false;
        if (adminList == null) {
            saveConfig = true;
            adminList = new HashSet<>();
        }
        if (banMusic == null) {
            saveConfig = true;
            banMusic = new HashSet<>();
        }
        if (banPlayer == null) {
            saveConfig = true;
            banPlayer = new HashSet<>();
        }
        if (defaultHud == null) {
            saveConfig = true;
            defaultHud = SaveObj.make();
        }
        if (mutePlayer == null) {
            saveConfig = true;
            mutePlayer = new HashSet<>();
        }
        if (muteServer == null) {
            saveConfig = true;
            muteServer = new HashSet<>();
        }
        if (economy == null || economy.check()) {
            saveConfig = true;
            economy = EconomyObj.make();
        }
        if (funConfig == null) {
            saveConfig = true;
            funConfig = FunConfigObj.make();
        }
        if (limit == null) {
            saveConfig = true;
            limit = LimitObj.make();
        }
        if (cost == null) {
            saveConfig = true;
            cost = CostObj.make();
        }

        return saveConfig;
    }

    public void init() {
        maxPlayerList = 0;
        maxPlayList = 10;
        minVote = 3;
        voteTime = 30;
        adminList = new HashSet<>();
        adminList.add("color_yr");
        muteServer = new HashSet<>();
        mutePlayer = new HashSet<>();
        banMusic = new HashSet<>();
        banPlayer = new HashSet<>();
        playListSwitch = true;
        playListRandom = true;
        playListEscapeDeep = 40;
        sendLyric = true;
        needPermission = false;
        defaultHud = SaveObj.make();
        mutePlayMessage = false;
        muteAddMessage = false;
        showInBar = false;
        defaultAddMusic = 0;
        musicBR = "320000";
        topPAPI = false;
        limit = LimitObj.make();
        economy = EconomyObj.make();
        funConfig = FunConfigObj.make();
        cost = CostObj.make();
        ktvMode = true;
        ktvLyricDelay = 0;
        lyricDelay = 0;
        sendDelay = 1000;
        version = AllMusic.configVersion;
    }
}