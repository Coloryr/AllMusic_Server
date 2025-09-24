package com.coloryr.allmusic.server.core.music.api;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.decoder.flac.FlacDecoder;
import com.coloryr.allmusic.server.core.decoder.flac.StreamInfo;
import com.coloryr.allmusic.server.core.decoder.mp3.Bitstream;
import com.coloryr.allmusic.server.core.decoder.mp3.Header;
import com.coloryr.allmusic.server.core.music.play.LyricDo;
import com.coloryr.allmusic.server.core.music.play.LyricSave;
import com.coloryr.allmusic.server.core.objs.HttpResObj;
import com.coloryr.allmusic.server.core.objs.MediaType;
import com.coloryr.allmusic.server.core.objs.SearchMusicObj;
import com.coloryr.allmusic.server.core.objs.api.music.info.InfoObj;
import com.coloryr.allmusic.server.core.objs.api.music.list.DataObj;
import com.coloryr.allmusic.server.core.objs.api.music.lyric.WLyricObj;
import com.coloryr.allmusic.server.core.objs.api.music.search.SearchDataObj;
import com.coloryr.allmusic.server.core.objs.api.music.search.songs;
import com.coloryr.allmusic.server.core.objs.api.music.trialinfo.TrialInfoObj;
import com.coloryr.allmusic.server.core.objs.api.program.info.PrInfoObj;
import com.coloryr.allmusic.server.core.objs.enums.EncryptType;
import com.coloryr.allmusic.server.core.objs.message.ARG;
import com.coloryr.allmusic.server.core.objs.music.SearchPageObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.sql.DataSql;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class APIMain {

    private boolean isUpdate;

    public APIMain() {
        AllMusic.log.info("§d[AllMusic3]§e正在初始化网络爬虫");
        HttpClientUtil.init();
        HttpResObj res = HttpClientUtil.get("https://music.163.com", "");
        if (res == null || !res.ok) {
            AllMusic.log.info("§d[AllMusic3]§c初始化网络爬虫连接失败");
        }
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    /**
     * 获取音乐详情
     *
     * @param id     音乐ID
     * @param player 用户名
     * @param isList 是否是空闲列表
     * @return 结果
     */
    private SongInfoObj getMusicDetail(String id, String player, boolean isList) {
        JsonObject params = new JsonObject();
        params.addProperty("c", "[{\"id\":" + id + "}]");

        HttpResObj res = HttpClientUtil.post("https://music.163.com/api/v3/song/detail", params, EncryptType.WEAPI, null);
        if (res != null && res.ok) {
            InfoObj temp = AllMusic.gson.fromJson(res.data, InfoObj.class);
            if (temp.isOk()) {
                params = new JsonObject();
                params.addProperty("ids", "[" + id + "]");
                params.addProperty("br", "320000");
                res = HttpClientUtil.post("https://music.163.com/weapi/song/enhance/player/url", params, EncryptType.WEAPI, null);
                if (res == null || !res.ok) {
                    AllMusic.log.warning("§d[AllMusic3]§c版权检索失败");
                    return null;
                }
                TrialInfoObj obj = AllMusic.gson.fromJson(res.data, TrialInfoObj.class);
                return new SongInfoObj(temp.getAuthor(), temp.getName(),
                        id, temp.getAlia(), player, temp.getAl(), isList, temp.getLength(),
                        temp.getPicUrl(), obj.isTrial(), obj.getFreeTrialInfo());
            }
        }
        return null;
    }

    /**
     * 获取音乐数据
     *
     * @param id     音乐ID
     * @param player 用户名
     * @param isList 是否是空闲列表
     * @return 结果
     */
    public SongInfoObj getMusic(String id, String player, boolean isList) {
        SongInfoObj info = getMusicDetail(id, player, isList);
        if (info != null)
            return info;
        JsonObject params = new JsonObject();
        params.addProperty("id", id);
        HttpResObj res = HttpClientUtil.post("https://music.163.com/api/dj/program/detail", params, EncryptType.WEAPI, null);
        if (res != null && res.ok) {
            PrInfoObj temp = AllMusic.gson.fromJson(res.data, PrInfoObj.class);
            if (temp.isOk()) {
                return new SongInfoObj(temp.getAuthor(), temp.getName(),
                        temp.getId(), temp.getAlia(), player, "电台", isList, temp.getLength(),
                        null, false, null);
            } else {
                AllMusic.log.warning("§d[AllMusic3]§c歌曲信息获取为空");
            }
        }
        return info;
    }

    /**
     * 获取播放链接
     *
     * @param id 音乐ID
     * @return 结果
     */
    public String getPlayUrl(String id) {
        JsonObject params = new JsonObject();
        params.addProperty("ids", "[" + id + "]");
        params.addProperty("br", AllMusic.getConfig().musicBR);
        HttpResObj res = HttpClientUtil.post("https://music.163.com/weapi/song/enhance/player/url", params, EncryptType.WEAPI, null);
        if (res != null && res.ok) {
            try {
                TrialInfoObj obj = AllMusic.gson.fromJson(res.data, TrialInfoObj.class);
                return obj.getUrl();
            } catch (Exception e) {
                AllMusic.log.warning("§d[AllMusic3]§c播放连接解析错误：" + res.data);
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 添加空闲歌单
     *
     * @param id     歌单id
     * @param sender 发送者
     */
    public void setList(String id, Object sender) {
        final Thread thread = new Thread(() -> {
            JsonObject params = new JsonObject();
            params.addProperty("id", id);
            params.addProperty("n", 100000);
            params.addProperty("s", 8);
            HttpResObj res = HttpClientUtil.post("https://music.163.com/api/v6/playlist/detail", params, EncryptType.API, null);
            if (res != null && res.ok)
                try {
                    isUpdate = true;
                    DataObj obj = AllMusic.gson.fromJson(res.data, DataObj.class);
                    DataSql.addIdleList(obj.getPlaylist());
                    AllMusic.side.sendMessageTask(sender, AllMusic.getMessage().musicPlay.listMusic.get.replace(ARG.name, obj.getName()));
                } catch (Exception e) {
                    AllMusic.log.warning("§d[AllMusic3]§c歌曲列表获取错误");
                    e.printStackTrace();
                }
            isUpdate = false;
        }, "AllMusic_setList");
        thread.start();
    }

    /**
     * 获取歌词
     *
     * @param id 歌曲id
     * @return 结果
     */
    public LyricSave getLyric(String id) {
        LyricSave lyric = new LyricSave();
        JsonObject params = new JsonObject();
        params.addProperty("id", id);
        params.addProperty("cp", false);
        params.addProperty("tv", 0);
        params.addProperty("lv", 0);
        params.addProperty("rv", 0);
        params.addProperty("kv", 0);
        params.addProperty("yv", 0);
        params.addProperty("ytv", 0);
        params.addProperty("rtv", 0);
        HttpResObj res = HttpClientUtil.post("https://interface3.music.163.com/eapi/song/lyric/v1",
                params, EncryptType.EAPI, "/api/song/lyric/v1");
        if (res != null && res.ok) {
            try {
                WLyricObj obj = AllMusic.gson.fromJson(res.data, WLyricObj.class);
                LyricDo temp = new LyricDo();
                for (int times = 0; times < 3; times++) {
                    if (temp.check(obj)) {
                        AllMusic.log.warning("§d[AllMusic3]§c歌词解析错误，正在进行第" + times + "重试");
                    } else {
                        if (temp.isHave) {
                            lyric.setHaveLyric(AllMusic.getConfig().sendLyric);
                            lyric.setLyric(temp.getTemp());
                            if (temp.isHaveK) {
                                lyric.setKlyric(temp.getKLyric());
                            }
                        }
                        return lyric;
                    }
                    Thread.sleep(1000);
                }
                AllMusic.log.warning("§d[AllMusic3]§c歌词解析失败");
            } catch (Exception e) {
                AllMusic.log.warning("§d[AllMusic3]§c歌词解析错误");
                e.printStackTrace();
            }
        }
        return lyric;
    }

    /**
     * 搜歌
     *
     * @param name      关键字
     * @param isDefault 是否是默认方式
     * @return 结果
     */
    public SearchPageObj search(String[] name, boolean isDefault) {
        List<SearchMusicObj> resData = new ArrayList<>();
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

        HttpResObj res = HttpClientUtil.post("https://music.163.com/weapi/search/get", params, EncryptType.WEAPI, null);
        if (res != null && res.ok) {
            SearchDataObj obj = AllMusic.gson.fromJson(res.data, SearchDataObj.class);
            if (obj != null && obj.isOk()) {
                List<songs> res1 = obj.getResult();
                SearchMusicObj item;
                for (songs temp : res1) {
                    item = new SearchMusicObj(String.valueOf(temp.getId()), temp.getName(),
                            temp.getArtists(), temp.getAlbum());
                    resData.add(item);
                }
                maxpage = res1.size() / 10;
                return new SearchPageObj(resData, maxpage);
            } else {
                AllMusic.log.warning("§d[AllMusic3]§c歌曲搜索出现错误");

            }
        }
        return null;
    }

    private static int toUnsignedInt(byte x) {
        return ((int) x) & 0xff;//将高24位全部变成0，低8位保持不变
    }

    private static MediaType testMediaType(BufferedInputStream stream) throws IOException {
        stream.mark(10);
        byte[] temp = new byte[4];
        int res = stream.read(temp);
        if (res != 4) {
            return MediaType.Unknow;
        }
        if (temp[0] == 'R' && temp[1] == 'I' && temp[2] == 'F' && temp[3] == 'F') {
            return MediaType.Wav;
        } else if (temp[0] == 'f' && temp[1] == 'L' && temp[2] == 'a' && temp[3] == 'C') {
            return MediaType.Flac;
        } else if (temp[0] == 'I' && temp[1] == 'D' && temp[2] == '3') {
            return MediaType.Mp3;
        } else if (toUnsignedInt(temp[0]) == 0xFF && toUnsignedInt(temp[1]) == 0xE0) {
            return MediaType.Mp3;
        }

        return MediaType.Unknow;
    }

    public static SongInfoObj getUrlMusic(String url) {
        try {
            InputStream stream = HttpClientUtil.get(url);
            if (stream == null) {
                return null;
            }
            BufferedInputStream in = new BufferedInputStream(stream);
            MediaType type = testMediaType(in);
            in.reset();
            float le = 0;
            if (type == MediaType.Mp3) {
                Bitstream bt = new Bitstream(in);
                bt.readFrame();
                bt.closeFrame();
                Header h;
                for (; ; ) {
                    h = bt.readFrame();
                    if (h == null) {
                        break;
                    }
                    bt.closeFrame();
                    le += h.ms_per_frame();
                }
                bt.close();
                return new SongInfoObj(null, url, (int) le, type);
            } else if (type == MediaType.Flac) {
                FlacDecoder flac = new FlacDecoder(in);
                for (; ; ) {
                    Object temp = flac.readAndHandleMetadataBlock();
                    if (temp instanceof StreamInfo) {
                        StreamInfo info = (StreamInfo) temp;
                        le = (float) info.numSamples / info.sampleRate;
                        flac.close();
                        return new SongInfoObj(null, url, (int) le, type);
                    }
                }
            }
        } catch (Exception e) {
            AllMusic.log.warning("§d[AllMusic3]§c歌曲长度解析错误");
            e.printStackTrace();
        }

        return null;
    }
}
