package Color_yr.AllMusic.musicAPI.web;

import Color_yr.AllMusic.AllMusic;
import Color_yr.AllMusic.http.CryptoUtil;
import Color_yr.AllMusic.http.EncryptType;
import Color_yr.AllMusic.http.HttpClientUtil;
import Color_yr.AllMusic.http.Res;
import Color_yr.AllMusic.musicAPI.web.getMusicInfo.InfoOBJ;
import Color_yr.AllMusic.musicAPI.web.getMusicList.DataOBJ;
import Color_yr.AllMusic.musicAPI.web.getMusicLyric.LyricOBJ;
import Color_yr.AllMusic.musicAPI.web.getMusicSearch.SearchDataOBJ;
import Color_yr.AllMusic.musicAPI.web.getMusicSearch.songs;
import Color_yr.AllMusic.musicAPI.web.getMusicTrialInfo.TrialInfoObj;
import Color_yr.AllMusic.musicAPI.web.getProgramInfo.PrInfoOBJ;
import Color_yr.AllMusic.musicAPI.SongInfo;
import Color_yr.AllMusic.musicAPI.songLyric.LyricDo;
import Color_yr.AllMusic.musicAPI.songLyric.LyricSave;
import Color_yr.AllMusic.musicAPI.songSearch.SearchOBJ;
import Color_yr.AllMusic.musicAPI.songSearch.SearchPage;
import Color_yr.AllMusic.Utils.logs;
import com.google.gson.JsonObject;
import okhttp3.Cookie;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

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
            login(null);
        }
    }

    public void login(Object sender) {
        JsonObject params = new JsonObject();
        params.addProperty("rememberLogin", "true");
        params.addProperty("password", CryptoUtil.getMd5(AllMusic.getConfig().getLoginPass()).toLowerCase());
        if (AllMusic.Cookie.cookieStore.containsKey("music.163.com")) {
            List<Cookie> cookies = AllMusic.Cookie.cookieStore.get("music.163.com");
            for (Cookie item : cookies) {
                if (item.name().equalsIgnoreCase("os")) {
                    cookies.remove(item);
                    break;
                }
            }
            List<Cookie> cookies1 = new CopyOnWriteArrayList<>();
            cookies1.addAll(cookies);
            cookies1.add(new Cookie.Builder().name("os").value("pc").domain("music.163.com").build());
            AllMusic.Cookie.cookieStore.put("music.163.com", cookies1);
            AllMusic.saveCookie();
        }
        if (AllMusic.getConfig().getLoginUser().contains("@")) {
            params.addProperty("username", AllMusic.getConfig().getLoginUser());
            Res res = HttpClientUtil.post("https://music.163.com/weapi/login", params, EncryptType.weapi, null);
            if (res == null || !res.isOk()) {
                if (sender == null)
                    AllMusic.log.info("§d[AllMusic]§c登录失败");
                else
                    AllMusic.Side.sendMessage(sender, "§d[AllMusic]§c登录失败");
                return;
            }
            if (res.getData().contains("502")) {
                if (sender == null)
                    AllMusic.log.info("§d[AllMusic]§c登录失败:账号或密码错误");
                else
                    AllMusic.Side.sendMessage(sender, "§d[AllMusic]§c登录失败:账号或密码错误");
            } else {
                if (sender == null)
                    AllMusic.log.info("§d[AllMusic]§d已登录");
                else
                    AllMusic.Side.sendMessage(sender, "§d[AllMusic]§d已登录");
            }
        } else {
            params.addProperty("countrycode", "86");
            params.addProperty("phone", AllMusic.getConfig().getLoginUser());
            Res res = HttpClientUtil.post("https://music.163.com/weapi/login/cellphone", params, EncryptType.weapi, null);
            if (res == null || !res.isOk()) {
                if (sender == null)
                    AllMusic.log.info("§d[AllMusic]§c登录失败");
                else
                    AllMusic.Side.sendMessage(sender, "§d[AllMusic]§c登录失败");
                return;
            }
            if (res.getData().contains("502")) {
                if (sender == null)
                    AllMusic.log.info("§d[AllMusic]§c登录失败:账号或密码错误");
                else
                    AllMusic.Side.sendMessage(sender, "§d[AllMusic]§c登录失败:账号或密码错误");
            } else {
                if (sender == null)
                    AllMusic.log.info("§d[AllMusic]§d已登录");
                else
                    AllMusic.Side.sendMessage(sender, "§d[AllMusic]§d已登录");
            }
        }
    }

    private SongInfo getMusicDetail(String ID, String player, boolean isList) {
        JsonObject params = new JsonObject();
        params.addProperty("c", "[{\"id\":" + ID + "}]");

        Res res = HttpClientUtil.post("https://music.163.com/api/v3/song/detail", params, EncryptType.weapi, null);
        if (res != null && res.isOk()) {
            InfoOBJ temp = AllMusic.gson.fromJson(res.getData(), InfoOBJ.class);
            if (temp.isok()) {
                params = new JsonObject();
                params.addProperty("ids", "[" + ID + "]");
                params.addProperty("br", "320000");
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

    public SongInfo getMusic(String ID, String player, boolean isList) {
        SongInfo info = getMusicDetail(ID, player, isList);
        if (info != null)
            return info;
        JsonObject params = new JsonObject();
        params.addProperty("id", ID);
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

    public String getPlayUrl(String ID) {
        JsonObject params = new JsonObject();
        params.addProperty("ids", "[" + ID + "]");
        params.addProperty("br", AllMusic.getConfig().getMusicBR());
        Res res = HttpClientUtil.post("https://music.163.com/weapi/song/enhance/player/url", params, EncryptType.weapi, null);
        if (res != null && res.isOk()) {
            try {
                TrialInfoObj obj = AllMusic.gson.fromJson(res.getData(), TrialInfoObj.class);
                return obj.getUrl();
            } catch (Exception e) {
                logs.logWrite(res.getData());
                AllMusic.log.warning("§d[AllMusic]§c播放连接解析错误");
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setList(String ID, Object sender) {
        final Thread thread = new Thread(() -> {
            JsonObject params = new JsonObject();
            params.addProperty("id", ID);
            params.addProperty("n", 100000);
            params.addProperty("s", 8);
            Res res = HttpClientUtil.post("https://music.163.com/api/v6/playlist/detail", params, EncryptType.api, null);
            if (res != null && res.isOk())
                try {
                    isUpdata = true;
                    DataOBJ obj = AllMusic.gson.fromJson(res.getData(), DataOBJ.class);
                    AllMusic.getConfig().getPlayList().addAll(obj.getPlaylist());
                    AllMusic.save();
                    AllMusic.Side.sendMessaget(sender, AllMusic.getMessage().getMusicPlay().getListMusic().getGet().replace("%ListName%", obj.getName()));
                } catch (Exception e) {
                    AllMusic.log.warning("§d[AllMusic]§c歌曲列表获取错误");
                    e.printStackTrace();
                }
            isUpdata = false;
        }, "AllMusic_setList");
        thread.start();
    }

    public LyricSave getLyric(String ID) {
        LyricSave Lyric = new LyricSave();
        JsonObject params = new JsonObject();
        params.addProperty("id", ID);
        params.addProperty("lv", -1);
        params.addProperty("kv", -1);
        params.addProperty("tv", -1);
        Res res = HttpClientUtil.post("https://music.163.com/api/song/lyric", params, EncryptType.api, null);
        if (res != null && res.isOk()) {
            try {
                LyricOBJ obj = AllMusic.gson.fromJson(res.getData(), LyricOBJ.class);
                LyricDo temp = new LyricDo();
                for (int times = 0; times < 3; times++) {
                    if (temp.check(obj)) {
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

    public SearchPage search(String[] name, boolean isDefault) {
        List<SearchOBJ> resData = new ArrayList<>();
        int maxpage;

        StringBuilder name1 = new StringBuilder();
        for (int a = isDefault ? 0 : 1; a < name.length; a++) {
            name1.append(name[a]).append(" ");
        }
        String MusicName = name1.toString();
        MusicName = MusicName.substring(0, MusicName.length() - 1);

        JsonObject params = new JsonObject();
        params.addProperty("s", MusicName);
        params.addProperty("type", 1);
        params.addProperty("limit", 30);
        params.addProperty("offset", 0);

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

    public String getListMusic() {
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
