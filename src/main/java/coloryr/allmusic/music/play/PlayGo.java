package coloryr.allmusic.music.play;

import coloryr.allmusic.AllMusic;
import coloryr.allmusic.hud.HudUtils;
import coloryr.allmusic.music.lyric.LyricSave;
import coloryr.allmusic.music.lyric.ShowOBJ;

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

    public static void start() {
        Thread taskT = new Thread(PlayGo::task, "AllMusic_Play");
        isRun = true;
        taskT.start();
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
        PlayMusic.nowLyric = null;
        PlayMusic.nowPlayMusic = null;
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
        ShowOBJ show = PlayMusic.lyric.checkTime(PlayMusic.musicNowTime);
        if (show != null) {
            PlayMusic.nowLyric = show;
            times = 0;
            HudUtils.sendHudLyricData(show);
        } else {
            times++;
            if (times == 500 && PlayMusic.nowLyric != null) {
                times = 0;
                HudUtils.sendHudLyricData(PlayMusic.nowLyric);
            }
        }
    }

    private static void task() {
        while (isRun) {
            if (PlayMusic.getSize() == 0) {
                try {
                    HudUtils.sendHudNowData();
                    HudUtils.sendHudLyricData(null);
                    HudUtils.sendHudListData();
                    if (AllMusic.side.NeedPlay()) {
                        String ID = AllMusic.getMusicApi().getListMusic();
                        if (ID != null) {
                            MusicObj obj = new MusicObj();
                            obj.sender = ID;
                            obj.name = "空闲列表";
                            obj.isDefault = true;
                            PlayMusic.addMusic(ID, "空闲列表", true);
                        }
                    }
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                AllMusic.side.sendHudSaveAll();
                PlayMusic.nowPlayMusic = PlayMusic.getMusic(0);
                PlayMusic.remove(0);

                url = PlayMusic.nowPlayMusic.getPlayerUrl() == null ?
                        AllMusic.getMusicApi().getPlayUrl(PlayMusic.nowPlayMusic.getID()) :
                        PlayMusic.nowPlayMusic.getPlayerUrl();
                if (url == null) {
                    String data = AllMusic.getMessage().getMusicPlay().getNoCanPlay();
                    AllMusic.side.bqt(data.replace("%MusicID%", PlayMusic.nowPlayMusic.getID()));
                    continue;
                }

                if (PlayMusic.nowPlayMusic.getPlayerUrl() == null)
                    PlayMusic.lyric = AllMusic.getMusicApi().getLyric(PlayMusic.nowPlayMusic.getID());
                else
                    PlayMusic.lyric = new LyricSave();

                if (PlayMusic.nowPlayMusic.getLength() != 0) {
                    PlayMusic.musicAllTime = PlayMusic.musicLessTime = (PlayMusic.nowPlayMusic.getLength() / 1000) + 3;
                    String info = AllMusic.getMessage().getMusicPlay().getPlay();
                    info = info.replace("%MusicName%", PlayMusic.nowPlayMusic.getName())
                            .replace("%MusicAuthor%", PlayMusic.nowPlayMusic.getAuthor())
                            .replace("%MusicAl%", PlayMusic.nowPlayMusic.getAl())
                            .replace("%MusicAlia%", PlayMusic.nowPlayMusic.getAlia())
                            .replace("%PlayerName%", PlayMusic.nowPlayMusic.getCall());
                    AllMusic.side.bqt(info);
                    startTimer();
                    AllMusic.side.send("[Play]" + url, true);
                    AllMusic.side.task(() ->
                            AllMusic.side.send("[Img]" + PlayMusic.nowPlayMusic.getPicUrl(), true), 10);
                    if (PlayMusic.nowPlayMusic.isTrial()) {
                        AllMusic.side.bqt(AllMusic.getMessage().getMusicPlay().getTrail());
                        PlayMusic.musicLessTime = PlayMusic.nowPlayMusic.getTrialInfo().getEnd();
                        PlayMusic.musicNowTime = PlayMusic.nowPlayMusic.getTrialInfo().getStart();
                    }
                    try {
                        while (PlayMusic.musicLessTime > 0) {
                            HudUtils.sendHudNowData();
                            HudUtils.sendHudListData();
                            if (!AllMusic.side.NeedPlay()) {
                                PlayMusic.musicLessTime = 1;
                            }
                            if (PlayMusic.voteTime > 0) {
                                PlayMusic.voteTime--;
                                if (PlayMusic.voteTime == 0) {
                                    AllMusic.clearVote();
                                    AllMusic.side.bqt(AllMusic.getMessage().getVote().getTimeOut());
                                } else {
                                    int players = AllMusic.side.getAllPlayer();
                                    if (AllMusic.getVoteCount() >= AllMusic.getConfig().getMinVote()
                                            || (players <= AllMusic.getConfig().getMinVote()
                                            && players <= AllMusic.getVoteCount())) {
                                        AllMusic.side.bqt(AllMusic.getMessage().getVote().getDo());
                                        AllMusic.clearVote();
                                        PlayMusic.voteTime = 0;
                                        break;
                                    }
                                }
                            }
                            Thread.sleep(1000);
                        }
                        AllMusic.side.send("[Stop]", false);
                    } catch (InterruptedException e) {
                        AllMusic.log.warning("§c歌曲播放出现错误");
                        e.printStackTrace();
                    }
                } else {
                    String data = AllMusic.getMessage().getMusicPlay().getNoCanPlay();
                    AllMusic.side.bqt(data.replace("%MusicID%", PlayMusic.nowPlayMusic.getID()));
                }
                clear();
            }
        }
    }
}
