package com.coloryr.allmusic.server.core.music.play;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.message.PAL;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.utils.HudUtils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlayRuntime {
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
        Thread taskT = new Thread(PlayRuntime::task, "AllMusicPlay");
        isRun = true;
        taskT.start();

        service2 = Executors.newSingleThreadScheduledExecutor();
        service2.scheduleAtFixedRate(PlayRuntime::time3, 0, 1, TimeUnit.SECONDS);
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
        service.scheduleAtFixedRate(PlayRuntime::time1, 0, 10, TimeUnit.MILLISECONDS);
        if (PlayMusic.lyric != null && PlayMusic.lyric.isHaveLyric()) {
            service1 = Executors.newSingleThreadScheduledExecutor();
            service1.scheduleAtFixedRate(PlayRuntime::time2, 0, 2, TimeUnit.MILLISECONDS);
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
        HudUtils.sendHudNowData();
        HudUtils.sendHudLyricData();
        HudUtils.sendHudListData();
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
        SongInfoObj music = PlayMusic.getPush();
        if (music != null) {
            if (PlayMusic.nowPlayMusic.getID().equalsIgnoreCase(music.getID())) {
                return false;
            }
            List<SongInfoObj> list = PlayMusic.getList();
            if (list.isEmpty()) {
                return false;
            }
            SongInfoObj id1 = list.get(0);
            if (id1 != null && id1.getID().equalsIgnoreCase(music.getID())) {
                return false;
            }
            for (int a = 1; a < list.size(); a++) {
                id1 = list.get(a);
                if (id1.getID().equalsIgnoreCase(music.getID()))
                    return true;
            }
        }

        return false;
    }

    /**
     * 事务定时器
     */
    private static void time3() {
        try {
            ping++;
            if (ping >= 10) {
                AllMusic.side.ping();
            }
            if (PlayMusic.getPushTime() > 0) {
                if (!checkPush()) {
                    PlayMusic.clearPush();
                    AllMusic.side.broadcastInTask(AllMusic.getMessage().push.cancel);
                } else {
                    PlayMusic.pushTick();
                    if (PlayMusic.getPushTime() == 0) {
                        PlayMusic.clearPush();
                        AllMusic.side.broadcastInTask(AllMusic.getMessage().push.timeOut);
                    } else {
                        int players = AllMusic.side.getPlayers().size();
                        if (PlayMusic.getPushCount() >= AllMusic.getConfig().minVote
                                || (players <= AllMusic.getConfig().minVote
                                && players <= PlayMusic.getPushCount())) {
                            PlayMusic.pushMusic();
                            PlayMusic.clearPush();
                            AllMusic.side.broadcastInTask(AllMusic.getMessage().push.doPush);
                        }
                    }
                }
            }

            if (PlayMusic.getVoteTime() > 0) {
                PlayMusic.voteTick();
                if (PlayMusic.getVoteTime() == 0) {
                    PlayMusic.clearVote();
                    AllMusic.side.broadcastInTask(AllMusic.getMessage().vote.timeOut);
                } else {
                    int players = AllMusic.side.getPlayers().size();
                    if (PlayMusic.getVoteCount() >= AllMusic.getConfig().minVote
                            || (players <= AllMusic.getConfig().minVote
                            && players <= PlayMusic.getVoteCount())) {
                        PlayMusic.musicLessTime = 0;
                        PlayMusic.clearVote();
                        AllMusic.side.broadcastInTask(AllMusic.getMessage().vote.voteDone);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void task() {
        while (isRun) {
            try {
                if (PlayMusic.getListSize() == 0) {
                    if (PlayMusic.error >= 10) {
                        Thread.sleep(1000);
                    } else if (AllMusic.side.needPlay(true) && PlayMusic.getIdleListSize() > 0) {
                        String id = PlayMusic.getIdleMusic();
                        if (id != null) {
                            PlayMusic.addMusic(null, id, AllMusic.getMessage().custom.idle, true);
                        }
                    }
                    Thread.sleep(1000);
                } else {
                    HudUtils.sendHudNowData();
                    HudUtils.sendHudLyricData();
                    HudUtils.sendHudListData();
                    AllMusic.side.sendHudUtilsAll();
                    PlayMusic.nowPlayMusic = PlayMusic.remove(0);
                    if (AllMusic.side.onMusicPlay(PlayMusic.nowPlayMusic)) {
                        AllMusic.side.broadcastInTask(AllMusic.getMessage().musicPlay.cancel);
                        continue;
                    }

                    PlayMusic.url = PlayMusic.nowPlayMusic.getPlayerUrl() == null ?
                            AllMusic.getMusicApi().getPlayUrl(PlayMusic.nowPlayMusic.getID()) :
                            PlayMusic.nowPlayMusic.getPlayerUrl();
                    if (PlayMusic.url == null) {
                        String data = AllMusic.getMessage().musicPlay.emptyCanPlay;
                        AllMusic.side.broadcastInTask(data.replace(PAL.musicId, PlayMusic.nowPlayMusic.getID()));
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
                        AllMusic.side.sendMusic(PlayMusic.url);
                        if (!AllMusic.getConfig().mutePlayMessage) {
                            SongInfoObj music = PlayMusic.nowPlayMusic;
                            if (AllMusic.getConfig().showInBar) {
                                String info = AllMusic.getMessage().musicPlay.nowPlay
                                        .replace(PAL.musicName, HudUtils.messageLimit(music.getName()))
                                        .replace(PAL.musicAuthor, HudUtils.messageLimit(music.getAuthor()))
                                        .replace(PAL.musicAl, HudUtils.messageLimit(music.getAl()))
                                        .replace(PAL.musicAlia, HudUtils.messageLimit(music.getAlia()))
                                        .replace(PAL.player, music.getCall());
                                AllMusic.side.sendBar(info);
                            } else {
                                String info = AllMusic.getMessage().musicPlay.nowPlay
                                        .replace(PAL.musicName, music.getName())
                                        .replace(PAL.musicAuthor, music.getAuthor())
                                        .replace(PAL.musicAl, music.getAl())
                                        .replace(PAL.musicAlia, music.getAlia())
                                        .replace(PAL.player, music.getCall());
                                AllMusic.side.broadcastInTask(info);
                            }
                        }
                        if (!PlayMusic.nowPlayMusic.isUrl() && PlayMusic.nowPlayMusic.getPicUrl() != null) {
                            AllMusic.side.sendPic(PlayMusic.nowPlayMusic.getPicUrl());
                        }
                        if (PlayMusic.nowPlayMusic.isTrial()) {
                            AllMusic.side.broadcastInTask(AllMusic.getMessage().musicPlay.trail);
                            PlayMusic.musicLessTime = PlayMusic.nowPlayMusic.getTrialInfo().getEnd();
                            PlayMusic.musicNowTime = PlayMusic.nowPlayMusic.getTrialInfo().getStart();
                        }

                        AllMusic.side.updateInfo();

                        while (PlayMusic.musicLessTime > 0) {
                            HudUtils.sendHudNowData();
                            HudUtils.sendHudListData();
                            if (PlayMusic.nowPlayMusic == null || !AllMusic.side.needPlay(PlayMusic.nowPlayMusic.isList())) {
                                PlayMusic.musicLessTime = 1;
                            }
                            Thread.sleep(AllMusic.getConfig().sendDelay);
                        }
                        AllMusic.side.sendStop();
                    } else {
                        String data = AllMusic.getMessage().musicPlay.emptyCanPlay;
                        AllMusic.side.broadcastInTask(data.replace(PAL.musicId, PlayMusic.nowPlayMusic.getID()));
                    }
                    clear();
                }
            } catch (Exception e) {
                AllMusic.log.warning("§c歌曲播放出现错误");
                e.printStackTrace();
            }
        }
    }
}
