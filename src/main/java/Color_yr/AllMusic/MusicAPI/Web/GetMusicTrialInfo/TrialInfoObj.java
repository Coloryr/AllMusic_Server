package Color_yr.AllMusic.MusicAPI.Web.GetMusicTrialInfo;

import java.util.List;

public class TrialInfoObj {
    private List<song> data;

    public boolean isTrial() {
        if (data.size() == 0)
            return false;
        song song = data.get(0);
        freeTrialInfo info = song.getFreeTrialInfo();
        if (info == null && song.getFee() == 0)
            return false;
        return true;
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
    private int fee;

    public int getFee() {
        return fee;
    }

    public freeTrialInfo getFreeTrialInfo() {
        return freeTrialInfo;
    }
}