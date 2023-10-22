package coloryr.allmusic.core.objs.config;

import coloryr.allmusic.core.AllMusic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 配置文件对象
 */
public class ConfigObj {
    /**
     * 最大歌曲数
     */
    public int MaxList;
    /**
     * 一个玩家最大可点数量
     */
    public int PlayerMaxList;
    /**
     * 最小通过投票数
     */
    public int MinVote;
    /**
     * 投票时间
     */
    public int VoteTime;
    /**
     * 歌曲延迟
     */
    public int Delay;
    /**
     * 搜歌花费
     */
    public int SearchCost;
    /**
     * 点歌花费
     */
    public int AddMusicCost;
    /**
     * 默认添加歌曲方式
     */
    public int DefaultAddMusic;
    /**
     * 消息限制长度
     */
    public int MessageLimitSize;
    /**
     * 最长音乐长度
     */
    public int MaxMusicTime;
    /**
     * KTV模式歌词加时
     */
    public int KDelay;

    /**
     * 管理员ID列表
     */
    public Set<String> Admin;
    /**
     * 不参与点歌的服务器列表
     */
    public Set<String> NoMusicServer;
    /**
     * 不参与点歌的玩家列表
     */
    public Set<String> NoMusicPlayer;
    /**
     * 禁止点歌ID列表
     */
    public Set<String> BanMusic;
    /**
     * 空闲歌单列表
     */
    public List<String> PlayList;
    /**
     * 禁止玩家点歌列表
     */
    public Set<String> BanPlayer;
    /**
     * 玩家点歌后是否直接从空闲歌单切换至玩家歌曲
     */
    public boolean PlayListSwitch;
    /**
     * 空闲歌单随机播放
     */
    public boolean PlayListRandom;
    /**
     * 显示歌词
     */
    public boolean SendLyric;
    /**
     * 指令需要权限
     */
    public boolean NeedPermission;
    /**
     * 开启花钱点歌
     */
    public boolean UseCost;
    /**
     * 顶层模式，用于和BC交换数据
     */
    public boolean TopPAPI;
    /**
     * 开启信息长度限制
     */
    public boolean MessageLimit;
    /**
     * 不发送播放信息
     */
    public boolean MutePlayMessage;
    /**
     * 不发送点歌信息
     */
    public boolean MuteAddMessage;
    /**
     * 将信息限制在bar处
     */
    public boolean ShowInBar;
    /**
     * K歌歌词
     */
    public boolean KtvMode;

    /**
     * 登录的用户
     */
    public String LoginUser;
    /**
     * 歌曲音质
     */
    public String MusicBR;
    /**
     * 配置文件版本号
     */
    public String Version;

    /**
     * 默认的Hud配置
     */
    public SaveObj DefaultHud;
    /**
     * 经济插件挂钩
     */
    public EconomyObj Economy;
    /**
     * 娱乐选项
     */
    public FunConfigObj FunConfig;

    public void addBanMusic(String id) {
        BanMusic.add(id);
        AllMusic.saveConfig();
    }

    public void addBanPlayer(String id) {
        BanPlayer.add(id);
        AllMusic.saveConfig();
    }

    public void AddNoMusicPlayer(String ID) {
        NoMusicPlayer.add(ID);
        AllMusic.saveConfig();
    }

    public void RemoveNoMusicPlayer(String ID) {
        NoMusicPlayer.remove(ID);
        AllMusic.saveConfig();
    }

    public boolean check() {
        boolean saveConfig = false;
        if (PlayList == null) {
            saveConfig = true;
            PlayList = new ArrayList<>();
        }
        if (Admin == null) {
            saveConfig = true;
            Admin = new HashSet<>();
        }
        if (BanMusic == null) {
            saveConfig = true;
            BanMusic = new HashSet<>();
        }
        if (BanPlayer == null) {
            saveConfig = true;
            BanPlayer = new HashSet<>();
        }
        if (DefaultHud == null) {
            saveConfig = true;
            DefaultHud = SaveObj.make();
        }
        if (NoMusicPlayer == null) {
            saveConfig = true;
            NoMusicPlayer = new HashSet<>();
        }
        if (NoMusicServer == null) {
            saveConfig = true;
            NoMusicServer = new HashSet<>();
        }
        if (Economy == null || Economy.check()) {
            saveConfig = true;
            Economy = EconomyObj.make();
        }
        if (FunConfig == null) {
            saveConfig = true;
            FunConfig = FunConfigObj.make();
        }

        return saveConfig;
    }

    public void init() {
        PlayerMaxList = 0;
        MaxList = 10;
        MinVote = 3;
        VoteTime = 30;
        Delay = 0;
        Admin = new HashSet<>();
        Admin.add("CONSOLE");
        Admin.add("Color_yr");
        NoMusicServer = new HashSet<>();
        NoMusicPlayer = new HashSet<>();
        BanMusic = new HashSet<>();
        PlayList = new ArrayList<>();
        BanPlayer = new HashSet<>();
        PlayListSwitch = true;
        PlayListRandom = true;
        SendLyric = true;
        NeedPermission = false;
        DefaultHud = SaveObj.make();

        UseCost = false;
        SearchCost = 20;
        MutePlayMessage = false;
        MuteAddMessage = false;
        ShowInBar = false;
        AddMusicCost = 10;

        DefaultAddMusic = 0;
        LoginUser = "";

        MusicBR = "320000";

        MaxMusicTime = 600;

        TopPAPI = false;
        MessageLimit = false;
        MessageLimitSize = 40;

        Economy = EconomyObj.make();
        FunConfig = FunConfigObj.make();

        KtvMode = true;
        KDelay = 0;

        Version = AllMusic.configVersion;
    }

    public static ConfigObj make() {
        ConfigObj config = new ConfigObj();
        config.init();

        return config;
    }
}