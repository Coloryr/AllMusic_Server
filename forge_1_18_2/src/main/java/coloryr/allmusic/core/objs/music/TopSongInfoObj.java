package coloryr.allmusic.core.objs.music;

import coloryr.allmusic.core.objs.api.music.trialinfo.freeTrialInfo;

public class TopSongInfoObj extends SongInfoObj {
    public TopSongInfoObj() {
        super(null, null, 0);
    }

    public void setAl(String al) {
        this.al = al;
    }

    public void setAlia(String alia) {
        this.alia = alia;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setList(boolean list) {
        isList = list;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setPlayerUrl(String playerUrl) {
        this.playerUrl = playerUrl;
    }

    public void setTrial(boolean trial) {
        isTrial = trial;
    }

    public void setTrialInfo(freeTrialInfo trialInfo) {
        this.trialInfo = trialInfo;
    }

    public void setUrl(boolean url) {
        isUrl = url;
    }
}
