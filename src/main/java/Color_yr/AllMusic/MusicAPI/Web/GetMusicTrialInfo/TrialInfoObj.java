package Color_yr.AllMusic.MusicAPI.Web.GetMusicTrialInfo;

import java.util.List;

public class TrialInfoObj {
    private List<song> data;

    public boolean isTrial() {
        if (data.size() == 0)
            return false;
        song song = data.get(0);
        return song.getCode() != 200;
    }

    public freeTrialInfo getFreeTrialInfo() {
        song song = data.get(0);
        return song.getFreeTrialInfo() == null ? new freeTrialInfo(){{
            this.setEnd(30);
        }} : song.getFreeTrialInfo();
    }
}

class song {
    private freeTrialInfo freeTrialInfo;
    private int code;

    public int getCode() {
        return code;
    }

    public freeTrialInfo getFreeTrialInfo() {
        return freeTrialInfo;
    }
}