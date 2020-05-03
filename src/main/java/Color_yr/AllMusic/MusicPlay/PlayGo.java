package Color_yr.AllMusic.MusicPlay;

import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.MusicAPI.SongLyric.ShowOBJ;
import Color_yr.AllMusic.MusicPlay.SendHud.Hud;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class PlayGo extends Thread {

    private int count = 0;
    private final Runnable runnable = () -> {
        PlayMusic.MusicNowTime += 10;
        count++;
        if (count == 100) {
            PlayMusic.MusicLessTime--;
            count = 0;
        }
    };
    private int times = 0;
    private final Runnable runnable1 = () -> {
        ShowOBJ show = PlayMusic.Lyric.checkTime(PlayMusic.MusicNowTime);
        if (show != null) {
            PlayMusic.nowLyric = show;
            times = 0;
            Hud.SendHudLyricData(show);
        } else {
            times++;
            if (times == 500 && PlayMusic.nowLyric != null) {
                times = 0;
                Hud.SendHudLyricData(PlayMusic.nowLyric);
            }
        }
    };

    private ScheduledExecutorService service;
    private ScheduledExecutorService service1;

    public void closeTimer() {
        if (service != null) {
            service.shutdown();
            service = null;
        }
        if (service1 != null) {
            service1.shutdown();
            service1 = null;
        }
    }

    private void startTimer() {
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, 10, TimeUnit.MILLISECONDS);
        if (PlayMusic.Lyric.isHaveLyric()) {
            service1 = Executors.newSingleThreadScheduledExecutor();
            service1.scheduleAtFixedRate(runnable1, 0, 2, TimeUnit.MILLISECONDS);
        }
    }

    public void clear() {
        PlayMusic.MusicNowTime = 0;
        PlayMusic.MusicAllTime = 0;
        PlayMusic.MusicLessTime = 0;
        PlayMusic.Lyric = null;
        PlayMusic.nowLyric = null;
        PlayMusic.NowPlayMusic = null;
        closeTimer();
        Hud.clearHud();
    }

    @Override
    public synchronized void run() {
        while (true) {
            if (PlayMusic.getSize() == 0) {
                try {
                    Hud.SendHudNowData();
                    Hud.SendHudListData();
                    if (AllMusic.Side.NeedPlay()) {
                        String ID = AllMusic.Music.GetListMusic();
                        if (ID != null) {
                            AllMusic.Side.RunTask(() -> PlayMusic.addMusic(ID, "空闲列表", true));
                        }
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                AllMusic.Side.SendHudSaveAll();
                PlayMusic.NowPlayMusic = PlayMusic.getMusic(0);
                PlayMusic.remove(0);

                String url = AllMusic.Music.GetPlayUrl(PlayMusic.NowPlayMusic.getID());
                if (url == null) {
                    String data = AllMusic.getMessage().getMusicPlay().getNoCanPlay();
                    AllMusic.Side.bqt(data.replace("%MusicID%", PlayMusic.NowPlayMusic.getID()));
                    continue;
                }

                PlayMusic.Lyric = AllMusic.Music.getLyric(PlayMusic.NowPlayMusic.getID());

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
                    AllMusic.Side.Send("[Play]" + url, true);
                    try {
                        while (PlayMusic.MusicLessTime > 0) {
                            Hud.SendHudNowData();
                            Hud.SendHudListData();
                            if (!AllMusic.Side.NeedPlay()) {
                                PlayMusic.MusicLessTime = 1;
                            }
                            if (PlayMusic.VoteTime > 0) {
                                PlayMusic.VoteTime--;
                                if (PlayMusic.VoteTime == 0) {
                                    AllMusic.clearVote();
                                    AllMusic.Side.bqt(AllMusic.getMessage().getVote().getTimeOut());
                                } else {
                                    int players = AllMusic.Side.GetAllPlayer();
                                    if (AllMusic.getVoteCount() >= AllMusic.getConfig().getMinVote()
                                            || (players <= AllMusic.getConfig().getMinVote()
                                            && players <= AllMusic.getVoteCount())) {
                                        AllMusic.Side.bqt(AllMusic.getMessage().getVote().getDo());
                                        AllMusic.Side.Send("[Stop]", false);
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
