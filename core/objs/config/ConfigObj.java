package com.coloryr.allmusic.server.core.objs.config;

import com.coloryr.allmusic.server.core.AllMusic;

import java.util.HashSet;
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
     * 搜歌花费
     */
    public int searchCost;
    /**
     * 点歌花费
     */
    public int addMusicCost;
    /**
     * 默认添加歌曲方式
     */
    public int defaultAddMusic;
    /**
     * 消息限制长度
     */
    public int messageLimitSize;
    /**
     * 最长音乐长度
     */
    public int maxMusicTime;
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
     * 显示歌词
     */
    public boolean sendLyric;
    /**
     * 指令需要权限
     */
    public boolean needPermission;
    /**
     * 开启花钱点歌
     */
    public boolean useCost;
    /**
     * 顶层模式，用于和BC交换数据
     */
    public boolean topPAPI;
    /**
     * 开启信息长度限制
     */
    public boolean messageLimit;
    /**
     * 不发送播放信息
     */
    public boolean mutePlayMessage;
    /**
     * 不发送点歌信息
     */
    public boolean muteAddMessage;
    /**
     * 将信息限制在bar处
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

    public void addBanPlayer(String id) {
        banPlayer.add(id);
        AllMusic.saveConfig();
    }

    public void AddNoMusicPlayer(String ID) {
        mutePlayer.add(ID);
        AllMusic.saveConfig();
    }

    public void RemoveNoMusicPlayer(String ID) {
        mutePlayer.remove(ID);
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

        return saveConfig;
    }

    public void init() {
        maxPlayerList = 0;
        maxPlayList = 10;
        minVote = 3;
        voteTime = 30;
        adminList = new HashSet<>();
        adminList.add("console");
        adminList.add("color_yr");
        muteServer = new HashSet<>();
        mutePlayer = new HashSet<>();
        banMusic = new HashSet<>();
        banPlayer = new HashSet<>();
        playListSwitch = true;
        playListRandom = true;
        sendLyric = true;
        needPermission = false;
        defaultHud = SaveObj.make();
        useCost = false;
        searchCost = 20;
        mutePlayMessage = false;
        muteAddMessage = false;
        showInBar = false;
        addMusicCost = 10;
        defaultAddMusic = 0;
        musicBR = "320000";
        maxMusicTime = 600;
        topPAPI = false;
        messageLimit = false;
        messageLimitSize = 40;
        economy = EconomyObj.make();
        funConfig = FunConfigObj.make();
        ktvMode = true;
        ktvLyricDelay = 0;
        lyricDelay = 0;
        sendDelay = 1000;
        version = AllMusic.configVersion;
    }
}