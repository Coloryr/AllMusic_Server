package com.coloryr.allmusic.server.core.objs.music;

import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.MediaType;
import com.coloryr.allmusic.server.core.objs.api.music.trialinfo.freeTrialInfo;
import com.coloryr.allmusic.server.core.objs.message.PAL;

public class SongInfoObj {
    /**
     * 作者
     */
    protected String author;
    /**
     * 名字
     */
    protected String name;
    /**
     * 音乐ID
     */
    protected String id;
    /**
     * 原曲
     */
    protected String alia;
    /**
     * 点歌者
     */
    protected String call;
    /**
     * 专辑
     */
    protected String al;
    /**
     * 播放链接
     */
    protected String playerUrl;
    /**
     * 图片链接
     */
    protected String picUrl;
    /**
     * 是否是试听
     */
    protected boolean isTrial;
    /**
     * 试听数据
     */
    protected freeTrialInfo trialInfo;
    /**
     * 长度
     */

    protected long length;

    /**
     * 是否是空闲歌单的歌
     */
    protected boolean isList;
    /**
     * 是否是Url歌曲
     */
    protected boolean isUrl;
    /**
     * 音乐类型
     */
    protected MediaType mediaType;

    public SongInfoObj(String name, String url, int length, MediaType type) {
        this.length = length;
        playerUrl = url;
        this.name = name;
        id = alia = call = al = author = picUrl = "";
        isList = false;
        isUrl = true;
        mediaType = type;
    }

    public SongInfoObj(String Author, String Name, String ID, String Alia, String Call, String Al,
                       boolean isList, long Length, String picUrl, boolean isTrial, freeTrialInfo trialInfo) {
        this.author = Author;
        this.name = Name;
        this.id = ID;
        this.alia = Alia;
        this.call = Call;
        this.al = Al;
        this.picUrl = picUrl;
        this.isList = isList;
        this.length = Length;
        this.isTrial = isTrial;
        this.trialInfo = trialInfo;
        this.mediaType = MediaType.Mp3;
    }

    public boolean isUrl() {
        return isUrl;
    }

    public boolean isTrial() {
        return isTrial;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public freeTrialInfo getTrialInfo() {
        return trialInfo;
    }

    public String getPlayerUrl() {
        return playerUrl;
    }

    public String getAl() {
        return al == null ? "" : al;
    }

    public String getAlia() {
        return alia == null ? "" : alia;
    }

    public String getCall() {
        return call == null ? "" : call;
    }

    public String getAuthor() {
        return author == null ? "" : author;
    }

    public long getLength() {
        return length;
    }

    public boolean isList() {
        return isList;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public String getID() {
        return id;
    }

    public String getInfo() {
        String info = AllMusic.getMessage().musicPlay.musicInfo;
        info = info.replace(PAL.musicName, name == null ? "" : name)
                .replace(PAL.musicAuthor, author == null ? "" : author)
                .replace(PAL.musicAl, al == null ? "" : al)
                .replace(PAL.musicAlia, alia == null ? "" : alia)
                .replace(PAL.player, call == null ? "" : call);
        return info;
    }

    public boolean isNull() {
        return name == null;
    }

    public void setName(String info) {
        name = info;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
}
