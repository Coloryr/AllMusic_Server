package Color_yr.ALLMusic.MusicAPI.MusicAPI2;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Http.HttpGet;
import Color_yr.ALLMusic.Http.Res;
import Color_yr.ALLMusic.MusicAPI.IMusic;
import Color_yr.ALLMusic.MusicAPI.MusicAPI2.GetMusicInfo.InfoOBJ;
import Color_yr.ALLMusic.MusicAPI.MusicAPI2.GetMusicInfo.PlayOBJ;
import Color_yr.ALLMusic.MusicAPI.MusicAPI2.GetMusicList.DataOBJ;
import Color_yr.ALLMusic.MusicAPI.MusicAPI2.GetMusicLyric.LyricOBJ;
import Color_yr.ALLMusic.MusicAPI.MusicAPI2.GetMusicSearch.SearchDataOBJ;
import Color_yr.ALLMusic.MusicAPI.MusicAPI2.GetMusicSearch.songs;
import Color_yr.ALLMusic.MusicAPI.SongInfo.SongInfo;
import Color_yr.ALLMusic.MusicAPI.SongLyric.LyricDo;
import Color_yr.ALLMusic.MusicAPI.SongLyric.LyricSave;
import Color_yr.ALLMusic.MusicAPI.SongSearch.SearchOBJ;
import Color_yr.ALLMusic.MusicAPI.SongSearch.SearchPage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class API2 implements IMusic {

    public int PlayNow = 0;
    public boolean isUpdata;

    public API2() {
        ALLMusic.log.info("§d[ALLMusic]§e使用API2");
    }

    @Override
    public SongInfo GetMusic(String ID, String player, boolean isList) {
        Res res = HttpGet.realData(ALLMusic.getConfig().getMusic_Api2() + "?type=detail&id=", ID);
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
        Res res = HttpGet.realData(ALLMusic.getConfig().getMusic_Api2() + "?type=song&br=198000&id=", ID);
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
    public void SetList(String ID, Object sender) {
        Thread thread = new Thread(() ->
        {
            Res res = HttpGet.realData(ALLMusic.getConfig().getMusic_Api2() + "?type=playlist&id=", ID);
            if (res != null && res.isOk())
                try {
                    isUpdata = true;
                    DataOBJ obj = new Gson().fromJson(res.getData(), DataOBJ.class);
                    ALLMusic.getConfig().getPlayList().addAll(obj.getPlaylist());
                    ALLMusic.save();
                    ALLMusic.Side.SendMessaget(sender, ALLMusic.getMessage().getMusicPlay().getListMusic().getGet().replace("%ListName%", obj.getName()));
                } catch (Exception e) {
                    ALLMusic.log.warning("§d[ALLMusic]§c歌曲列表获取错误");
                    e.printStackTrace();
                }
            isUpdata = false;
        });
        thread.start();
    }

    @Override
    public LyricSave getLyric(String ID) {
        LyricSave Lyric = new LyricSave();
        Res res = HttpGet.realData(ALLMusic.getConfig().getMusic_Api2() + "?type=lyric&id=", ID);
        if (res != null && res.isOk()) {
            try {
                LyricOBJ obj = new Gson().fromJson(res.getData(), LyricOBJ.class);
                LyricDo temp = new LyricDo();
                for (int times = 0; times < 3; times++) {
                    if (temp.Check(obj)) {
                        ALLMusic.log.warning("§d[ALLMusic]§c歌词解析错误，正在进行第" + times + "重试");
                    } else {
                        Lyric.setHaveLyric(ALLMusic.getConfig().isSendLyric());
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
        Res res = HttpGet.realData(ALLMusic.getConfig().getMusic_Api2() + "?type=search&s=", MusicName);
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
        if (!isUpdata && ALLMusic.getConfig().getPlayList().size() != 0) {
            String ID;
            if (ALLMusic.getConfig().isPlayListRandom()) {
                ID = ALLMusic.getConfig().getPlayList().get(new Random().nextInt(ALLMusic.getConfig().getPlayList().size() - 1));
            } else {
                ID = ALLMusic.getConfig().getPlayList().get(PlayNow);
                if (PlayNow == ALLMusic.getConfig().getPlayList().size())
                    PlayNow = 0;
                else
                    PlayNow++;
            }
            return ID;
        }
        return null;
    }
}
