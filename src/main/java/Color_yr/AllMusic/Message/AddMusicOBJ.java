package Color_yr.AllMusic.Message;

public class AddMusicOBJ {
    private final String ListFull;
    private final String BanMusic;
    private final String ExistMusic;
    private final String Success;
    private final String NoPlayer;
    private final String NoID;

    public AddMusicOBJ() {
        ListFull = "§d[AllMusic]§c错误，队列已满";
        BanMusic = "§d[AllMusic]§c错误，这首歌被禁点了";
        ExistMusic = "§d[AllMusic]§c错误，这首歌已经存在了";
        Success = "§d[AllMusic]§2点歌成功";
        NoPlayer = "§d[AllMusic]§c没有播放的玩家";
        NoID = "§d[AllMusic]§c错误，请输入歌曲数字ID";
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
