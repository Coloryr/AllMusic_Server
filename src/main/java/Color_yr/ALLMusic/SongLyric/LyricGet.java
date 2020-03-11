package Color_yr.ALLMusic.SongLyric;

import Color_yr.ALLMusic.ALLMusic;
import Color_yr.ALLMusic.Http.HttpGet;
import Color_yr.ALLMusic.Http.Res;

public class LyricGet {
    private int times = 0;

    public LyricDo Get(String ID) {
        Res res = HttpGet.realData("https://api.imjad.cn/cloudmusic/?type=lyric&id=", ID);
        if (res != null && res.isOk()) {
            try {
                LyricDo temp = new LyricDo(res.getData());
                if (!temp.Check()) {
                    times++;
                    if (times > 3) {
                        return new LyricDo();
                    }
                    ALLMusic.log.warning("§d[ALLMusic]§c歌词解析错误，正在进行第" + times + "重试");
                    temp = Get(ID);
                }
                if (!ALLMusic.Config.isSendLyric() && ALLMusic.VV == null)
                    temp.setHaveLyric(false);
                return temp;
            } catch (Exception e) {
                ALLMusic.log.warning("§d[ALLMusic]§c歌词解析错误");
                e.printStackTrace();
            }
        }
        return new LyricDo();
    }
}
