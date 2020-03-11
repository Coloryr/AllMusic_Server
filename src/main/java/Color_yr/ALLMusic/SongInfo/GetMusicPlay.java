package Color_yr.ALLMusic.SongInfo;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Http.HttpGet;
import Color_yr.ALLMusic.Http.Res;
import com.google.gson.Gson;

public class GetMusicPlay {
    public static String Get(String ID) {
        Res res = HttpGet.realData(ALLMusic.Config.getNetease_Api() + "/url?quality=192&isRedirect=0&id=", ID);
        if (res != null && res.isOk()) {
            try {
                PlayOBJ obj = new Gson().fromJson(res.getData(), PlayOBJ.class);
                if (obj.getCode() == 200) {
                    return obj.getData();
                } else
                    return null;
            } catch (Exception e) {
                ALLMusic.log.warning("§d[ALLMusic]§c播放连接解析错误");
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
