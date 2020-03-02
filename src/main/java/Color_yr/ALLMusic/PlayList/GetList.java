package Color_yr.ALLMusic.PlayList;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Http.Get;
import com.google.gson.Gson;

import java.util.Random;

public class GetList {
    public static int PlayNow = 0;
    public static boolean isUpdata;

    public static void GetL(String ID) {
        Thread thread = new Thread(() ->
        {
            String data = Get.realData(ALLMusic.Config.getList_Api1(), ID);
            if (data != null)
                try {
                    isUpdata = true;
                    DataOBJ obj = new Gson().fromJson(data, DataOBJ.class);
                    ALLMusic.Config.getPlayList().addAll(obj.getPlaylist());
                    ALLMusic.Side.save();
                    ALLMusic.log.info("§d[ALLMusic]§c歌曲列表" + ID + "获取成功");
                } catch (Exception e) {
                    ALLMusic.log.warning("§d[ALLMusic]§c歌曲列表获取错误");
                    e.printStackTrace();
                }
            isUpdata = false;
        });
        thread.start();
    }

    public static String GetMusic() {
        if (!isUpdata && ALLMusic.Config.getPlayList().size() != 0) {
            String obj = null;
            if (ALLMusic.Config.isPlayListRandom()) {
                obj = ALLMusic.Config.getPlayList().get(new Random().nextInt(ALLMusic.Config.getPlayList().size() - 1));
            } else {
                obj = ALLMusic.Config.getPlayList().get(PlayNow);
                PlayNow = PlayNow == ALLMusic.Config.getPlayList().size() ? 0 : +1;
            }
            return obj;
        }
        return null;
    }
}
