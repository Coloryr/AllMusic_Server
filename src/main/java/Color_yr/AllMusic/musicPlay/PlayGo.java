package Color_yr.AllMusic.musicPlay;

import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.musicAPI.songLyric.LyricSave;
import Color_yr.AllMusic.musicAPI.songLyric.ShowOBJ;
import Color_yr.AllMusic.musicPlay.sendHud.HudUtils;

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
        PlayMusic.MusicLessTime = 0;
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
        if (PlayMusic.Lyric.isHaveLyric()) {
            service1 = Executors.newSingleThreadScheduledExecutor();
            service1.scheduleAtFixedRate(PlayGo::time2, 0, 2, TimeUnit.MILLISECONDS);
        }
    }

    public static void clear() {
        PlayMusic.MusicNowTime = 0;
        PlayMusic.MusicAllTime = 0;
        PlayMusic.MusicLessTime = 0;
        PlayMusic.Lyric = null;
        PlayMusic.nowLyric = null;
        PlayMusic.NowPlayMusic = null;
        closeTimer();
        HudUtils.clearHud();
    }

    private static void time1() {
        PlayMusic.MusicNowTime += 10;
        count++;
        if (count == 100) {
            PlayMusic.MusicLessTime--;
            count = 0;
        }
    }

    private static void time2() {
        ShowOBJ show = PlayMusic.Lyric.checkTime(PlayMusic.MusicNowTime);
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
                    if (AllMusic.Side.NeedPlay()) {
                        String ID = AllMusic.getMusicApi().getListMusic();
                        if (ID != null) {
                            MusicObj obj = new MusicObj();
                            obj.sender = ID;
                            obj.Name = "空闲列表";
                            obj.isDefault = true;
                            PlayMusic.addMusic(ID, "空闲列表", true);
                        }
                    }
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                AllMusic.Side.sendHudSaveAll();
                PlayMusic.NowPlayMusic = PlayMusic.getMusic(0);
                PlayMusic.remove(0);

                url = PlayMusic.NowPlayMusic.getPlayerUrl() == null ?
                        AllMusic.getMusicApi().getPlayUrl(PlayMusic.NowPlayMusic.getID()) :
                        PlayMusic.NowPlayMusic.getPlayerUrl();
                if (url == null) {
                    String data = AllMusic.getMessage().getMusicPlay().getNoCanPlay();
                    AllMusic.Side.bqt(data.replace("%MusicID%", PlayMusic.NowPlayMusic.getID()));
                    continue;
                }

                if (PlayMusic.NowPlayMusic.getPlayerUrl() == null)
                    PlayMusic.Lyric = AllMusic.getMusicApi().getLyric(PlayMusic.NowPlayMusic.getID());
                else
                    PlayMusic.Lyric = new LyricSave();

                if (PlayMusic.NowPlayMusic.getLength() != 0) {
                    PlayMusic.MusicAllTime = PlayMusic.MusicLessTime = (PlayMusic.NowPlayMusic.getLength() / 1000) + 3;
                    String info = AllMusic.getMessage().getMusicPlay().getPlay();
                    info = info.replace("%MusicName%", PlayMusic.NowPlayMusic.getName())
                            .replace("%MusicAuthor%", PlayMusic.NowPlayMusic.getAuthor())
                            .replace("%MusicAl%", PlayMusic.NowPlayMusic.getAl())
                            .replace("%MusicAlia%", PlayMusic.NowPlayMusic.getAlia())
                            .replace("%PlayerName%", PlayMusic.NowPlayMusic.getCall());
                    AllMusic.Side.bqt(info);
                    startTimer();
                    AllMusic.Side.send("[Play]" + url, true);
                    AllMusic.Side.task(() ->
                            AllMusic.Side.send("[Img]" + PlayMusic.NowPlayMusic.getPicUrl(), true), 10);
                    if (PlayMusic.NowPlayMusic.isTrial()) {
                        AllMusic.Side.bqt(AllMusic.getMessage().getMusicPlay().getTrail());
                        PlayMusic.MusicLessTime = PlayMusic.NowPlayMusic.getTrialInfo().getEnd();
                        PlayMusic.MusicNowTime = PlayMusic.NowPlayMusic.getTrialInfo().getStart();
                    }
                    try {
                        while (PlayMusic.MusicLessTime > 0) {
                            HudUtils.sendHudNowData();
                            HudUtils.sendHudListData();
                            if (!AllMusic.Side.NeedPlay()) {
                                PlayMusic.MusicLessTime = 1;
                            }
                            if (PlayMusic.VoteTime > 0) {
                                PlayMusic.VoteTime--;
                                if (PlayMusic.VoteTime == 0) {
                                    AllMusic.clearVote();
                                    AllMusic.Side.bqt(AllMusic.getMessage().getVote().getTimeOut());
                                } else {
                                    int players = AllMusic.Side.getAllPlayer();
                                    if (AllMusic.getVoteCount() >= AllMusic.getConfig().getMinVote()
                                            || (players <= AllMusic.getConfig().getMinVote()
                                            && players <= AllMusic.getVoteCount())) {
                                        AllMusic.Side.bqt(AllMusic.getMessage().getVote().getDo());
                                        AllMusic.Side.send("[Stop]", false);
                                        AllMusic.clearVote();
                                        PlayMusic.VoteTime = 0;
                                        break;
                                    }
                                }
                            }
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        AllMusic.log.warning("§c歌曲播放出现错误");
                        e.printStackTrace();
                    }
                } else {
                    String data = AllMusic.getMessage().getMusicPlay().getNoCanPlay();
                    AllMusic.Side.bqt(data.replace("%MusicID%", PlayMusic.NowPlayMusic.getID()));
                }
                clear();
            }
        }
    }
}
