package com.coloryr.allmusic.server.core.objs.message;

public class MusicPlayObj {
    public String stopPlaying;
    public String emptyPlayingMusic;
    public String nowPlay;
    public String emptyPlay;
    public ListMusicObj listMusic;
    public String emptyCanPlay;
    public String mute;
    public String mute1;
    public String mute2;
    public String mute3;
    public String switchMusic;
    public String addMusic;
    public String checkMusic;
    public String musicInfo;
    public String trail;
    public String cancel;
    public String error1;
    public String error2;

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
        if (mute1 == null)
            res = true;
        if (mute2 == null)
            res = true;
        if (mute3 == null)
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
        if (error2 == null)
            res = true;

        return res;
    }

    public void init() {
        if (stopPlaying == null)
            stopPlaying = "<light_purple>[AllMusic3]<yellow>已停止你的音乐播放";
        if (emptyPlayingMusic == null)
            emptyPlayingMusic = "<light_purple>[AllMusic3]<yellow>无正在播放的歌曲";
        if (nowPlay == null)
            nowPlay = "<light_purple>[AllMusic3]<yellow>正在播放：" + ARG.musicName + " | " + ARG.musicAuthor
                    + " by: " + ARG.player;
        if (emptyPlay == null)
            emptyPlay = "<light_purple>[AllMusic3]<yellow>队列中无歌曲";
        if (listMusic == null)
            listMusic = ListMusicObj.make();
        if (emptyCanPlay == null)
            emptyCanPlay = "<light_purple>[AllMusic3]<red>无法播放歌曲" + ARG.musicId + "可能该歌曲为VIP歌曲";
        if (mute == null)
            mute = "<light_purple>[AllMusic3]<yellow>你不会再收到点歌了！想要再次参与点歌就点一首歌吧！或者再输入一次指令恢复";
        if (mute1 == null)
            mute1 = "<light_purple>[AllMusic3]<yellow>你不会再收到空闲歌单点歌了，再输入一次指令恢复";
        if (mute2 == null)
            mute2 = "<light_purple>[AllMusic3]<yellow>你将开始收到空闲歌单点歌";
        if (mute3 == null)
            mute3 = "<light_purple>[AllMusic3]<yellow>你将开始收到点歌";
        if (switchMusic == null)
            switchMusic = "<light_purple>[AllMusic3]<yellow>切换到玩家歌曲";
        if (addMusic == null)
            addMusic = "<light_purple>[AllMusic3]<yellow>音乐列表添加" + ARG.musicName + " | " + ARG.musicAuthor
                    + " | " + ARG.musicAl + " | " + ARG.musicAlia + " | by: " + ARG.player;
        if (checkMusic == null)
            checkMusic = "<light_purple>[AllMusic3]<yellow>正在解析歌曲" + ARG.musicId;
        if (musicInfo == null)
            musicInfo = ARG.musicName + " | " + ARG.musicAuthor + " | " + ARG.musicAl
                    + " | " + ARG.musicAlia + " | by: " + ARG.player;
        if (trail == null)
            trail = "<light_purple>[AllMusic3]<yellow>该音乐为试听音乐";
        if (cancel == null)
            cancel = "<light_purple>[AllMusic3]<yellow>播放被取消";
        if (error1 == null)
            error1 = "<light_purple>[AllMusic3]<red>未知音乐类型";
        if (error2 == null)
            error2 = "<light_purple>[AllMusic3]<red>未知音乐API";
    }
}
