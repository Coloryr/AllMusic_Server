package Color_yr.AllMusic.MusicAPI.Web;

import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.Http.CryptoUtil;
import Color_yr.AllMusic.Http.EncryptType;
import Color_yr.AllMusic.Http.HttpClientUtil;
import Color_yr.AllMusic.Http.Res;
import Color_yr.AllMusic.MusicAPI.Web.GetMusicInfo.InfoOBJ;
import Color_yr.AllMusic.MusicAPI.Web.GetMusicInfo.PlayOBJ;
import Color_yr.AllMusic.MusicAPI.Web.GetMusicList.DataOBJ;
import Color_yr.AllMusic.MusicAPI.Web.GetMusicLyric.LyricOBJ;
import Color_yr.AllMusic.MusicAPI.Web.GetMusicSearch.SearchDataOBJ;
import Color_yr.AllMusic.MusicAPI.Web.GetMusicSearch.songs;
import Color_yr.AllMusic.MusicAPI.Web.GetMusicTrialInfo.TrialInfoObj;
import Color_yr.AllMusic.MusicAPI.Web.GetProgramInfo.PrInfoOBJ;
import Color_yr.AllMusic.MusicAPI.SongInfo;
import Color_yr.AllMusic.MusicAPI.SongLyric.LyricDo;
import Color_yr.AllMusic.MusicAPI.SongLyric.LyricSave;
import Color_yr.AllMusic.MusicAPI.SongSearch.SearchOBJ;
import Color_yr.AllMusic.MusicAPI.SongSearch.SearchPage;
import Color_yr.AllMusic.Utils.logs;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class APIMain {

    public int PlayNow = 0;
    public boolean isUpdata;

    public APIMain() {
        AllMusic.log.info("§d[AllMusic]§e使用本地爬虫");
        Res res = HttpClientUtil.get("https://music.163.com", "");
        if (res == null || !res.isOk()) {
            AllMusic.log.info("§d[AllMusic]§c使用本地爬虫连接失败");
            return;
        }
        if (!AllMusic.getConfig().getLoginPass().isEmpty() && !AllMusic.getConfig().getLoginUser().isEmpty()) {
            AllMusic.log.info("§d[AllMusic]§e登陆中");
            login();
        }
    }

    public void login() {
        JSONObject data = new JSONObject();
        data.put("rememberLogin", "true");
        data.put("password", CryptoUtil.getMd5(AllMusic.getConfig().getLoginPass()));
        if (!AllMusic.getConfig().getLoginUser().contains("@")) {
            data.put("username", AllMusic.getConfig().getLoginUser());
            Res res = HttpClientUtil.post("https://music.163.com/weapi/login", data, EncryptType.weapi, null);
            if (res == null || !res.isOk()) {
                AllMusic.log.info("§d[AllMusic]§c登录失败");
            }
        } else {
            data.put("phone", AllMusic.getConfig().getLoginUser());
            Res res = HttpClientUtil.post("https://music.163.com/weapi/login/cellphone", data, EncryptType.weapi, null);
            if (res == null || !res.isOk()) {
                AllMusic.log.info("§d[AllMusic]§c登录失败");
            }
        }
    }

    private SongInfo GetMusicDetail(String ID, String player, boolean isList) {
        JSONObject params = new JSONObject();
        params.put("c", "[{\"id\":" + ID + "}]");

        Res res = HttpClientUtil.post("https://music.163.com/api/v3/song/detail", params, EncryptType.weapi, null);
        if (res != null && res.isOk()) {
            InfoOBJ temp = AllMusic.gson.fromJson(res.getData(), InfoOBJ.class);
            if (temp.isok()) {
                params.clear();
                params = new JSONObject();
                params.put("ids", "[" + ID + "]");
                params.put("br", "320000");
                res = HttpClientUtil.post("https://music.163.com/weapi/song/enhance/player/url", params, EncryptType.weapi, null);
                if (res == null || !res.isOk()) {
                    AllMusic.log.warning("§d[AllMusic]§c版权检索失败");
                    return null;
                }
                TrialInfoObj obj = AllMusic.gson.fromJson(res.getData(), TrialInfoObj.class);
                return new SongInfo(temp.getAuthor(), temp.getName(),
                        ID, temp.getAlia(), player, temp.getAl(), isList, temp.getLength(),
                        temp.getPicUrl(), obj.isTrial(), obj.getFreeTrialInfo());
            }
        }
        return null;
    }

    public SongInfo GetMusic(String ID, String player, boolean isList) {
        SongInfo info = GetMusicDetail(ID, player, isList);
        if (info != null)
            return info;
        JSONObject params = new JSONObject();
        params.put("id", ID);
        Res res = HttpClientUtil.post("https://music.163.com/api/dj/program/detail", params, EncryptType.weapi, null);
        if (res != null && res.isOk()) {
            PrInfoOBJ temp = AllMusic.gson.fromJson(res.getData(), PrInfoOBJ.class);
            if (temp.isOK()) {
                return new SongInfo(temp.getAuthor(), temp.getName(),
                        temp.getId(), temp.getAlia(), player, "电台", isList, temp.getLength(),
                        null, false, null);
            } else {
                AllMusic.log.warning("§d[AllMusic]§c歌曲信息获取为空");
            }
        }
        return info;
    }

    public String GetPlayUrl(String ID) {
        JSONObject params = new JSONObject();
        params.put("ids", "[" + ID + "]");
        params.put("br", "320000");

        Res res = HttpClientUtil.post("https://interface3.music.163.com/eapi/song/enhance/player/url", params, EncryptType.eapi, "/api/song/enhance/player/url");
        if (res != null && res.isOk()) {
            try {
                PlayOBJ obj = AllMusic.gson.fromJson(res.getData(), PlayOBJ.class);
                if (obj.getCode() == 200 && obj.getData() != null) {
                    return obj.getData();
                }
            } catch (Exception e) {
                logs.logWrite(res.getData());
                AllMusic.log.warning("§d[AllMusic]§c播放连接解析错误");
                e.printStackTrace();
            }
        }
        return null;
    }

    public void SetList(String ID, Object sender) {
        Thread thread = new Thread(() -> {
            JSONObject params = new JSONObject();
            params.put("id", ID);
            params.put("n", 100000);
            params.put("s", 8);
            Res res = HttpClientUtil.post("https://music.163.com/api/v6/playlist/detail", params, EncryptType.api, null);
            if (res != null && res.isOk())
                try {
                    isUpdata = true;
                    DataOBJ obj = AllMusic.gson.fromJson(res.getData(), DataOBJ.class);
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

    public LyricSave getLyric(String ID) {
        LyricSave Lyric = new LyricSave();
        JSONObject params = new JSONObject();
        params.put("id", ID);
        params.put("lv", -1);
        params.put("kv", -1);
        params.put("tv", -1);
        Res res = HttpClientUtil.post("https://music.163.com/api/song/lyric", params, EncryptType.api, null);
        if (res != null && res.isOk()) {
            try {
                LyricOBJ obj = AllMusic.gson.fromJson(res.getData(), LyricOBJ.class);
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

    public SearchPage Search(String[] name, boolean isDefault) {
        List<SearchOBJ> resData = new ArrayList<>();
        int maxpage;

        StringBuilder name1 = new StringBuilder();
        for (int a = isDefault ? 0 : 1; a < name.length; a++) {
            name1.append(name[a]).append(" ");
        }
        String MusicName = name1.toString();
        MusicName = MusicName.substring(0, MusicName.length() - 1);

        JSONObject params = new JSONObject();
        params.put("s", MusicName);
        params.put("type", 1);
        params.put("limit", 30);
        params.put("offset", 0);

        Res res = HttpClientUtil.post("https://music.163.com/weapi/search/get", params, EncryptType.weapi, null);
        if (res != null && res.isOk()) {
            SearchDataOBJ obj = AllMusic.gson.fromJson(res.getData(), SearchDataOBJ.class);
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

    public String GetListMusic() {
        if (!isUpdata && AllMusic.getConfig().getPlayList().size() != 0) {
            String ID;
            if (AllMusic.getConfig().isPlayListRandom()) {
                if (AllMusic.getConfig().getPlayList().size() == 0)
                    return null;
                else if (AllMusic.getConfig().getPlayList().size() == 1)
                    return AllMusic.getConfig().getPlayList().get(0);
                ID = AllMusic.getConfig().getPlayList().get(new Random().nextInt(AllMusic.getConfig().getPlayList().size()));
            } else {
                ID = AllMusic.getConfig().getPlayList().get(PlayNow);
                if (PlayNow == AllMusic.getConfig().getPlayList().size() - 1)
                    PlayNow = 0;
                else
                    PlayNow++;
            }
            return ID;
        }
        return null;
    }
}
