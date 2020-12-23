package Color_yr.AllMusic.MusicAPI.MusicAPI2;

import Color_yr.AllMusic.API.IMusicAPI;
import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.Http.HttpGet;
import Color_yr.AllMusic.Http.Res;
import Color_yr.AllMusic.MusicAPI.MusicAPI2.GetMusicInfo.InfoOBJ;
import Color_yr.AllMusic.MusicAPI.MusicAPI2.GetMusicLyric.LyricOBJ;
import Color_yr.AllMusic.MusicAPI.SongInfo.SongInfo;
import Color_yr.AllMusic.MusicAPI.SongLyric.LyricDo;
import Color_yr.AllMusic.MusicAPI.SongLyric.LyricSave;
import Color_yr.AllMusic.MusicAPI.SongSearch.SearchPage;
import com.google.gson.Gson;

import java.util.Random;

public class API2 implements IMusicAPI {

    public int PlayNow = 0;
    public boolean isUpdata;

    public API2() {
        AllMusic.log.info("§d[AllMusic]§e使用内置爬虫");
    }

    @Override
    public SongInfo GetMusic(String ID, String player, boolean isList) {
        Res res = HttpGet.realData("https://music.163.com/api/song/detail/?ids=%5B", ID + "]");
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
        return "https://music.163.com/song/media/outer/url?id=" + ID;
    }

    @Override
    public void SetList(String ID, Object sender) {
        AllMusic.log.warning("内置API不支持获取歌单，请用外置API，并且登录账户");
    }

    @Override
    public LyricSave getLyric(String ID) {
        LyricSave Lyric = new LyricSave();
        Res res = HttpGet.realData("https://music.163.com/api/song/lyric?os=pc&lv=-1&kv=-1&tv=-1&id=", ID);
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
        AllMusic.log.warning("内置API不支持搜歌，请使用外置API");
        return null;
    }

    @Override
    public String GetListMusic() {
        if (!isUpdata && AllMusic.getConfig().getPlayList().size() != 0) {
            String ID;
            if (AllMusic.getConfig().isPlayListRandom()) {
                if(AllMusic.getConfig().getPlayList().size() == 0)
                    return null;
                ID = AllMusic.getConfig().getPlayList().get(new Random().nextInt( AllMusic.getConfig().getPlayList().size()- 1));
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
