package com.coloryr.allmusic.server.core.music.play;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.config.LimitObj;
import com.coloryr.allmusic.server.core.objs.message.PAL;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.utils.HudUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlayGo {
    /**
     * 播放链接
     */
    public static String url;
    /**
     * 倒数计数器
     */
    private static int count = 0;
    private static int ping = 0;
    private static boolean isRun;
    /**
     * 歌曲更新计数器
     */
    private static int times = 0;
    /**
     * 歌曲定时器
     */
    private static ScheduledExecutorService service;
    /**
     * 歌词定时器
     */
    private static ScheduledExecutorService service1;
    /**
     * 事务定时器
     */
    private static ScheduledExecutorService service2;

    /**
     * 启动歌曲工作
     */
    public static void start() {
        Thread taskT = new Thread(PlayGo::task, "AllMusic_Play");
        isRun = true;
        taskT.start();

        service2 = Executors.newSingleThreadScheduledExecutor();
        service2.scheduleAtFixedRate(PlayGo::time3, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * 停止歌曲工作
     */
    public static void stop() {
        closeTimer();
        if (service2 != null) {
            service2.shutdown();
            service2 = null;
        }
        isRun = false;
        PlayMusic.musicLessTime = 0;
    }

    private static void closeTimer() {
        if (service != null) {
            service.shutdown();
            service = null;
        }
        if (service1 != null) {
            service1.shutdown();
            service1 = null;
        }
    }

    private static void startTimer() {
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(PlayGo::time1, 0, 10, TimeUnit.MILLISECONDS);
        if (PlayMusic.lyric != null && PlayMusic.lyric.isHaveLyric()) {
            service1 = Executors.newSingleThreadScheduledExecutor();
            service1.scheduleAtFixedRate(PlayGo::time2, 0, 2, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 清空歌曲数据
     */
    private static void clear() {
        closeTimer();
        PlayMusic.musicNowTime = 0;
        PlayMusic.musicAllTime = 0;
        PlayMusic.musicLessTime = 0;
        PlayMusic.lyric = null;
        PlayMusic.nowPlayMusic = null;
        AllMusic.side.updateInfo();
        HudUtils.clearHud();
    }

    /**
     * 歌曲时间定时器
     */
    private static void time1() {
        PlayMusic.musicNowTime += 10;
        count++;
        if (count == 100) {
            PlayMusic.musicLessTime--;
            count = 0;
        }
    }

    /**
     * 歌词更新
     */
    private static void time2() {
        try {
            if (PlayMusic.lyric == null)
                return;
            boolean res = PlayMusic.lyric
                    .checkTime(PlayMusic.musicNowTime, AllMusic.getConfig().ktvMode);
            if (res) {
                times = 0;
                HudUtils.sendHudLyricData();
                AllMusic.side.updateLyric();
            } else {
                times++;
                if (times == AllMusic.getConfig().sendDelay / 2 && PlayMusic.lyric != null) {
                    times = 0;
                    HudUtils.sendHudLyricData();
                    AllMusic.side.updateLyric();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkPush() {
        if (PlayMusic.push != null) {
            SongInfoObj push = PlayMusic.push;
            if (PlayMusic.nowPlayMusic.getID().equalsIgnoreCase(push.getID())) {
                PlayMusic.voteTime = 0;
                AllMusic.side.bqTask(AllMusic.getMessage().push.cancel);
                return false;
            }
            List<SongInfoObj> list = PlayMusic.getList();
            if (list.isEmpty()) {
                return false;
            }
            SongInfoObj id1 = list.get(0);
            if (id1 != null && id1.getID().equalsIgnoreCase(push.getID())) {
                return false;
            }
            for (int a = 1; a < list.size(); a++) {
                id1 = list.get(a);
                if (id1.getID().equalsIgnoreCase(push.getID()))
                    return true;
            }
        }

        return false;
    }

    /**
     * 事务定时器
     */
    private static void time3() {
        ping++;
        if (ping >= 10) {
            AllMusic.side.ping();
        }
        if (PlayMusic.pushTime > 0) {
            if (!checkPush()) {
                PlayMusic.push = null;
                PlayMusic.pushTime = 0;
                AllMusic.side.bqTask(AllMusic.getMessage().push.cancel);
            } else {
                PlayMusic.pushTime--;
                if (PlayMusic.pushTime == 0) {
                    PlayMusic.push = null;
                    AllMusic.clearPush();
                    AllMusic.side.bqTask(AllMusic.getMessage().push.timeOut);
                } else {
                    int players = AllMusic.side.getPlayerSize();
                    if (AllMusic.getVoteCount() >= AllMusic.getConfig().minVote
                            || (players <= AllMusic.getConfig().minVote
                            && players <= AllMusic.getVoteCount())) {
                        SongInfoObj info = PlayMusic.push;
                        PlayMusic.push = null;
                        PlayMusic.pushMusic(info);
                        AllMusic.side.bqTask(AllMusic.getMessage().push.doPush);
                        AllMusic.clearPush();
                        PlayMusic.pushTime = 0;
                    }
                }
            }
        }
        if (PlayMusic.voteTime > 0) {
            PlayMusic.voteTime--;
            if (PlayMusic.voteTime == 0) {
                AllMusic.clearPush();
                AllMusic.side.bqTask(AllMusic.getMessage().vote.timeOut);
            } else {
                int players = AllMusic.side.getPlayerSize();
                if (AllMusic.getVoteCount() >= AllMusic.getConfig().minVote
                        || (players <= AllMusic.getConfig().minVote
                        && players <= AllMusic.getVoteCount())) {
                    AllMusic.side.bqTask(AllMusic.getMessage().vote.voteDone);
                    AllMusic.clearVote();
                    PlayMusic.musicLessTime = 0;
                    PlayMusic.voteTime = 0;
                }
            }
        }
    }

    private static void task() {
        while (isRun) {
            try {
                if (PlayMusic.getListSize() == 0) {
                    if (PlayMusic.error >= 10) {
                        Thread.sleep(1000);
                    } else {
                        HudUtils.sendHudNowData();
                        HudUtils.sendHudLyricData();
                        HudUtils.sendHudListData();
                        if (AllMusic.side.needPlay()) {
                            String ID = PlayMusic.getIdleMusic();
                            if (ID != null) {
                                PlayMusic.addMusic(null, ID, AllMusic.getMessage().custom.idle, true);
                            }
                        }
                    }
                    Thread.sleep(50);
                } else {
                    HudUtils.sendHudNowData();
                    HudUtils.sendHudLyricData();
                    HudUtils.sendHudListData();

                    AllMusic.side.sendHudUtilsAll();
                    PlayMusic.nowPlayMusic = PlayMusic.remove(0);
                    if (AllMusic.side.onMusicPlay(PlayMusic.nowPlayMusic)) {
                        AllMusic.side.bqTask(AllMusic.getMessage().musicPlay.cancel);
                        continue;
                    }

                    url = PlayMusic.nowPlayMusic.getPlayerUrl() == null ?
                            AllMusic.getMusicApi().getPlayUrl(PlayMusic.nowPlayMusic.getID()) :
                            PlayMusic.nowPlayMusic.getPlayerUrl();
                    if (url == null) {
                        String data = AllMusic.getMessage().musicPlay.emptyCanPlay;
                        AllMusic.side.bqTask(data.replace(PAL.musicId, PlayMusic.nowPlayMusic.getID()));
                        PlayMusic.nowPlayMusic = null;
                        continue;
                    }

                    if (PlayMusic.nowPlayMusic.getPlayerUrl() == null)
                        PlayMusic.lyric = AllMusic.getMusicApi().getLyric(PlayMusic.nowPlayMusic.getID());
                    else
                        PlayMusic.lyric = new LyricSave();

                    if (PlayMusic.nowPlayMusic.getLength() != 0) {
                        PlayMusic.musicAllTime = PlayMusic.musicLessTime = (PlayMusic.nowPlayMusic.getLength() / 1000) + 3;
                        startTimer();
                        AllMusic.side.sendMusic(url);
                        if (!AllMusic.getConfig().mutePlayMessage) {
                            String info = getInfo();
                            if (AllMusic.getConfig().showInBar)
                                AllMusic.side.sendBar(info);
                            else
                                AllMusic.side.bqTask(info);
                        }
                        if (!PlayMusic.nowPlayMusic.isUrl() && PlayMusic.nowPlayMusic.getPicUrl() != null) {
                            AllMusic.side.sendPic(PlayMusic.nowPlayMusic.getPicUrl());
                        }
                        if (PlayMusic.nowPlayMusic.isTrial()) {
                            AllMusic.side.bqTask(AllMusic.getMessage().musicPlay.trail);
                            PlayMusic.musicLessTime = PlayMusic.nowPlayMusic.getTrialInfo().getEnd();
                            PlayMusic.musicNowTime = PlayMusic.nowPlayMusic.getTrialInfo().getStart();
                        }

                        AllMusic.side.updateInfo();

                        while (PlayMusic.musicLessTime > 0) {
                            HudUtils.sendHudNowData();
                            HudUtils.sendHudListData();
                            if (!AllMusic.side.needPlay()) {
                                PlayMusic.musicLessTime = 1;
                            }
                            Thread.sleep(AllMusic.getConfig().sendDelay);
                        }
                        AllMusic.side.sendStop();
                    } else {
                        String data = AllMusic.getMessage().musicPlay.emptyCanPlay;
                        AllMusic.side.bqTask(data.replace(PAL.musicId, PlayMusic.nowPlayMusic.getID()));
                    }
                    clear();
                }
            } catch (Exception e) {
                AllMusic.log.warning("§c歌曲播放出现错误");
                e.printStackTrace();
            }
        }
    }

    private static @NotNull String getInfo() {
        String info = AllMusic.getMessage().musicPlay.nowPlay
                .replace(PAL.musicName, PlayMusic.nowPlayMusic.getName())
                .replace(PAL.musicAuthor, PlayMusic.nowPlayMusic.getAuthor())
                .replace(PAL.musicAl, PlayMusic.nowPlayMusic.getAl())
                .replace(PAL.musicAlia, PlayMusic.nowPlayMusic.getAlia())
                .replace(PAL.player, PlayMusic.nowPlayMusic.getCall());
        LimitObj limit = AllMusic.getConfig().limit;
        if (limit.messageLimit
                && info.length() > limit.messageLimitSize) {
            info = info.substring(0, limit.messageLimitSize) + limit.limitText;
        }
        return info;
    }
}
