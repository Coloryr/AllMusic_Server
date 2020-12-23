package Color_yr.AllMusic.MusicAPI.MusicAPI1;

import Color_yr.AllMusic.API.IMusicAPI;
import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.Http.HttpGet;
import Color_yr.AllMusic.Http.Res;
import Color_yr.AllMusic.MusicAPI.MusicAPI1.GetMusicInfo.InfoOBJ;
import Color_yr.AllMusic.MusicAPI.MusicAPI1.GetMusicInfo.PlayOBJ;
import Color_yr.AllMusic.MusicAPI.MusicAPI1.GetMusicList.DataOBJ;
import Color_yr.AllMusic.MusicAPI.MusicAPI1.GetMusicLyric.LyricOBJ;
import Color_yr.AllMusic.MusicAPI.MusicAPI1.GetMusicSearch.SearchDataOBJ;
import Color_yr.AllMusic.MusicAPI.MusicAPI1.GetMusicSearch.songs;
import Color_yr.AllMusic.MusicAPI.SongInfo.SongInfo;
import Color_yr.AllMusic.MusicAPI.SongLyric.LyricDo;
import Color_yr.AllMusic.MusicAPI.SongLyric.LyricSave;
import Color_yr.AllMusic.MusicAPI.SongSearch.SearchOBJ;
import Color_yr.AllMusic.MusicAPI.SongSearch.SearchPage;
import Color_yr.AllMusic.Utils.logs;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class API1 implements IMusicAPI {

    public int PlayNow = 0;
    public boolean isUpdata;

    public API1() {
        AllMusic.log.info("§d[AllMusic]§e使用外置本地爬虫");
    }

    @Override
    public SongInfo GetMusic(String ID, String player, boolean isList) {
        Res res = HttpGet.realData(AllMusic.getConfig().getMusic_Url() + "/song/detail?ids=", ID);
        SongInfo info = null;
        if (res != null && res.isOk()) {
            InfoOBJ temp = new Gson().fromJson(res.getData(), InfoOBJ.class);
            if (temp.isok()) {
                info = new SongInfo(temp.getAuthor(), temp.getName(),
                        ID, temp.getAlia(), player, temp.getAl(), isList, temp.getLength());
            } else {
                AllMusic.log.warning("§d[AllMusic]§c歌曲信息获取为空");
            }
        }
        return info;
    }

    @Override
    public String GetPlayUrl(String ID) {
        Res res = HttpGet.realData(AllMusic.getConfig().getMusic_Url() + "/song/url?dr=320000&id=", ID );
        if (res != null && res.isOk()) {
            try {
                PlayOBJ obj = new Gson().fromJson(res.getData(), PlayOBJ.class);
                if (obj.getCode() == 200) {
                    return obj.getData();
                } else
                    return null;
            } catch (Exception e) {
                logs.logWrite(res.getData());
                AllMusic.log.warning("§d[AllMusic]§c播放连接解析错误");
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
            Res res = HttpGet.realData(AllMusic.getConfig().getMusic_Url() + "/playlist/detail?id=", ID);
            if (res != null && res.isOk())
                try {
                    isUpdata = true;
                    DataOBJ obj = new Gson().fromJson(res.getData(), DataOBJ.class);
                    AllMusic.getConfig().getPlayList().addAll(obj.getPlaylist());
                    AllMusic.save();
                    AllMusic.Side.SendMessaget(sender, AllMusic.getMessage().getMusicPlay().getListMusic().getGet().replace("%ListName%", obj.getName()));
                } catch (Exception e) {
                    AllMusic.log.warning("§d[AllMusic]§c歌曲列表获取错误");
                    e.printStackTrace();
                }
            isUpdata = false;
        });
        thread.start();
    }

    @Override
    public LyricSave getLyric(String ID) {
        LyricSave Lyric = new LyricSave();
        Res res = HttpGet.realData(AllMusic.getConfig().getMusic_Url() + "/lyric?id=", ID);
        if (res != null && res.isOk()) {
            try {
                LyricOBJ obj = new Gson().fromJson(res.getData(), LyricOBJ.class);
                LyricDo temp = new LyricDo();
                for (int times = 0; times < 3; times++) {
                    if (temp.Check(obj)) {
                        AllMusic.log.warning("§d[AllMusic]§c歌词解析错误，正在进行第" + times + "重试");
                    } else {
                        if (temp.isHave) {
                            Lyric.setHaveLyric(AllMusic.getConfig().isSendLyric());
                            Lyric.setLyric(temp.getTemp());
                        }
                        return Lyric;
                    }
                    Thread.sleep(1000);
                }
                AllMusic.log.warning("§d[AllMusic]§c歌词解析失败");
            } catch (Exception e) {
                AllMusic.log.warning("§d[AllMusic]§c歌词解析错误");
                e.printStackTrace();
            }
        }
        return Lyric;
    }

    @Override
    public SearchPage Search(String[] name, boolean isDefault) {
        List<SearchOBJ> resData = new ArrayList<>();
        int maxpage;

        StringBuilder name1 = new StringBuilder();
        for (int a = isDefault ? 0 : 1; a < name.length; a++) {
            name1.append(name[a]).append(" ");
        }
        String MusicName = name1.toString();
        MusicName = MusicName.substring(0, MusicName.length() - 1);
        Res res = HttpGet.realData(AllMusic.getConfig().getMusic_Url() + "/search?keywords=", MusicName);
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
                AllMusic.log.warning("§d[AllMusic]§c歌曲搜索出现错误");

            }
        }
        return null;
    }

    @Override
    public String GetListMusic() {
        if (!isUpdata && AllMusic.getConfig().getPlayList().size() != 0) {
            String ID;
            if (AllMusic.getConfig().isPlayListRandom()) {
                if(AllMusic.getConfig().getPlayList().size() == 0)
                    return null;
                ID = AllMusic.getConfig().getPlayList().get(new Random().nextInt(AllMusic.getConfig().getPlayList().size() - 1));
            } else {
                ID = AllMusic.getConfig().getPlayList().get(PlayNow);
                if (PlayNow == AllMusic.getConfig().getPlayList().size())
                    PlayNow = 0;
                else
                    PlayNow++;
            }
            return ID;
        }
        return null;
    }
}
