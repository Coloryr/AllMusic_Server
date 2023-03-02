package coloryr.allmusic.core.objs.music;

import coloryr.allmusic.core.AllMusic;
import coloryr.allmusic.core.objs.api.music.trialinfo.freeTrialInfo;

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

    protected int length;

    /**
     * 是否是空闲歌单的歌
     */
    protected boolean isList;
    /**
     * 是否是Url歌曲
     */
    protected boolean isUrl;

    public SongInfoObj(String Name, String Url, int Length) {
        this.length = Length;
        playerUrl = Url;
        this.name = Name;
        id = alia = call = al = author = picUrl = "";
        isList = false;
        isUrl = true;
    }

    public SongInfoObj(String Author, String Name, String ID, String Alia, String Call, String Al,
                       boolean isList, int Length, String picUrl, boolean isTrial, freeTrialInfo trialInfo) {
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

    public int getLength() {
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
        String info = AllMusic.getMessage().MusicPlay.MusicInfo;
        info = info.replace("%MusicName%", name == null ? "" : name)
                .replace("%MusicAuthor%", author == null ? "" : author)
                .replace("%MusicAl%", al == null ? "" : al)
                .replace("%MusicAlia%", alia == null ? "" : alia)
                .replace("%PlayerName%", call == null ? "" : call);
        return info;
    }

    public boolean isNull() {
        return name == null;
    }
}
