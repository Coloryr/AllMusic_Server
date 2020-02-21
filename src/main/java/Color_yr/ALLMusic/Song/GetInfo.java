package Color_yr.ALLMusic.Song;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Http.Get;
import com.google.gson.Gson;

public class GetInfo {
    public static Info Get(String ID, String player) {
        String data = Get.realData(ALLMusic.Config.getInfo_Api1() + ID);
        Info info = new Info(null, null, ID, null, player);
        if (data != null) {
            InfoOBJ temp = new Gson().fromJson(data, InfoOBJ.class);
            info = new Info(temp.getAuthor(), temp.getName(), ID, temp.getAlia(), player);
        }
        return info;
    }
}
