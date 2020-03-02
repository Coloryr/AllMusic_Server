package Color_yr.ALLMusic.Play;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Http.Get;
import Color_yr.ALLMusic.Lyric.LyricDo;
import Color_yr.ALLMusic.Lyric.ShowOBJ;
import Color_yr.ALLMusic.PlayList.GetList;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class PlayGo extends Thread {

    private int times = 0;
    //创建 run 方法
    Runnable runnable = () -> PlayMusic.MusicNowTime += 10;
    Runnable runnable1 = () -> {
        ShowOBJ show = PlayMusic.Lyric.checkTime(PlayMusic.MusicNowTime);
        if (show != null) {
            String now = show.toString();
            PlayMusic.nowLyric = now != null ? now : PlayMusic.nowLyric;
            times = 0;
            ALLMusic.Side.SendLyric(PlayMusic.nowLyric);
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

    private void startTime() {
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, 10, TimeUnit.MILLISECONDS);
        service1 = Executors.newSingleThreadScheduledExecutor();
        service1.scheduleAtFixedRate(runnable1, 0, 2, TimeUnit.MILLISECONDS);
    }

    @Override
    public synchronized void run() {
        while (true) {
            if (PlayMusic.PlayList.size() == 0) {
                try {
                    Thread.sleep(1000);
                    if (ALLMusic.Side.NeedPlay()) {
                        String obj = GetList.GetMusic();
                        if (obj != null) {
                            PlayMusic.isList = true;
                            PlayMusic.AddMusic(obj, "空闲列表");
                        }
                    }
                } catch (InterruptedException ignored) {
                    ignored.printStackTrace();
                }
            } else {
                PlayMusic.NowPlayMusic = PlayMusic.PlayList.get(0);
                PlayMusic.PlayList.remove(0);
                PlayMusic.MusicNowTime = 0;
                PlayMusic.MusicAllTime = 0;
                PlayMusic.Lyric = null;
                PlayMusic.nowLyric = "";
                PlayMusic.haveLyric = false;
                ALLMusic.Side.SendLyric("");
                String songURL = Get.realURL(ALLMusic.Config.getMusic_Api1() + PlayMusic.NowPlayMusic.getID());
                String Lyric = Get.realData(ALLMusic.Config.getLyric_Api1(), PlayMusic.NowPlayMusic.getID());
                if (Lyric != null) {
                    try {
                        PlayMusic.Lyric = new LyricDo(Lyric);
                        PlayMusic.Lyric.Check();
                        PlayMusic.haveLyric = true;
                    } catch (Exception e) {
                        ALLMusic.log.warning("§d[ALLMusic]§c歌词解析错误");
                        e.printStackTrace();
                    }
                }
                if (songURL != null) {
                    PlayMusic.MusicAllTime = Get.getMusicTime(songURL);
                    ALLMusic.Side.bq("§d[ALLMusic]§2" + "正在播放歌曲" + PlayMusic.NowPlayMusic.getInfo());
                    if (PlayMusic.haveLyric)
                        startTime();
                    ALLMusic.Side.Send("[Play]" + songURL, true);
                    try {
                        while (PlayMusic.MusicAllTime > 0) {
                            if (!ALLMusic.Side.NeedPlay()) {
                                PlayMusic.MusicAllTime = 1;
                            }
                            if (PlayMusic.Vote_time > 0) {
                                PlayMusic.Vote_time--;
                                if (PlayMusic.Vote_time == 0) {
                                    PlayMusic.Vote.clear();
                                    ALLMusic.Side.bq("§d[ALLMusic]§2" + "切歌时间结束");
                                } else {
                                    int players = ALLMusic.Side.GetAllPlayer();
                                    if (PlayMusic.Vote.size() >= ALLMusic.Config.getMinVote() ||
                                            (players <= ALLMusic.Config.getMinVote() && players <= PlayMusic.Vote.size())) {
                                        ALLMusic.Side.bq("§d[ALLMusic]§2" + "已切歌");
                                        ALLMusic.Side.Send("[Stop]", false);
                                        PlayMusic.Vote.clear();
                                        PlayMusic.MusicAllTime = 1;
                                        if (PlayMusic.PlayList.size() == 0) {
                                            ALLMusic.Side.bq("§d[ALLMusic]§2" + "队列中无歌曲");
                                        }
                                        PlayMusic.Vote_time = 0;
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
                    ALLMusic.Side.bq("§d[ALLMusic]§2" + "无效歌曲歌曲" + PlayMusic.NowPlayMusic.getID());
                }
                PlayMusic.NowPlayMusic = null;
            }
        }
    }
}
