package coloryr.allmusic.core.objs.message;

public class AddMusicObj {
    public String ListFull;
    public String PlayerToMany;
    public String BanMusic;
    public String PlayerBan;
    public String ExistMusic;
    public String Success;
    public String NoPlayer;
    public String NoID;
    public String Cancel;
    public String TimeOut;

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
        if (Cancel == null)
            return true;
        if (PlayerToMany == null)
            return true;
        if (PlayerBan == null)
            return true;
        return TimeOut == null;
    }

    public void init() {
        ListFull = "§d[AllMusic]§c错误，队列已满";
        PlayerToMany = "§d[AllMusic]§c错误，你点了太多的歌";
        PlayerBan = "§d[AllMusic]§c错误，你已被禁止点歌";
        BanMusic = "§d[AllMusic]§c错误，这首歌被禁点了";
        ExistMusic = "§d[AllMusic]§c错误，这首歌已经存在了";
        Success = "§d[AllMusic]§2点歌成功";
        NoPlayer = "§d[AllMusic]§c没有播放的玩家";
        NoID = "§d[AllMusic]§c错误，请输入歌曲数字ID";
        Cancel = "§d[AllMusic]§e点歌被取消";
        TimeOut = "§d[AllMusic]§e点歌被取消，音乐长度过长";
    }

    public static AddMusicObj make() {
        AddMusicObj obj = new AddMusicObj();
        obj.init();

        return obj;
    }
}
