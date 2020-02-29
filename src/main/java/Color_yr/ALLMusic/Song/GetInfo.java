package Color_yr.ALLMusic.Song;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Http.Get;
import com.google.gson.Gson;

public class GetInfo {
    public static SongInfo Get(String ID, String player) {
        String data = Get.realData(ALLMusic.Config.getInfo_Api1() + ID);
        SongInfo info = new SongInfo(null, null, ID, null, player);
        if (data != null) {
            InfoOBJ temp = new Gson().fromJson(data, InfoOBJ.class);
            info = new SongInfo(temp.getAuthor(), temp.getName(), ID, temp.getAlia(), player);
        }
        return info;
    }
}
