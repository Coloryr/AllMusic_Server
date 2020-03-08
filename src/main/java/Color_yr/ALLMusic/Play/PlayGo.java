package Color_yr.ALLMusic.Play;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.ALLMusicBukkit;
import Color_yr.ALLMusic.Http.Get;
import Color_yr.ALLMusic.Lyric.LyricDo;
import Color_yr.ALLMusic.Lyric.ShowOBJ;
import Color_yr.ALLMusic.PlayList.GetList;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class PlayGo extends Thread {

    private final Runnable runnable = () -> PlayMusic.MusicNowTime += 10;
    private int times = 0;
    private final Runnable runnable1 = () -> {
        ShowOBJ show = PlayMusic.Lyric.checkTime(PlayMusic.MusicNowTime);
        if (show != null) {
            times = 0;
            String now = show.toString();
            PlayMusic.nowLyric = now != null ? now : PlayMusic.nowLyric;
            ALLMusic.Side.SendLyric(PlayMusic.nowLyric);
            if (ALLMusic.VV != null) {
                ALLMusic.VV.SendLyric(show);
            }
        } else {
            times++;
            if (times == 200) {
                times = 0;
                ALLMusic.Side.SendLyric(PlayMusic.nowLyric);
            }
        }
    };

    private ScheduledExecutorService service;
    private ScheduledExecutorService service1;

    public void close() {
        if (service != null) {
            service.shutdown();
            service.shutdownNow();
        }
        if (service1 != null) {
            service1.shutdown();
            service1.shutdownNow();
        }
    }

    private void startTimer() {
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, 10, TimeUnit.MILLISECONDS);
        service1 = Executors.newSingleThreadScheduledExecutor();
        service1.scheduleAtFixedRate(runnable1, 0, 2, TimeUnit.MILLISECONDS);
    }

    @Override
    public synchronized void run() {
        while (true) {
            if (PlayMusic.getSize() == 0) {
                try {
                    if (ALLMusic.Side.NeedPlay()) {
                        String obj = GetList.GetMusic();
                        if (obj != null) {
                            PlayMusic.addMusic(obj, "空闲列表", true);
                        }
                    }
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                PlayMusic.NowPlayMusic = PlayMusic.getMusic(0);
                PlayMusic.remove(0);
                PlayMusic.MusicNowTime = 0;
                PlayMusic.MusicAllTime = 0;
                PlayMusic.Lyric = null;
                PlayMusic.nowLyric = "";
                PlayMusic.haveLyric = false;
                ALLMusic.Side.SendLyric("");
                String Lyric = Get.realData(ALLMusic.Config.getLyric_Api1(), PlayMusic.NowPlayMusic.getID());
                if (Lyric != null) {
                    try {
                        PlayMusic.Lyric = new LyricDo(Lyric);
                        PlayMusic.Lyric.Check();
                        if (ALLMusic.Config.isSendLyric() || ALLMusic.VV != null)
                            PlayMusic.haveLyric = true;
                    } catch (Exception e) {
                        ALLMusic.log.warning("§d[ALLMusic]§c歌词解析错误");
                        e.printStackTrace();
                    }
                }
                if (PlayMusic.NowPlayMusic.getLength() != 0) {
                    PlayMusic.MusicAllTime = PlayMusic.NowPlayMusic.getLength() / 1000;
                    ALLMusic.Side.bq("§d[ALLMusic]§2" + "正在播放歌曲" + PlayMusic.NowPlayMusic.getInfo());
                    if (PlayMusic.haveLyric)
                        startTimer();
                    ALLMusic.Side.Send("[Play]" + ALLMusic.Config.getMusic_Api1() +
                            PlayMusic.NowPlayMusic.getID(), true);
                    try {
                        while (PlayMusic.MusicAllTime > 0) {
                            if (ALLMusic.Config.isVexView() && ALLMusicBukkit.VVEnable) {
                                ALLMusic.VV.SendList();
                                ALLMusic.VV.SendInfo();
                            }
                            if (!ALLMusic.Side.NeedPlay()) {
                                PlayMusic.MusicAllTime = 1;
                            }
                            if (PlayMusic.VoteTime > 0) {
                                PlayMusic.VoteTime--;
                                if (PlayMusic.VoteTime == 0) {
                                    PlayMusic.VotePlayer.clear();
                                    ALLMusic.Side.bq("§d[ALLMusic]§2" + "切歌时间结束");
                                } else {
                                    int players = ALLMusic.Side.GetAllPlayer();
                                    if (PlayMusic.VotePlayer.size() >= ALLMusic.Config.getMinVote() ||
                                            (players <= ALLMusic.Config.getMinVote() && players <= PlayMusic.VotePlayer.size())) {
                                        ALLMusic.Side.bq("§d[ALLMusic]§2" + "已切歌");
                                        ALLMusic.Side.Send("[Stop]", false);
                                        PlayMusic.VotePlayer.clear();
                                        PlayMusic.MusicAllTime = 1;
                                        if (PlayMusic.getSize() == 0) {
                                            ALLMusic.Side.bq("§d[ALLMusic]§2" + "队列中无歌曲");
                                        }
                                        PlayMusic.VoteTime = 0;
                                        PlayMusic.NowPlayMusic = null;
                                    }
                                }
                            }
                            Thread.sleep(1000);
                            PlayMusic.MusicAllTime--;
                        }
                    } catch (InterruptedException e) {
                        ALLMusic.log.warning("§d[ALLMusic]§c歌曲播放出现错误");
                        e.printStackTrace();
                    }
                    if (PlayMusic.haveLyric)
                        close();
                } else {
                    ALLMusic.Side.bq("§d[ALLMusic]§2" + "无效歌曲" + PlayMusic.NowPlayMusic.getID());
                }
                PlayMusic.NowPlayMusic = null;
                if (ALLMusic.VV != null && PlayMusic.getSize() == 0) {
                    ALLMusic.VV.clear();
                }
            }
        }
    }
}
