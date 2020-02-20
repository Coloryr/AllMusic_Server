package Color_yr.ALLMusic.Play;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Http.Get;
import Color_yr.ALLMusic.Lyric.LyricDo;
import Color_yr.ALLMusic.Lyric.ShowOBJ;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

class PlayGo extends Thread {

    @Override
    public synchronized void run() {
        while (true) {
            if (PlayMusic.PlayList.size() == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.getMessage();
                }
            } else {
                PlayMusic.NowPlayMusic = PlayMusic.PlayList.get(0);
                PlayMusic.PlayList.remove(0);
                PlayMusic.nowLyric = "";
                PlayMusic.haveLyric = false;
                PlayMusic.Lyric = null;
                ALLMusic.Side.SendLyric("");
                String songURL = Get.realURL(ALLMusic.Config.getMusic_Api1() + PlayMusic.NowPlayMusic);
                String Lyric = Get.realData(ALLMusic.Config.getLyric_Api1() + PlayMusic.NowPlayMusic);
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
                    ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLMusic]§2" + "正在播放歌曲" + PlayMusic.NowPlayMusic));
                    ALLMusic.Side.Send("[Play]" + songURL, true);
                    try {
                        while (PlayMusic.MusicAllTime > 0) {
                            if (PlayMusic.Vote_time > 0) {
                                PlayMusic.Vote_time--;
                                if (PlayMusic.Vote_time == 0) {
                                    PlayMusic.Vote.clear();
                                    ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLMusic]§2" + "切歌时间结束"));
                                } else {
                                    int players = ALLMusic.Side.GetAllPlayer();
                                    if (PlayMusic.Vote.size() >= ALLMusic.Config.getMinVote() || (players <= ALLMusic.Config.getMinVote() && players == PlayMusic.Vote.size())) {
                                        ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLMusic]§2" + "已切歌"));
                                        ALLMusic.Side.Send("[Stop]", false);
                                        PlayMusic.MusicAllTime = 1;
                                        if (PlayMusic.PlayList.size() == 0) {
                                            ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLMusic]§2" + "队列中无歌曲"));
                                        }
                                        PlayMusic.Vote_time = 0;
                                        PlayMusic.NowPlayMusic = null;
                                    }
                                }
                            }
                            if(PlayMusic.haveLyric) {
                                ShowOBJ show = PlayMusic.Lyric.checkTime(PlayMusic.MusicNowTime);
                                if (show != null) {
                                    String now = show.toString();
                                    PlayMusic.nowLyric = now != null ? now : PlayMusic.nowLyric;
                                }
                                ALLMusic.Side.SendLyric(PlayMusic.nowLyric);
                            }
                            Thread.sleep(1000);
                            PlayMusic.MusicAllTime--;
                            PlayMusic.MusicNowTime++;
                        }
                    } catch (InterruptedException e) {
                        e.getMessage();
                    }
                } else {
                    ProxyServer.getInstance().broadcast(new TextComponent("§d[ALLMusic]§2" + "无效歌曲歌曲" + PlayMusic.NowPlayMusic));
                    PlayMusic.NowPlayMusic = null;
                }
            }
        }
    }
}
