package coloryr.allmusic.message;

public class AddMusicOBJ {
    private String ListFull;
    private String BanMusic;
    private String ExistMusic;
    private String Success;
    private String NoPlayer;
    private String NoID;
    private String Cancel;

    public AddMusicOBJ() {
        ListFull = "§d[AllMusic]§c错误，队列已满";
        BanMusic = "§d[AllMusic]§c错误，这首歌被禁点了";
        ExistMusic = "§d[AllMusic]§c错误，这首歌已经存在了";
        Success = "§d[AllMusic]§2点歌成功";
        NoPlayer = "§d[AllMusic]§c没有播放的玩家";
        NoID = "§d[AllMusic]§c错误，请输入歌曲数字ID";
        Cancel = "§d[AllMusic]§e点歌被取消";
    }

    public boolean check() {
        if (ListFull == null)
            return true;
        if (BanMusic == null)
            return true;
        if (ExistMusic == null)
            return true;
        if (Success == null)
            return true;
        if (NoPlayer == null)
            return true;
        if (NoID == null)
            return true;
        if(Cancel == null)
            return true;

        return false;
    }

    public String getCancel() {
        return Cancel;
    }

    public String getNoID() {
        return NoID;
    }

    public String getNoPlayer() {
        return NoPlayer;
    }

    public String getSuccess() {
        return Success;
    }

    public String getExistMusic() {
        return ExistMusic;
    }

    public String getBanMusic() {
        return BanMusic;
    }

    public String getListFull() {
        return ListFull;
    }
}
