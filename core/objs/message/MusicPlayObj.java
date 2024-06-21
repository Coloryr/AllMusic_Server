package com.coloryr.allmusic.server.core.objs.message;

public class MusicPlayObj {
    public String stopPlaying;
    public String emptyPlayingMusic;
    public String nowPlay;
    public String emptyPlay;
    public ListMusicObj listMusic;
    public String emptyCanPlay;
    public String mute;
    public String switchMusic;
    public String addMusic;
    public String checkMusic;
    public String musicInfo;
    public String trail;
    public String cancel;
    public String error1;

    public static MusicPlayObj make() {
        MusicPlayObj obj = new MusicPlayObj();
        obj.init();

        return obj;
    }

    public boolean check() {
        boolean res = stopPlaying == null;
        if (emptyPlayingMusic == null)
            res = true;
        if (nowPlay == null)
            res = true;
        if (emptyPlay == null)
            res = true;
        if (listMusic == null || listMusic.check())
            res = true;
        if (emptyCanPlay == null)
            res = true;
        if (mute == null)
            res = true;
        if (switchMusic == null)
            res = true;
        if (addMusic == null)
            res = true;
        if (checkMusic == null)
            res = true;
        if (musicInfo == null)
            res = true;
        if (trail == null)
            res = true;
        if (cancel == null)
            res = true;
        if (error1 == null)
            res = true;

        return res;
    }

    public void init() {
        stopPlaying = "§d[AllMusic3]§e已停止你的音乐播放";
        emptyPlayingMusic = "§d[AllMusic3]§e无正在播放的歌曲";
        nowPlay = "§d[AllMusic3]§e正在播放：" + PAL.musicName + " | " + PAL.musicAuthor
                + " by: " + PAL.player;
        emptyPlay = "§d[AllMusic3]§e队列中无歌曲";
        listMusic = ListMusicObj.make();
        emptyCanPlay = "§d[AllMusic3]§c无法播放歌曲" + PAL.musicId + "可能该歌曲为VIP歌曲";
        mute = "§d[AllMusic3]§e你不会再收到点歌了！想要再次参与点歌就点一首歌吧！";
        switchMusic = "§d[AllMusic3]§e切换到玩家歌曲";
        addMusic = "§d[AllMusic3]§e音乐列表添加" + PAL.musicName + " | " + PAL.musicAuthor
                + " | " + PAL.musicAl + " | " + PAL.musicAlia;
        checkMusic = "§d[AllMusic3]§e正在解析歌曲" + PAL.musicId;
        musicInfo = PAL.musicName + " | " + PAL.musicAuthor + " | " + PAL.musicAl
                + " | " + PAL.musicAlia + " | by: " + PAL.player;
        trail = "§d[AllMusic3]§e该音乐为试听音乐";
        cancel = "§d[AllMusic3]§e播放被取消";
        error1 = "§d[AllMusic3]§c未知音乐类型";
    }
}
