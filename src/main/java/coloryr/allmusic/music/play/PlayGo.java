package coloryr.allmusic.music.play;

import coloryr.allmusic.AllMusic;
import coloryr.allmusic.hud.HudUtils;
import coloryr.allmusic.music.lyric.LyricItem;
import coloryr.allmusic.music.lyric.LyricSave;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlayGo {
    private static int count = 0;
    private static boolean isRun;
    private static int times = 0;
    public static String url;

    private static ScheduledExecutorService service;
    private static ScheduledExecutorService service1;
    private static ScheduledExecutorService service2;

    public static void start() {
        Thread taskT = new Thread(PlayGo::task, "AllMusic_Play");
        isRun = true;
        taskT.start();

        service2 = Executors.newSingleThreadScheduledExecutor();
        service2.scheduleAtFixedRate(PlayGo::time3, 0, 10, TimeUnit.SECONDS);
    }

    public static void stop() {
        closeTimer();
        isRun = false;
        PlayMusic.musicLessTime = 0;
    }

    public static void closeTimer() {
        if (service != null) {
            service.shutdown();
            service = null;
        }
        if (service1 != null) {
            service1.shutdown();
            service1 = null;
        }
        if (service2 != null) {
            service2.shutdown();
            service2 = null;
        }
    }

    private static void startTimer() {
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(PlayGo::time1, 0, 10, TimeUnit.MILLISECONDS);
        if (PlayMusic.lyric.isHaveLyric()) {
            service1 = Executors.newSingleThreadScheduledExecutor();
            service1.scheduleAtFixedRate(PlayGo::time2, 0, 2, TimeUnit.MILLISECONDS);
        }
    }

    public static void clear() {
        PlayMusic.musicNowTime = 0;
        PlayMusic.musicAllTime = 0;
        PlayMusic.musicLessTime = 0;
        PlayMusic.lyric = null;
        PlayMusic.lyricItem = null;
        PlayMusic.nowPlayMusic = null;
        AllMusic.side.updateInfo();
        closeTimer();
        HudUtils.clearHud();
    }

    private static void time1() {
        PlayMusic.musicNowTime += 10;
        count++;
        if (count == 100) {
            PlayMusic.musicLessTime--;
            count = 0;
        }
    }

    private static void time2() {
        LyricItem show = PlayMusic.lyric.checkTime(PlayMusic.musicNowTime);
        if (show != null) {
            PlayMusic.lyricItem = show;
            times = 0;
            HudUtils.sendHudLyricData(show);
            AllMusic.side.updateLyric();
        } else {
            times++;
            if (times == 500 && PlayMusic.lyricItem != null) {
                times = 0;
                HudUtils.sendHudLyricData(PlayMusic.lyricItem);
                AllMusic.side.updateLyric();
            }
        }
    }

    private static void time3() {
        AllMusic.side.ping();
    }

    private static void task() {
        while (isRun) {
            try {
                if (PlayMusic.getSize() == 0) {
                    HudUtils.sendHudNowData();
                    HudUtils.sendHudLyricData(null);
                    HudUtils.sendHudListData();
                    if (AllMusic.side.needPlay()) {
                        String ID = AllMusic.getMusicApi().ListMusic;
                        if (ID != null) {
                            MusicObj obj = new MusicObj();
                            obj.sender = ID;
                            obj.name = "空闲列表";
                            obj.isDefault = true;
                            PlayMusic.addMusic(null, ID, "空闲列表", true);
                        }
                    }
                    Thread.sleep(50);
                } else {
                    AllMusic.side.sendHudSaveAll();
                    PlayMusic.nowPlayMusic = PlayMusic.remove(0);
                    if (AllMusic.side.onMusicPlay(PlayMusic.nowPlayMusic)) {
                        AllMusic.side.bqt(AllMusic.getMessage().MusicPlay.getCancel());
                        continue;
                    }

                    url = PlayMusic.nowPlayMusic.getPlayerUrl() == null ?
                            AllMusic.getMusicApi().getPlayUrl(PlayMusic.nowPlayMusic.getID()) :
                            PlayMusic.nowPlayMusic.getPlayerUrl();
                    if (url == null) {
                        String data = AllMusic.getMessage().MusicPlay.getNoCanPlay();
                        AllMusic.side.bqt(data.replace("%MusicID%", PlayMusic.nowPlayMusic.getID()));
                        continue;
                    }

                    if (PlayMusic.nowPlayMusic.getPlayerUrl() == null)
                        PlayMusic.lyric = AllMusic.getMusicApi().getLyric(PlayMusic.nowPlayMusic.getID());
                    else
                        PlayMusic.lyric = new LyricSave();

                    if (PlayMusic.nowPlayMusic.getLength() != 0) {
                        PlayMusic.musicAllTime = PlayMusic.musicLessTime = (PlayMusic.nowPlayMusic.getLength() / 1000) + 3;
                        if (!AllMusic.getConfig().isMutePlayMessage()) {
                            String info = AllMusic.getMessage().MusicPlay.Play;
                            info = info.replace("%MusicName%", PlayMusic.nowPlayMusic.getName())
                                    .replace("%MusicAuthor%", PlayMusic.nowPlayMusic.getAuthor())
                                    .replace("%MusicAl%", PlayMusic.nowPlayMusic.getAl())
                                    .replace("%MusicAlia%", PlayMusic.nowPlayMusic.getAlia())
                                    .replace("%PlayerName%", PlayMusic.nowPlayMusic.getCall());
                            if (AllMusic.getConfig().isShowInBar())
                                AllMusic.side.sendBar(info);
                            else
                                AllMusic.side.bqt(info);
                        }
                        startTimer();
                        AllMusic.side.sendMusic(url);
                        if (!PlayMusic.nowPlayMusic.isUrl() && PlayMusic.nowPlayMusic.getPicUrl() != null) {
                            AllMusic.side.sendPic(PlayMusic.nowPlayMusic.getPicUrl());
                        }
                        if (PlayMusic.nowPlayMusic.isTrial()) {
                            AllMusic.side.bqt(AllMusic.getMessage().MusicPlay.getTrail());
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
                            if (PlayMusic.voteTime > 0) {
                                PlayMusic.voteTime--;
                                if (PlayMusic.voteTime == 0) {
                                    AllMusic.clearVote();
                                    AllMusic.side.bqt(AllMusic.getMessage().Vote.getTimeOut());
                                } else {
                                    int players = AllMusic.side.getAllPlayer();
                                    if (AllMusic.getVoteCount() >= AllMusic.getConfig().getMinVote()
                                            || (players <= AllMusic.getConfig().getMinVote()
                                            && players <= AllMusic.getVoteCount())) {
                                        AllMusic.side.bqt(AllMusic.getMessage().Vote.getDo());
                                        AllMusic.clearVote();
                                        PlayMusic.voteTime = 0;
                                        break;
                                    }
                                }
                            }
                            Thread.sleep(1000);
                        }
                        AllMusic.side.sendStop();
                    } else {
                        String data = AllMusic.getMessage().MusicPlay.getNoCanPlay();
                        AllMusic.side.bqt(data.replace("%MusicID%", PlayMusic.nowPlayMusic.getID()));
                    }
                    clear();
                }
            } catch (InterruptedException e) {
                AllMusic.log.warning("§c歌曲播放出现错误");
                e.printStackTrace();
            }
        }
    }
}
