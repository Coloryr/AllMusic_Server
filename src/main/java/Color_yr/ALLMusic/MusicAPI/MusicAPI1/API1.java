package Color_yr.ALLMusic.MusicAPI.MusicAPI1;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Http.HttpGet;
import Color_yr.ALLMusic.Http.Res;
import Color_yr.ALLMusic.MusicAPI.IMusic;
import Color_yr.ALLMusic.MusicAPI.MusicAPI1.GetMusicInfo.InfoOBJ;
import Color_yr.ALLMusic.MusicAPI.MusicAPI1.GetMusicList.DataOBJ;
import Color_yr.ALLMusic.MusicAPI.MusicAPI1.GetMusicLyric.LyricCheck;
import Color_yr.ALLMusic.MusicAPI.MusicAPI1.GetMusicSearch.SearchDataOBJ;
import Color_yr.ALLMusic.MusicAPI.MusicAPI1.GetMusicSearch.songs;
import Color_yr.ALLMusic.MusicAPI.MusicAPI1.GetMusicInfo.PlayOBJ;
import Color_yr.ALLMusic.MusicAPI.SongSearch.SearchOBJ;
import Color_yr.ALLMusic.MusicAPI.SongSearch.SearchPage;
import Color_yr.ALLMusic.MusicAPI.SongInfo.SongInfo;
import Color_yr.ALLMusic.MusicAPI.SongLyric.LyricDo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class API1 implements IMusic {

    public int PlayNow = 0;
    public boolean isUpdata;

    public API1() {
        ALLMusic.log.info("使用API1");
    }

    @Override
    public SongInfo GetMusic(String ID, String player, boolean isList) {
        Res res = HttpGet.realData(ALLMusic.Config.getMusic_Api1() + "song?id=", ID);
        SongInfo info = null;
        if (res != null && res.isOk()) {
            InfoOBJ temp = new Gson().fromJson(res.getData(), InfoOBJ.class);
            if (temp.isok()) {
                info = new SongInfo(temp.getAuthor(), temp.getName(),
                        ID, temp.getAlia(), player, temp.getAl(), isList, temp.getLength());
            } else {
                ALLMusic.log.warning("§d[ALLMusic]§c歌曲信息获取为空");
            }
        }
        return info;
    }

    @Override
    public String GetPlayUrl(String ID) {
        Res res = HttpGet.realData(ALLMusic.Config.getMusic_Api1() + "/url?quality=192&isRedirect=0&id=", ID);
        if (res != null && res.isOk()) {
            try {
                PlayOBJ obj = new Gson().fromJson(res.getData(), PlayOBJ.class);
                if (obj.getCode() == 200) {
                    return obj.getData();
                } else
                    return null;
            } catch (Exception e) {
                ALLMusic.log.warning("§d[ALLMusic]§c播放连接解析错误");
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public void SetList(String ID) {
        Thread thread = new Thread(() ->
        {
            Res res = HttpGet.realData(ALLMusic.Config.getMusic_Api1() + "songList?id=", ID);
            if (res != null && res.isOk())
                try {
                    isUpdata = true;
                    DataOBJ obj = new Gson().fromJson(res.getData(), DataOBJ.class);
                    ALLMusic.Config.getPlayList().addAll(obj.getPlaylist());
                    ALLMusic.save();
                    ALLMusic.log.info("§d[ALLMusic]§2歌曲列表" + obj.getName() + "获取成功");
                } catch (Exception e) {
                    ALLMusic.log.warning("§d[ALLMusic]§c歌曲列表获取错误");
                    e.printStackTrace();
                }
            isUpdata = false;
        });
        thread.start();
    }

    @Override
    public LyricDo getLyric(String ID) {
        ;
        LyricDo Lyric = new LyricDo();
        Res res = HttpGet.realData("https://api.imjad.cn/cloudmusic/?type=lyric&id=", ID);
        if (res != null && res.isOk()) {
            try {
                LyricCheck temp = new LyricCheck(res.getData());
                for(int times = 0 ; times <3;times++ ) {
                    if (!temp.Check()) {
                        ALLMusic.log.warning("§d[ALLMusic]§c歌词解析错误，正在进行第" + times + "重试");
                    }
                    else {
                        if (!ALLMusic.Config.isSendLyric() && ALLMusic.VV == null) {
                            Lyric.setHaveLyric(false);
                        }
                        Lyric.setLyric(temp.getTemp());
                        return Lyric;
                    }
                    Thread.sleep(1000);
                }
                ALLMusic.log.warning("§d[ALLMusic]§c歌词解析失败");
            } catch (Exception e) {
                ALLMusic.log.warning("§d[ALLMusic]§c歌词解析错误");
                e.printStackTrace();
            }
        }
        return Lyric;
    }

    @Override
    public SearchPage Search(String[] name) {
        List<SearchOBJ> resData = new ArrayList<>();
        int maxpage;

        StringBuilder name1 = new StringBuilder();
        for (int a = 1; a < name.length; a++) {
            name1.append(name[a]).append(" ");
        }
        String MusicName = name1.toString();
        MusicName = MusicName.substring(0, MusicName.length() - 1);
        Res res = HttpGet.realData(ALLMusic.Config.getMusic_Api1() + "search?type=song&keyword=", MusicName);
        if (res != null && res.isOk()) {
            SearchDataOBJ obj = new Gson().fromJson(res.getData(), SearchDataOBJ.class);
            if (obj != null && obj.isok()) {
                List<songs> res1 = obj.getResult();
                SearchOBJ item;
                for (songs temp : res1) {
                    item = new SearchOBJ(String.valueOf(temp.getId()), temp.getName(), temp.getArtists(), temp.getAlbum());
                    resData.add(item);
                }
                maxpage = res1.size() / 10;
                return new SearchPage(resData, maxpage);
            } else {
                ALLMusic.log.warning("§d[ALLMusic]§c歌曲搜索出现错误");

            }
        }
        return null;
    }

    @Override
    public String GetListMusic() {
        if (!isUpdata && ALLMusic.Config.getPlayList().size() != 0) {
            String ID;
            if (ALLMusic.Config.isPlayListRandom()) {
                ID = ALLMusic.Config.getPlayList().get(new Random().nextInt(ALLMusic.Config.getPlayList().size()) - 1);
            } else {
                ID = ALLMusic.Config.getPlayList().get(PlayNow);
                PlayNow = PlayNow == ALLMusic.Config.getPlayList().size() ? 0 : +1;
            }
            return ID;
        }
        return null;
    }
}
