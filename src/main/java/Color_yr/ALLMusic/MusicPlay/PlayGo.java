package Color_yr.ALLMusic.MusicPlay;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.MusicAPI.SongLyric.ShowOBJ;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class PlayGo extends Thread {

    private int count = 0;
    private final Runnable runnable = () -> {
        PlayMusic.MusicNowTime += 10;
        count++;
        if (count == 100) {
            PlayMusic.MusicAllTime--;
            count = 0;
        }
    };
    private int times = 0;
    private final Runnable runnable1 = () -> {
        ShowOBJ show = PlayMusic.Lyric.checkTime(PlayMusic.MusicNowTime);
        if (show != null) {
            PlayMusic.nowLyric = show;
            times = 0;
            ALLMusic.Side.SendLyric(PlayMusic.nowLyric.getString());
            if (ALLMusic.VVEnable) {
                ALLMusic.VV.SendLyric(show);
            }
        } else {
            times++;
            if (times == 1000 && PlayMusic.nowLyric != null) {
                times = 0;
                ALLMusic.Side.SendLyric(PlayMusic.nowLyric.getString());
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
        PlayMusic.Lyric = null;
        PlayMusic.nowLyric = null;
        ALLMusic.Side.SendLyric("");
        PlayMusic.NowPlayMusic = null;
        closeTimer();
        if (ALLMusic.VVEnable) {
            ALLMusic.VV.clear();
        }
    }

    @Override
    public synchronized void run() {
        while (true) {
            if (PlayMusic.getSize() == 0) {
                try {
                    if (ALLMusic.Side.NeedPlay()) {
                        String ID = ALLMusic.Music.GetListMusic();
                        if (ID != null) {
                            ALLMusic.Side.RunTask(() -> PlayMusic.addMusic(ID, "空闲列表", true));
                        }
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                PlayMusic.NowPlayMusic = PlayMusic.getMusic(0);
                PlayMusic.remove(0);

                String url = ALLMusic.Music.GetPlayUrl(PlayMusic.NowPlayMusic.getID());
                if (url == null) {
                    String data = ALLMusic.getMessage().getMusicPlay().getNoCanPlay();
                    ALLMusic.Side.bqt(data.replace("%MusicID%", PlayMusic.NowPlayMusic.getID()));
                    continue;
                }

                PlayMusic.Lyric = ALLMusic.Music.getLyric(PlayMusic.NowPlayMusic.getID());

                if (PlayMusic.NowPlayMusic.getLength() != 0) {
                    PlayMusic.MusicAllTime = (PlayMusic.NowPlayMusic.getLength() / 1000) + 10;
                    String info = ALLMusic.getMessage().getMusicPlay().getPlay();
                    info = info.replace("%MusicName%", PlayMusic.NowPlayMusic.getName())
                            .replace("%MusicAuthor%", PlayMusic.NowPlayMusic.getAuthor())
                            .replace("%MusicAl%", PlayMusic.NowPlayMusic.getAl())
                            .replace("%MusicAlia%", PlayMusic.NowPlayMusic.getAlia())
                            .replace("%PlayerName%", PlayMusic.NowPlayMusic.getCall());
                    ALLMusic.Side.bqt(info);
                    startTimer();
                    ALLMusic.Side.Send("[Play]" + url, true);
                    try {
                        while (PlayMusic.MusicAllTime > 0) {
                            if (ALLMusic.VVEnable) {
                                ALLMusic.VV.SendList();
                                ALLMusic.VV.SendInfo();
                            }
                            if (!ALLMusic.Side.NeedPlay()) {
                                PlayMusic.MusicAllTime = 1;
                            }
                            if (PlayMusic.VoteTime > 0) {
                                PlayMusic.VoteTime--;
                                if (PlayMusic.VoteTime == 0) {
                                    ALLMusic.clearVote();
                                    ALLMusic.Side.bqt(ALLMusic.getMessage().getVote().getTimeOut());
                                } else {
                                    int players = ALLMusic.Side.GetAllPlayer();
                                    if (ALLMusic.getVoteCount() >= ALLMusic.getConfig().getMinVote() ||
                                            (players <= ALLMusic.getConfig().getMinVote() && players <= ALLMusic.getVoteCount())) {
                                        ALLMusic.Side.bqt(ALLMusic.getMessage().getVote().getDo());
                                        ALLMusic.Side.Send("[Stop]", false);
                                        ALLMusic.clearVote();
                                        PlayMusic.VoteTime = 0;
                                        break;
                                    }
                                }
                            }
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        ALLMusic.log.warning("§c歌曲播放出现错误");
                        e.printStackTrace();
                    }
                } else {
                    String data = ALLMusic.getMessage().getMusicPlay().getNoCanPlay();
                    ALLMusic.Side.bqt(data.replace("%MusicID%", PlayMusic.NowPlayMusic.getID()));
                }
                clear();
            }
        }
    }
}
