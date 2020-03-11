package Color_yr.ALLMusic.SongInfo;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Http.HttpGet;
import Color_yr.ALLMusic.Http.Res;
import com.google.gson.Gson;

public class GetInfo {
    public static SongInfo Get(String ID, String player, boolean isList) {
        Res res = HttpGet.realData(ALLMusic.Config.getNetease_Api() + "song?id=", ID);
        SongInfo info = null;
        if (res != null && res.isOk()) {
            InfoOBJ temp = new Gson().fromJson(res.getData(), InfoOBJ.class);
            if (temp.isok()) {
                info = new SongInfo(temp.getAuthor(), temp.getName(),
                        ID, temp.getAlia(), player, temp.getAl(), isList, temp.getLength());
            } else {
                ALLMusic.log.warning("§d[ALLMusic]§c歌曲信息获取为空");
            }
        }
        return info;
    }
}
